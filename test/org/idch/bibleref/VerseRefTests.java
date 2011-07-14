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
public class VerseRefTests extends TestCase {
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
		checkRef(new VerseRef("John.3.16"), "John", 3, 16, null);
		checkRef(new VerseRef("John.3.16!a"), "John", 3, 16, "a");
		checkRef(new VerseRef("John"), "John", null, null, null);
		checkRef(new VerseRef("18"), null, null, 18, null);
		checkRef(new VerseRef("John.3"), "John", 3, null, null);
		checkRef(new VerseRef("3.16"), null, 3, 16, null);
	}
	
	public void checkToString(String strRef) {
		VerseRef ref = new VerseRef(strRef);
		assertEquals(strRef, ref.toString());
	}
	
	public void testToString() {
		checkToString("John.3.16");
		checkToString("John.3.16!a");
		checkToString("John.3");
		checkToString("John");
		checkToString("3.16");
		checkToString("16");
	}
	
	public void testParams() throws InvalidReferenceException {
		VerseRef ref = new VerseRef(order, order.indexOf("John"), 3, 16, "a");
		checkRef(ref, "John", 3, 16, "a");
	}
	
	// TODO Test bad values, invalid book names, verses & chapters out of range, badly formed 
	//      references, etc
	
}
