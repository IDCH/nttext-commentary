/**
 * 
 */
package org.idch.bibleref;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Neal Audenaert
 */
public class VerseSet extends Passage {
	
	private SortedSet<Passage> passages = new TreeSet<Passage>();
	
	public VerseSet(String ref) {
		this(BookOrder.KJV, ref);
	}
	
	public VerseSet(BookOrder order, String ref) {
		super(order);
		
		Passage p = null;
		String[] references = ref.split("[,;]");
		for (String reference : references) {
			if (reference.indexOf('-') > 0) {
				p = new VerseRange(reference);
			} else 
				p = new VerseRef(reference);
			
			this.passages.add(p);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.idch.bibleref.Passage#getFirst()
	 */
	@Override
	public VerseRef getFirst() {
		return passages.first().getFirst();
	}

	/* (non-Javadoc)
	 * @see org.idch.bibleref.Passage#getLast()
	 */
	@Override
	public VerseRef getLast() {
		return passages.last().getLast();
	}

}
