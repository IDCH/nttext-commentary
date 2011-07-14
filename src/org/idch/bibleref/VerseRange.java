/**
 * 
 */
package org.idch.bibleref;

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
		
		String[] references = ref.split("-");
		if (references.length != 2) {
			throw new InvalidReferenceException("A verse range must have two verse " +
					"references separated by exactly one '-'.", ref);
		}
		
		this.start = new VerseRef(order, references[0]);
		this.end = new VerseRef(start, references[1]);
		
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
}
