/**
 * 
 */
package openscriptures.text.importer.sblgnt;

import java.util.SortedSet;
import java.util.logging.Logger;

import openscriptures.text.Structure;
import openscriptures.text.StructureFacade;
import openscriptures.text.StructureRepository;
import openscriptures.text.Work;
import openscriptures.text.impl.JPAStructureRepository;
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
            StructureRepository repo = new JPAStructureRepository();
            Importer importer = new Importer(filename, repo);
            
            importer.addHandler(new HeaderHandler());
            importer.addHandler(new FrontMatterHandler());
            importer.addHandler(new WordHandler());
            importer.addHandler(new ParagraphHandler());
            importer.addHandler(new VerseHandler());
            importer.addHandler(new ChapterHandler());
            importer.addHandler(new BookHandler());
            importer.addHandler(new BookTitleHandler());
            
            importer.parse();
            
            Work work = importer.getWork();
            
            StructureFacade facade = repo.getStructureFacade(work);
            SortedSet<Structure> books = facade.find("book");
            System.out.println("Number of Books: " + books.size());
            
            
            SortedSet<Structure> chapters = facade.find("chapter", books.first());
            System.out.println("Number of Chapters: " + chapters.size());
            
//            Iterator<Token> i = work.iterator();
//            int ix = 0;
//            while (i.hasNext() && ix++ < 10) {
//                System.out.print(i.next());
//            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Elapsed Time: " + (System.currentTimeMillis() - start));
    }
}