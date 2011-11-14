/**
 * 
 */
package org.nttext.commentary;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.idch.bible.ref.InvalidReferenceException;
import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRange;
import org.idch.bible.ref.VerseRef;
import org.idch.persist.RepositoryAccessException;
import org.idch.util.Filenames;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Neal Audenaert
 */
public class EntryServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EntryServlet.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //===================================================================================
    // SYMBOLIC CONSTANTS
    //===================================================================================
    
    /** The initialization parameters used to specify where to find the templates 
     *  (defaults to <tt>WEB-INF/templates</tt>). This path will be resolved relative
     *  to the directory this servelet is deployed to. */
    public static final String INIT_PARAM_TEMPL_DIR = "templateDirectory";
    
    public static final String DEFAULT_TEMPL_DIR = "WEB-INF/templates";
    
    //===================================================================================
    // MEMBER VARIABLES
    //===================================================================================
    // TODO add support for XML and JSON requests as well
    
    private Configuration config = null;
    private CommentaryModule commentaryModule = null;
    private Set<String> availableBooks = new HashSet<String>();

    private String defaultEntryRef = "Phil.1.1";        // TODO make configurable
    
    //===================================================================================
    // SERVLET INITIALIZATION METHODS
    //===================================================================================

    /**
     * Initializes the template configuration. To specify a custom directory to
     * use for the view templates set the <tt>templateDirectory</tt> parameter to the 
     * appropriate location in the Servlet configuration. 
     * 
     * @return A <tt>Configuration</tt> object to be used to lookup template files.
     * @throws ServletException
     */
    private void initTemplateConfiguration() throws ServletException {
        String templatePath = this.getInitParameter(INIT_PARAM_TEMPL_DIR);
        if (StringUtils.isBlank(templatePath))
            templatePath = DEFAULT_TEMPL_DIR;
        
        String realPath = this.getServletContext().getRealPath(templatePath);
        File templateDir = new File(realPath);
        if (!templateDir.exists() || !templateDir.isDirectory() || !templateDir.canRead()) {
            String msg = "Expected a readable directory to lookup view templates. ";
            try {
                msg += "Tried '" +  Filenames.getCanonicalPOSIXPath(templateDir) + "'";
            } catch (IOException e) {
                msg += "Tried '" +  templateDir.getAbsolutePath() + "'";
            }

            LOGGER.warn(msg);
            throw new ServletException(msg);
        }

        // load the configuration
        Configuration config = new Configuration();
        try {
            config.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            String msg = "Failed to load tempalte configuration";
            LOGGER.warn(msg, e);
            throw new ServletException(msg, e);
        }

        this.config = config;
    }

    private void initAvailableBooks() throws ServletException {
        // TODO get this from a configuration module
        this.availableBooks.add("phil");
    }
    
    private void initRepositoryModule() throws ServletException {
        // TODO The configurability and flexibility of the modules/repository architecture
        //      is poor. The following tasks are needed. 
        //      1. Define and configure using context.xml
        //      2. Use JNDI to identify the DataSource to use.
        
        try {
            MySQLCommentaryModule mod = MySQLCommentaryModule.get();
            this.commentaryModule = mod;

        } catch (RepositoryAccessException rae) {
            String msg = "Could not initialize commentary module.";
            LOGGER.warn(msg, rae);
            throw new ServletException(msg, rae);
        }
    }
    
    
    public void init() throws ServletException {
        initTemplateConfiguration();
        initAvailableBooks();
        initRepositoryModule();
    }

    //===================================================================================
    // ERROR RESPONSES
    //===================================================================================


    /**
     * Writes a 404 error response (404 Not Found The requested resource could not be found 
     * but may be available again in the future. Subsequent requests by the client 
     * are permissible.)
     * 
     * @param msg A message explaining the error.
     * @param resp The response object to write to.
     * @throws IOException If there are problems writing to the response object.
     */
    private void write404(String msg, HttpServletResponse resp) throws IOException {
        // TODO need to make the error handling more robust and user friendly.
        //      1. We need well designed error pages.
        //      2. Need a fallback if either the page template isn't there or if there 
        //         are problems processing the page.
        //      3. Need to generalize this so that it can be used by different Servlets
        //         and applications.
        //      4. Need to do a better job of setting header values and of generating
        //         appropriately formatted responses (HTML, JSON, XML, etc).
        
        StringWriter page = new StringWriter();
        try {
            Template template = config.getTemplate("/404.html");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());
            data.put("navigation", new Navigation(
                    commentaryModule.getInstanceRepository(), null));

            template.process(data, page);
        } catch (TemplateException ex) {
            page.append("Oops. . . could not process page: " + ex.getMessage());
            ex.printStackTrace(new PrintWriter(page));
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
    }

    /**
     * Writes 400 an error response (400 Bad Request The request cannot be fulfilled due to 
     * bad syntax.)
     * 
     * @param msg A message explaining the error.
     * @param resp The response object to write to.
     * @throws IOException If there are problems writing to the response object.
     */
    private void writeBadRequest(String msg, HttpServletResponse resp) throws IOException {
        StringWriter page = new StringWriter();
        try {
            Template template = config.getTemplate("/400.html");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());
            data.put("navigation", new Navigation(
                    commentaryModule.getInstanceRepository(), null));

            template.process(data, page);
        } catch (TemplateException ex) {
            page.append("Oops. . . could not process page: " + ex.getMessage());
            ex.printStackTrace(new PrintWriter(page));
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
    }


    //===================================================================================
    // HELPER METHODS
    //===================================================================================

    private boolean checkAvaliableBook(Passage passage, HttpServletResponse resp) throws IOException { 
        // TODO Do a better job of formatting and ordering books. More efficient 
        //      string processing with StringBuilder. 
        
        boolean valid = true;
        VerseRef ref = passage.getFirst();
        String bk = ref.getBookIdentifier();
        if (bk == null) {
            valid = false;
            String message = "Only a few books are available during our preview phase. " +
                    "Unfortunately, " + ref.getBookName() + " isn't one " +
                    "of them. Please select from the following books: ";
            
            writeBadRequest(message, resp);
            
        } else if (!this.availableBooks.contains(bk.toLowerCase())) {
            valid = false;
            
            String message = "Only a few books are available during our preview phase. " +
                "Unfortunately, " + ref.getBookName() + " isn't one " +
                "of them. Please select from the following books: ";
            boolean first = true;
            for (String book : this.availableBooks) {
                if (!first) message += ", ";
                else first = false;
                
                message += book;
            }
            
            write404(message, resp);
        }

        return valid;
    }

    private VerseRange getPassage(HttpServletRequest req, HttpServletResponse resp) 
        throws IOException {
        String ref = req.getPathInfo();
        ref = StringUtils.trimToNull(ref);
        ref = (ref == null)                
                ? ref = defaultEntryRef 
                : ref.replaceFirst("/", "");

        VerseRange passage = null;
        try {
            passage = new VerseRange(ref);
        } catch (InvalidReferenceException ire) {
            String msg = "Invalid passage reference (" + ref + "): " + ire;
            LOGGER.warn(msg, ire);
            writeBadRequest(msg, resp);
            passage = null;
        }

        if (checkAvaliableBook(passage, resp)) {
            VerseRef first = passage.getFirst();
            if (!first.isChapterSpecified()) 
                first.setChapter(1);
            
            if (!first.isVerseSpecified()) 
                first.setVerse(1);
            
        } else {
            passage = null;
        }

        return passage;
    }

    /**
     * Retrieve the requested entry based on the HTTP request.
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private EntryInstance getEntry(HttpServletRequest req, HttpServletResponse resp) 
        throws IOException {
        EntryInstance instance = null;
        VerseRange passage = getPassage(req, resp);
        if (passage == null)  // FIXME need to check the return value and behave appropriately
            return null;

        InstanceRepository repo = commentaryModule.getInstanceRepository();
        instance = repo.find(passage);
        if (instance == null) {
            String message = "There is no entry for this passage (" + passage.toOsisId() + ").";

            write404(message, resp);
            return null;
        }

        return instance;
    }


    //===================================================================================
    // HTTP REQUEST METHOD HANDLERS
    //===================================================================================

    /**
     * 
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringWriter page = new StringWriter();
        Template template = config.getTemplate("/entry.html");

        EntryInstance e = getEntry(req, resp);
        if (e == null) {
            assert resp.isCommitted();
            return;
        }
        
        
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());

            data.put("entry", new InstanceData(commentaryModule, e));
            data.put("navigation", new Navigation(commentaryModule.getInstanceRepository(), e));

            template.process(data, page);
        } catch (TemplateException ex) {
            // we'll go ahead and return the mangled page for de-bugging purposes
            LOGGER.warn("Error processing template page for entry: " + e.getPassage(), ex);
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
    }

}
