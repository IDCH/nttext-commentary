/**
 * 
 */
package org.nttext.commentary;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.idch.util.Filenames;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Neal Audenaert
 */
public class EntryServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EntryServlet.class.getName());
    
    private Configuration config = null;
    
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
            
            LOGGER.warning(msg);
//            LogService.logError(msg, LOGGER);
            throw new ServletException(msg);
        }
            
        // load the configuration
        Configuration config = new Configuration();
        try {
            config.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            String msg = "Failed to load tempalte configuration";
            LOGGER.warning(msg);
//            LogService.logError(msg, LOGGER, e);
            throw new ServletException(msg, e);
        }
        
        return config;
    }
    
    public void init() throws ServletException {
        this.config = loadTemplateConfiguration();
        
    }
    
    /**
     * Retrieve the requested entry based on the HTTP request.
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private Entry getEntry(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
        Entry e = TestEntryFactory.getDefaultEntry();
        
        return e;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringWriter page = new StringWriter();
        Template template = config.getTemplate("/entry.html");
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", new PageDetails());
            data.put("navigation", new Navigation());
            
            Entry e = getEntry(req, resp);
            data.put("entry", new EntryData(e));
            
            template.process(data, page);
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(page.getBuffer().toString());
        out.flush();
        
    }

}
