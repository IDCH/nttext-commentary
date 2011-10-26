/**
 * 
 */
package openscriptures.text.importer.sblgnt;


import org.apache.log4j.Logger;

import openscriptures.text.TextRepository;
import openscriptures.text.Work;
import openscriptures.text.impl.MySQLTextRepository;
import openscriptures.text.importer.Context;
import openscriptures.text.importer.Importer;


/**
 * @author Neal Audenaert
 */
public class SBLGNTImporter {
    static final Logger LOGGER = Logger.getLogger(SBLGNTImporter.class);
    
    private String filename = "";
    
    private long elapsedTime = 0;
    private Work work = null;
    
    private TextRepository m_repo;
    public SBLGNTImporter(TextRepository repo) {
        m_repo = repo;
    }
    
    public SBLGNTImporter(String filename, TextRepository repo) {
        this.filename = filename;
        m_repo = repo;
    }
    
    public void setFilename(String filename, TextRepository repo) {
        this.filename = filename;
        m_repo = repo;
    }
    
    public void doImport() throws Exception {
        long start = System.currentTimeMillis();
        Context context = null;
        try {
            context = new Context(m_repo);
            Importer importer = new Importer(filename, context);
            
            importer.addHandler(new WordHandler());
            importer.addHandler(new HeaderHandler());
            importer.addHandler(new FrontMatterHandler());
            importer.addHandler(new ParagraphHandler());
            importer.addHandler(new VerseHandler());
            importer.addHandler(new ChapterHandler());
            importer.addHandler(new BookHandler());
            importer.addHandler(new BookTitleHandler());
            
            importer.parse();
            this.work = importer.getWork();
            
        } catch (Exception ex) {
            LOGGER.error("Failed to import SBLGNT: " + filename, ex);
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
        //		String filename = "SBLGNT.osis.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\SBLGNT\\source\\SBLGNT.osis.xml";
        
        SBLGNTImporter importer;
        try {
            importer = new SBLGNTImporter(filename, MySQLTextRepository.get());
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