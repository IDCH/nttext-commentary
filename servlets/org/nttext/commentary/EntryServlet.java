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
    
    private Configuration config = null;
    private CommentaryModule commentaryModule = null;
    private Set<String> availableBooks = new HashSet<String>();
    

    //===================================================================================
    // SERVLET INITIALIZATION METHODS
    //===================================================================================
    
    /**
     * Initializes the template configuration. To specify a custom directory to
     * use for the view templates set the <tt>templateDirectory</tt> parameter to the 
     * appropriate location in the servlet configuration. 
     * 
     * @return A <tt>Configuration</tt> object to be used to lookup template files.
     * @throws ServletException
     */
    private Configuration loadTemplateConfiguration() throws ServletException {
        // TODO test proper error handling
        // TODO test better documentation
        
        String templatePath =  this.getInitParameter("templateDirectory");
        if (StringUtils.isBlank(templatePath))
            templatePath = "templates/";
        
        // Lookup the template directory
        File templateDir = new File(templatePath);
        if (!templateDir.exists() || !templateDir.isDirectory() || !templateDir.canRead()) {
            String msg = "Expected a readable directory to lookup view templages. ";
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
        
        return config;
    }
    
    public void init() throws ServletException {
        this.config = loadTemplateConfiguration();
        
        // TODO get this from a configureation module
        this.availableBooks.add("phil");
        
        try {
            MySQLCommentaryModule mod = MySQLCommentaryModule.get();
            // FIXME TEMPORARY TEST HARNESS CODE -- REMOVE THIS FOR PRODUCTION
            if (!mod.probe()) {
                mod.create();
            }
            // FIXME END TEMPORARY TEST HARNESS CODE -- REMOVE THIS FOR PRODUCTION

            this.commentaryModule = mod;
            
        } catch (RepositoryAccessException rae) {
            String msg = "Could not initialize commentary module.";
            LOGGER.warn(msg, rae);
            throw new ServletException(msg, rae);
        }
    }
    
    //===================================================================================
    // ERROR RESPONSES
    //===================================================================================
    
    /**
     * 
     * @param msg
     * @param resp
     * @throws IOException
     */
    private void write404(String msg, HttpServletResponse resp) throws IOException {
        StringWriter page = new StringWriter();
        try {
            Template template = config.getTemplate("/entry.html");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());
            
//            data.put("entry", new InstanceData(commentaryModule, e));
            data.put("navigation", new Navigation());
            
            template.process(data, page);
        } catch (TemplateException ex) {
            // TODO do something more sensible here
            page.append("Oops. . . could not process page: " + ex.getMessage());
            ex.printStackTrace(new PrintWriter(page));
        }
        
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        // TODO I think we need to set the main error message in the header
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
        
    }
    
    private void writeBadRequest(String msg, HttpServletResponse resp) throws IOException {
        // TODO implement me
    }

    
    //===================================================================================
    // HELPER METHODS
    //===================================================================================
    
    private boolean checkAvaliableBook(Passage passage, HttpServletResponse resp) throws IOException { 
        VerseRef ref = passage.getFirst();
        String bk = ref.getBookIdentifier();
        if (!this.availableBooks.contains(bk.toLowerCase())) {
            // TODO format 404 with preview only message
            String message = "Only a few books are available during our preview phase. " +
                    "Unfortunately, " + ref.getBookName() + " isn't one " +
                    "of them. Please select from the following books: ";
            write404(message, resp);
            return false;
        } 
        
        return true;
    }

    private VerseRange getPassage(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
        String ref = req.getPathInfo();
        ref = StringUtils.trimToNull(ref.replaceFirst("/", ""));
        if (ref == null)                // FIXME magic string
            ref = "Phil.1.1";           // set to default verse
        
        VerseRange passage = null;
        try {
            passage = new VerseRange(ref);
        } catch (InvalidReferenceException ire) {
            String msg = "Invalid passage reference (" + ref + "): " + ire;
            LOGGER.warn(msg, ire);
            writeBadRequest(msg, resp);
            passage = null;
        }
        
        // TODO check to see if verse is specified.
        
        return checkAvaliableBook(passage, resp) ? passage : null;
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
        if (passage == null)
            return null;
        
        InstanceRepository repo = commentaryModule.getInstanceRepository();
        instance = repo.find(passage);
        if (instance == null) {
            String message = "There is no entry for this passage (" + passage.toString() + ").";
            
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
            if (!resp.isCommitted()) {
                // TODO write 404
            }
            
            return;
        }
        // TODO handle XML and JSON requests as well
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());
            
            data.put("entry", new InstanceData(commentaryModule, e));
            data.put("navigation", new Navigation());
            
            template.process(data, page);
        } catch (TemplateException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
    }

}
