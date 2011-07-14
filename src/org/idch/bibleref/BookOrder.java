/**
 * 
 */
package org.idch.bibleref;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a particularly ordering of the books of the Bible. For now, this is a 
 * temporary holding class until I do something with it that makes sense. 
 * 
 * Note the work done by John Hunt at https://github.com/openscriptures/BibleOrgSys
 * 
 * @author Neal Audenaert
 */
public class BookOrder {
	
	public static final BookOrder KJV = new BookOrder();

	private String books[] = null;
	
	/**
	 * Temporary constructor that builds a book order list using the KJV sequence.
	 */
	public BookOrder() {
		String[] books = {
				// OLD TESTAMENT
				"Gen", "Exod", "Lev", "Num", "Deut", "Josh", "Judg", "Ruth", "1Sam", "2Sam", 
				"1Kgs", "2Kgs", "1Chr", "2Chr", "Ezra", "Neh", "Esth", "Job", "Ps", "Prov", "Eccl", 
				"Song", "Isa", "Jer", "Lam", "Ezek", "Dan", "Hos", "Joel", "Amos", "Obad", "Jonah", 
				"Mic", "Nah", "Hab", "Zeph", "Hag", "Zech", "Mal",
				
				// NEW TESTAMENT
				"Matt", "Mark", "Luke", "John", "Acts", "Rom", "1Cor", "2Cor", "Gal", "Eph", "Phil", 
				"Col", "1Thess", "2Thess", "1Tim", "2Tim", "Titus", "Phlm", "Heb", "Jas", "1Pet", 
				"2Pet", "1John", "2John", "3John", "Jude", "Rev",
				
				// DEUTEROCANONICAL
				"Bar", "AddDan", "PrAzar", "Bel", "SgThree", "Sus", "1Esd", "2Esd", "AddEsth", 
				"EpJer", "Jdt", "1Macc", "2Macc", "3Macc", "4Macc", "PrMan", "Sir", "Tob", "Wis"
			};
		
		this.books = books;
	}
	
	public BookOrder(File config) {
		
	}
	
	/** 
	 * Returns the index of the supplied book name.
	 * 
	 * @param name The book name to look for.
	 * @return The index of the indicated book or -1 if there is no such book.
	 */
	public int indexOf(String name) {
		for (int i = 0; i < books.length; i++) {
			if (books[i].equalsIgnoreCase(name)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public String getName(int ix) {
		assert (ix < books.length) && ix >= 0;
		
		if ((ix >= books.length) && (ix < 0)) {
			// In case we want to offer more information later. In contrast to returning 
			// null, this enforces a fail fast policy to support easier error finding.
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return this.books[ix];
	}
	
	public boolean contains(String name) {
		// TODO cache this value for performance 
		
		int ix = indexOf(name);
		return ix >= 0 && (ix < this.books.length);
	}
	
	public int size() {
		return this.books.length;
	}
	
	public List<String> listBooks() {
		return Collections.unmodifiableList(Arrays.asList(this.books));
	}
	
}
