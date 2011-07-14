/**
 * 
 */
package org.idch.bibleref;

import org.apache.commons.lang.StringUtils;
import org.idch.util.LogService;

/**
 * Maintains a reference to a single verse or to a point within a verse. 
 *  
 * The OSIS specification treats reference identifiers as a segment of '<tt>.</tt>' separated 
 * identifiers of increasingly narrowing scope. This allows the OSIS scheme to represent
 * not only Bibles but a variety different types of works. As a result, however, the semantics 
 * of different segments are not well defined. <tt>VerseRef</tt>, in contrast, emphasizes 
 * access by traditional verse semantics (book of the Bible, chapter, verse, and extension). 
 * Thus, <tt>VerseRef</tt> provides a partial implementation of the OSIS specification 
 * designed to simplify the common task of working with scripture references.
 * 
 * TODO This needs to be brought up to speed to parse full blown OSIS identifiers.
 *      For some detail, see: http://groups.google.com/group/openscriptures/msg/4fb744efb27c1a41?pli=1
 *      
 *      The current implementation works on verses represented in the form of Matt.3.4, where
 *      the names are book names that can be resolved using the supplied BookOrder. Chapters
 *      and verses are assumed to be numeric.
 *      
 * TODO Implement support for grains (e.g., Gen.1.1@cp[8] or Gen.1.1@s[beginning]).
 *  
 * 
 * @author Neal Audenaert
 */
public class VerseRef implements Comparable<VerseRef> {
	private static final String LOGGER = VerseRef.class.getName();
	
	private BookOrder order = BookOrder.KJV;
	
	private Integer book = null;
	private Integer chapter = null;
	private Integer verse = null;
	
	/** The sub-verse level extension, for example, in <tt>Matt.2.10!a</tt>, the 
	 *  extension is <tt>a</tt>. 
	 */
	private String ext = null;
	
	
//======================================================================================
// CONSTUCTORS
//======================================================================================	
	public VerseRef(String ref) throws InvalidReferenceException {
		this(BookOrder.KJV, ref);
	}
	
	public VerseRef(BookOrder order, String ref) throws InvalidReferenceException {
		this.order = order;
		this.parseCanonicalReference(ref, false /* defualtToChapter */);
	}
	
	/**
	 * Used to resolve a partial reference within the context of another verse reference.
	 * If, for example, when parsing a verse range like <tt>Matt.3.3-4</tt>, the first 
	 * verse in the range parses to <tt>Matt.3.3</tt>, while the second verse, 
	 * <tt>4</tt> should be interpreted relative to the first <tt>Matt.3.4</tt>.
	 *  
	 * @param order
	 * @param context
	 * @param ref
	 */
	public VerseRef(VerseRef context, String ref) {
		this.order = context.getBookOrder();
		this.parseCanonicalReference(ref, !context.isVerseSpecified() /* defualtToChapter */);
		
		// We'll attempt to inherit book and verse references, 
		if (this.book == null) 
			this.book = context.getBookIndex();
		else return;				// short ciruit once a higher order field is specified
		
		if (this.chapter == null)
			this.chapter = context.getChapter();
		else return;
		
		// but not verse and extension for now
	}
	
	public VerseRef(BookOrder order, Integer book, Integer chapter, Integer vs, String ext) {
		this.order = order;
		this.book = book;
		this.chapter = chapter;
		this.verse = vs;
		this.ext = ext;
	}
	
	//======================================================================================
	// PARSING METHODS
	//======================================================================================
	private String parseGrain(String ref) {
		String remainder = ref, grain = null;
		int ix = ref.lastIndexOf('@');
		if (ix >= 0) {
			assert ix < ref.length() : "Bad Refernce Format: trailing '@'";
			
			grain = ref.substring(ix + 1);
			remainder = ref.substring(0, ix);
			LogService.logDebug("Extracted grain '" + grain + "' from referecne '" + ref + "'.", LOGGER);
		}
		
		return remainder;
	}
	
	private String parseExtension(String ref) {
		String remainder = ref;
		int ix = ref.lastIndexOf('!');
		if (ix >= 0) {
			assert ix < ref.length() : "Bad Refernce Format: trailing '!'";
			
			this.ext = ref.substring(ix + 1);
			remainder = ref.substring(0, ix);
			LogService.logDebug("Extracted extension '" + this.ext + "' from referecne '" + ref + "'.", LOGGER);
		}
		
		return remainder;
		
	}
	

	/**
	 * Parses a canonical verse reference. A canonical reference is one with a book name,
	 * chapter and verse, separated by periods Matt.3.2!a@w[3]
	 * 
	 * DESCRIBE THIS HERE
	 * 
	 * @param ref The string-valued verse reference to parse.
	 * @param defaultToChapter if only a single numeric value is provided, indicates whether
	 * 		that value should be interpreted as a chapter or as a verse.
	 * @throws InvalidReferenceException
	 */
	private void parseCanonicalReference(String ref, boolean defaultToChapter) 
	throws InvalidReferenceException {
		// NOTE Consider using RegEx based parsing. 
		if (StringUtils.isBlank(ref)) {
			throw new InvalidReferenceException("Blank reference", ref);
		}
		
		ref = this.parseGrain(ref);
		ref = this.parseExtension(ref);
		
		String[] segments = ref.split("\\.");
		int len = segments.length;
		
		if (len == 0) 
			return;		// No segments to process
		
		if (!StringUtils.isNumeric(segments[0])) {
			// first segment is a book identifier, process left to right
			this.book = order.indexOf(segments[0]);
			this.chapter = (len > 1) ? Integer.parseInt(segments[1]) : null;
			this.verse = (len > 2) ? Integer.parseInt(segments[2]) : null;
			
			if (len > 3) {
				LogService.logWarn("Bad verse reference (" + ref + "): trailing segments.", LOGGER);
			}
			
		} else {
			// first segment is not a book, process right to left
			if (len == 1) {		// just a verse
				if (defaultToChapter)
					this.chapter = Integer.parseInt(segments[0]);
				else
					this.verse = Integer.parseInt(segments[0]);
			} else {			// chapter.verse
				this.chapter = Integer.parseInt(segments[0]);
				this.verse = Integer.parseInt(segments[1]);
			}
			
			if (len > 2) {
				LogService.logWarn("Bad verse reference (" + ref + "): too many numeric " +
						"identifiers. Expected reference in the form of 'ch.vs'.", LOGGER);
			}
		}
		
		LogService.logDebug("Book Index: " + this.book, LOGGER);
		LogService.logDebug("Chapter:    " + this.chapter, LOGGER);
		LogService.logDebug("Verse:      " + this.verse, LOGGER);
		
	}
	
	//======================================================================================
	// GETTERS AND SETTERS
	//======================================================================================
	
	public BookOrder getBookOrder() {
		return this.order;
	}
	
	/**
	 * Returns the index of the book of the Bible relative to the <tt>BookOrder</tt> scheme 
	 * used by this reference.
	 *  
	 * @return the index of this book or <tt>null</tt> if no book is specified for this 
	 * 		reference
	 */
	public Integer getBookIndex() {
		return this.book;
	}
	
	
	/** 
	 * Returns the short or canonical name used to identify this book in the given 
	 * <tt>BookOrder</tt>. Note that this is not the  
	 * 
	 * @return
	 */
	public String getBookName() {
		return (this.book != null) ? order.getName(book) : null;
	}
	
	public Integer getChapter() {
		return this.chapter;
	}
	
	public Integer getVerse() {
		return this.verse;
	}
	
	public String getExtension() {
		return this.ext;
	}
	
	/**
	 * Indicates whether the book is specified for this reference.
	 * @return <tt>true</tt> if the book is specified.
	 */
	public boolean isBookSpecified() {
		return this.book != null;
	}
	
	/**
	 * Indicates whether the chapter is specified for this reference.
	 * @return <tt>true</tt> if the chapter is specified.
	 */
	public boolean isChapterSpecified() {
		return this.chapter != null;
	}
	
	/**
	 * Indicates whether the verse is specified for this reference.
	 * @return <tt>true</tt> if the verse is specified.
	 */
	public boolean isVerseSpecified() {
		return this.verse != null;
	}
	
	//======================================================================================
	// UTILITY METHODS
	//======================================================================================
	
	/**
	 * Checks to ensure that this object refers to a valid verse.
	 * @return
	 */
	public boolean isValid() {
		// TODO right now, this just checks to see if we know about the verse. It 
		//      needs to be extened to see if the chapter and verse are valid for the 
		//		give book according to the specified reference system. 
		
		// make sure that we have a valid book, given our reference system
		if ((book != null) || (book < 0)) {
			return false;
		}
		
		// TODO check to see if the chapter and verse are valid.
		return true;
	}

	/**
	 * Returns this verse reference as a canonically formatted string.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean hasContent = false;
		
		if (this.book != null) {
			hasContent = true;
			sb.append(order.getName(this.book));
		}
		
		if (this.chapter != null) {
			if (hasContent) sb.append(".");
			sb.append(this.chapter);
			hasContent = true;
		}
		
		if (this.verse != null) {
			if (hasContent) sb.append(".");
			sb.append(this.verse);
			hasContent = true;
		}
		
		if (this.ext != null)
			sb.append("!").append(this.ext);
		
		return sb.toString();
	}
	
	public int compareTo(VerseRef ref) {
		// TODO need to make sure that we're using the same book order.
		//      figure out how to order them if we aren't.
		// TODO possible null pointer errors
		
		assert (ref.book != null) && (book != null);
		int bk = book - ref.book;
		if (bk != 0) return bk;
		
		// compare chapters
		if ((ref.chapter != null) && (chapter != null)) {
			int ch = chapter - ref.chapter;
			if (ch != 0) return ch;
		} else {
			return (ref.chapter == chapter) ? 0 
					: (chapter == null) ? -1 : 1;
		}
		
		// compare verses
		if ((ref.verse != null) && (verse != null)) {
			int vs = verse - ref.verse;
			if (vs != 0) return vs;
		} else {
			return (ref.verse == verse) ? 0 
					: (verse == null) ? -1 : 1;
		}
		
		
		return ext.toLowerCase().compareTo(ref.ext.toLowerCase());
	}
	
}