/**
 * 
 */
package org.idch.bible.ref;

import org.apache.commons.lang.StringUtils;
import org.idch.util.LogService;

/**
 * @author Neal Audenaert
 */
public class VerseRange extends Passage {
	// TODO implements Comparable<VerseRange>
	//      add in overlap tests, add in merge operation
	
	private VerseRef start;
	private VerseRef end;

	public VerseRange(VerseRef start, VerseRef end) {
		super(start.getBookOrder());
		this.start = start;
		this.end = end;
	}
	
	public VerseRange(String ref) {
		this(BookOrder.KJV, ref);
	}
	
	public VerseRange(BookOrder order, String ref) {
		super(order);
		
		LogService.logDebug("Parsing verse range: " + ref, LOGGER);
		
		String TWO_VS_REQUIRED = "A verse range must have two verse " +
				"references separated by exactly one '-'.";
		
		String[] references = ref.split("-");
		if (references.length != 2) {
			throw new InvalidReferenceException(TWO_VS_REQUIRED, ref);
		}
		
		String a = StringUtils.trimToNull(references[0]);
		String b = StringUtils.trimToEmpty(references[1]);
		
		if (a == null || b == null)
			throw new InvalidReferenceException(TWO_VS_REQUIRED, ref);
		this.start = new VerseRef(order, StringUtils.trimToEmpty(references[0]));
		this.end = new VerseRef(start, StringUtils.trimToEmpty(references[1]));
		
		if (this.start.compareTo(this.end) > 0) { 
			throw new InvalidReferenceException("The supplied ending verse is before the " +
					"starting verse.", ref);
		}
	}
	
	public VerseRef getFirst() {
		return this.start;
	}
	
	public VerseRef getLast() {
		return this.end;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.start.toString()).append("-");
		
		if (start.getBookIndex() != end.getBookIndex()) {
			sb.append(end.toString());
		} else {
			Integer ch = end.getChapter();
			Integer vs = end.getVerse();
			String ext = end.getExtension();
			
			if (start.getChapter() != ch) {
				sb.append(ch);
				
				if (vs != null) {
					sb.append(".").append(vs);
					if (ext != null)
						sb.append("!").append(ext);
				}
			} else if (start.getVerse() != vs) {
				sb.append(vs);
				if (ext != null)
					sb.append("!").append(ext);
			} else if (start.getExtension() != ext) {
				sb.append(vs);
			} else {
				// TODO handle error.
			}
		}
		
		return sb.toString();
	}
}
