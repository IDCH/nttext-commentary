/**
 * 
 */
package openscriptures.ref;

import org.idch.bible.ref.BookOrder;
import org.idch.bible.ref.InvalidReferenceException;
import org.idch.bible.ref.VerseRef;

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
	
	public void checkCompareTo(String strRefA, String strRefB, int expected) {
	    VerseRef a = new VerseRef(strRefA);
	    VerseRef b = new VerseRef(strRefB);
	    
	    assertEquals("Incorrect comparison for '" + strRefA + ".compareTo(" + strRefB + ")'", 
	            Math.signum(expected), Math.signum(a.compareTo(b)));
	}
	
	public void checkEquality(String strRefA, String strRefB, boolean equal) {
        VerseRef a = new VerseRef(strRefA);
        VerseRef b = new VerseRef(strRefB);
        
        assertEquals("Incorrect equality comparison", equal, a.equals(b));
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
	
	public void testCompareTo() {
	    checkCompareTo("John.3.16!a", "John.3.16!a", 0);
	    checkCompareTo("John.3.16!a", "John.3.16!b", -1);
	    checkCompareTo("John.3.16!b", "John.3.16!a", 1);
        
	    // check verses
	    checkCompareTo("John.3.16", "John.3.16", 0);
	    checkCompareTo("John.3.14", "John.3.17", -1);
	    checkCompareTo("John.3.17", "John.3.14", 1);
	    
	    // check chapters
	    checkCompareTo("John.3", "John.3", 0);
	    checkCompareTo("John.3", "John.4", -1);
	    checkCompareTo("John.3", "John.2", 1);
        checkCompareTo("John.3.18", "John.4.14", -1);   
        checkCompareTo("John.3.14", "John.2.18", 1);    
        
        // check books
        checkCompareTo("John", "John", 0);
        checkCompareTo("Matt", "John", -1);
        checkCompareTo("John", "Matt", 1);
        checkCompareTo("Matt.5", "John.4", -1);
        checkCompareTo("John.4", "Matt.5", 1);
        checkCompareTo("Matt.4.18", "John.3.14", -1);
        checkCompareTo("John.3.14", "Matt.4.18", 1);
	}
	
	public void testEquality() {
	    checkEquality("John.3.16!a", "John.3.16!a", true);
	    checkEquality("John.3.16!a", "John.3.16!b", false);
	    checkEquality("John.3.16!b", "John.3.16!a", false);
	    
	    // check verses
        checkEquality("John.3.16", "John.3.16", true);
        checkEquality("John.3.14", "John.3.17", false);
        checkEquality("John.3.17", "John.3.14", false);
        
        // check chapters
        checkEquality("John.3", "John.3", true);
        checkEquality("John.3", "John.4", false);
        checkEquality("John.3", "John.2", false);
        checkEquality("John.3.18", "John.4.14", false);   
        checkEquality("John.3.14", "John.2.18", false);    
        
        // check books
        checkEquality("John", "John", true);
        checkEquality("Matt", "John", false);
        checkEquality("John", "Matt", false);
        checkEquality("Matt.5", "John.4", false);
        checkEquality("John.4", "Matt.5", false);
        checkEquality("Matt.4.18", "John.3.14", false);
        checkEquality("John.3.14", "Matt.4.18", false);
	}
	
	// TODO Test bad values, invalid book names, verses & chapters out of range, badly formed 
	//      references, etc
	
}
