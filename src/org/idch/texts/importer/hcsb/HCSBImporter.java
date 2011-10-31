/**
 * 
 */
package org.idch.texts.importer.hcsb;


import org.apache.log4j.Logger;
import org.idch.texts.TextModule;
import org.idch.texts.TextModuleInstance;
import org.idch.texts.Work;
import org.idch.texts.importer.Context;
import org.idch.texts.importer.Importer;

/**
 * @author Neal Audenaert
 */
public class HCSBImporter {
    static final Logger LOGGER = Logger.getLogger(HCSBImporter.class);
    
    private String dirname = "";
    
    private long elapsedTime = 0;
    private Work work = null;
    
    private TextModule m_repo;
    public HCSBImporter(TextModule repo) {
        m_repo = repo;
    }
    
    public HCSBImporter(String filename, TextModule repo) {
        this.dirname = filename;
        m_repo = repo;
    }
    
    public void setFilename(String filename, TextModule repo) {
        this.dirname = filename;
        m_repo = repo;
    }
    
    public void doImport() throws Exception {
        long start = System.currentTimeMillis();
        Context context = null;
        try {
            // TODO this is going to need to loop through all files in the directory.
            context = new Context(m_repo);
            Importer importer = new Importer(dirname, context);
            
//            importer.addHandler(new WordHandler());
//            importer.addHandler(new HeaderHandler());
//            importer.addHandler(new FrontMatterHandler());
//            importer.addHandler(new ParagraphHandler());
//            importer.addHandler(new VerseHandler());
//            importer.addHandler(new ChapterHandler());
//            importer.addHandler(new BookHandler());
//            importer.addHandler(new BookTitleHandler());
            
            importer.parse();
            this.work = importer.getWork();
            
        } catch (Exception ex) {
            LOGGER.error("Failed to import SBLGNT: " + dirname, ex);
            throw ex;
        } finally {
            this.elapsedTime = System.currentTimeMillis() - start;
            LOGGER.info("Elapsed Time: " + this.elapsedTime);
        }
        
    }
    
    public long getElapsedTime() {
        return this.elapsedTime;
    }
    
    public Work getWork() {
        return this.work;
    }

    //===================================================================================
    // MAIN METHOD
    //===================================================================================
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        //      String filename = "SBLGNT.osis.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\HCSB\\source";
        
        HCSBImporter importer;
        try {
            importer = new HCSBImporter(filename, TextModuleInstance.get());
            importer.doImport();

            System.out.println();
            System.out.println();
            System.out.println("Elapsed Time: " + importer.getElapsedTime());
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
      
    }
}
