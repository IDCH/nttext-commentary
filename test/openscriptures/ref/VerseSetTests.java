/**
 * 
 */
package openscriptures.ref;

import org.idch.bible.ref.BookOrder;
import org.idch.bible.ref.VerseRange;
import org.idch.bible.ref.VerseRef;
import org.idch.bible.ref.VerseSet;

import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class VerseSetTests extends TestCase {
	BookOrder order = null;
	public void setUp() {
		order = BookOrder.KJV;
	}
	
	public void tearDown() {
		
	}
	
	/** Checks to see if a verse reference has the expected properties. */
	private void checkRef(VerseRef ref, String book, Integer ch, Integer vs, String ext) {
		if (book == null) {
			assertNull("Expected null book index", ref.getBookIndex());
			assertNull("Expected null book name", ref.getBookName());
		} else {
			assertTrue("Unexpected book.", ref.getBookIndex() == order.indexOf(book));
			assertEquals("Unexpected book name.", ref.getBookName(), book);
		}
		
		if (ch == null)
			assertNull("Expected null chapter", ref.getChapter());
		else
			assertEquals("Unexpected chapter number", ch, ref.getChapter());
		
		if (vs == null)
			assertNull("Expected null verse", ref.getVerse());
		else
			assertEquals("Unexpected verse number", vs, ref.getVerse());
		
		if (ext == null)
			assertNull("Expected null extension", ref.getExtension());
		else
			assertEquals("Unexpected extension", ext, ref.getExtension());
	}
	
	public void testValidReferences() {
//		VerseRange range = null;
		VerseSet set = null;
		VerseRange jn3_16_18 = new VerseRange("John.3.16-18");
		VerseRef jn1_1 = new VerseRef("John.1.1");
		
		set = new VerseSet(jn3_16_18.toOsisId() + "," + jn1_1.toOsisId());
		checkRef(set.getFirst(), "John", 1, 1, null);
		checkRef(set.getLast(), "John", 3, 18, null);
		
//		range = new VerseRange("John.3.16-4.5");
//		checkRef(range.getFirst(), "John", 3, 16, null);
//		checkRef(range.getLast(), "John", 4, 5, null);
//		
//		range = new VerseRange("John.3.16-John.8.5");
//		checkRef(range.getFirst(), "John", 3, 16, null);
//		checkRef(range.getLast(), "John", 8, 5, null);
//		
//		range = new VerseRange("John.3.16-Acts.2");
//		checkRef(range.getFirst(), "John", 3, 16, null);
//		checkRef(range.getLast(), "Acts", 2, null, null);
//		
//		range = new VerseRange("John.3-5");
//		checkRef(range.getFirst(), "John", 3, null, null);
//		checkRef(range.getLast(), "John", 5, null, null);
	}
	

}
