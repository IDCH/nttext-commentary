/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.idch.util.PersistenceUtil;

import openscriptures.text.Work;
import openscriptures.text.impl.JPAStructureRepository;
import openscriptures.text.impl.JPATokenRepository;
import openscriptures.text.impl.JPAWorkRepository;
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
    
    public SBLGNTImporter() {
        
    }
    
    public SBLGNTImporter(String filename) {
        this.filename = filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public void doImport() throws Exception {
        long start = System.currentTimeMillis();
        try {
            EntityManagerFactory emf = PersistenceUtil.getEMFactory("nttext");
            Context context = new Context(
                    new JPAWorkRepository(emf),
                    new JPATokenRepository(emf),
                    new JPAStructureRepository(emf));
            Importer importer = new Importer(filename, context);
            
            context.inText = true;
            importer.addHandler(new HeaderHandler());
            importer.addHandler(new FrontMatterHandler());
            importer.addHandler(new WordHandler());
//            importer.addHandler(new ParagraphHandler());
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
        
        SBLGNTImporter importer = new SBLGNTImporter(filename);

        try {
            importer.doImport();

        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
      
        System.out.println();
        System.out.println();
        System.out.println("Elapsed Time: " + importer.getElapsedTime());
    }
}