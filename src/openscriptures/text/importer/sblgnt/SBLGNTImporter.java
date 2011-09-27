/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.util.Iterator;
import java.util.logging.Logger;

import openscriptures.text.Token;
import openscriptures.text.Work;
import openscriptures.text.importer.Importer;


/**
 * @author Neal Audenaert
 */
public class SBLGNTImporter {
    static final Logger LOGGER = Logger.getLogger(SBLGNTImporter.class.getName());
    
    public static void main(String[] args) {
        //		String filename = "SBLGNT.osis.xml";
        String filename = "H:\\IDCH\\Development\\Workspaces\\nttext\\data\\nt\\SBLGNT\\source\\SBLGNT.osis.xml";

        long start = System.currentTimeMillis();
        try {
            Importer importer = new Importer(filename);
            
            importer.addHandler(new HeaderHandler());
            importer.addHandler(new FrontMatterHandler());
            importer.addHandler(new WordHandler());
            importer.addHandler(new ParagraphHandler());
            importer.addHandler(new ChapterHandler());
            importer.addHandler(new BookHandler());
            importer.addHandler(new BookTitleHandler());
            
            importer.parse();
            
            Work work = importer.getWork();
            
            Iterator<Token> i = work.iterator();
            int ix = 0;
            while (i.hasNext() && ix++ < 10) {
                System.out.print(i.next());
            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Elapsed Time: " + (System.currentTimeMillis() - start));
    }
}