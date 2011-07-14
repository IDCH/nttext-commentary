/**
 * 
 */
package org.idch.bibleref;

import org.idch.bibleref.BookOrder;
import org.idch.bibleref.InvalidReferenceException;
import org.idch.bibleref.VerseRef;

import junit.framework.TestCase;

/**
 * @author Neal Audenaert
 */
public class VerseRangeTests extends TestCase {
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
		VerseRange range = null;
		range = new VerseRange("John.3.16-18");
		checkRef(range.getStartVerse(), "John", 3, 16, null);
		checkRef(range.getEndVerse(), "John", 3, 18, null);
		
		range = new VerseRange("John.3.16-4.5");
		checkRef(range.getStartVerse(), "John", 3, 16, null);
		checkRef(range.getEndVerse(), "John", 4, 5, null);
		
		range = new VerseRange("John.3.16-John.8.5");
		checkRef(range.getStartVerse(), "John", 3, 16, null);
		checkRef(range.getEndVerse(), "John", 8, 5, null);
		
		range = new VerseRange("John.3.16-Acts.2");
		checkRef(range.getStartVerse(), "John", 3, 16, null);
		checkRef(range.getEndVerse(), "Acts", 2, null, null);
		
		range = new VerseRange("John.3-5");
		checkRef(range.getStartVerse(), "John", 3, null, null);
		checkRef(range.getEndVerse(), "John", 5, null, null);
	}
	
	public void checkToString(String strRef) {
//		VerseRef ref = new VerseRef(strRef);
//		assertEquals(strRef, ref.toString());
	}
	
	public void testToString() {
//		checkToString("John.3.16");
//		checkToString("John.3.16!a");
//		checkToString("John.3");
//		checkToString("John");
//		checkToString("3.16");
//		checkToString("16");
	}
	
	public void testParams() throws InvalidReferenceException {
//		VerseRef ref = new VerseRef(order, order.indexOf("John"), 3, 16, "a");
//		checkRef(ref, "John", 3, 16, "a");
	}
	
	// TODO Test invalid references, eg: out of order, multiple hyphens, other separators, etc.
	
}
