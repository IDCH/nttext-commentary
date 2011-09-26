/**
 * 
 */
package openscriptures.ref;

/**
 * Represents a selection of Bible verses. This may be a single verse, a . 
 * @author Neal Audenaert
 */
public abstract class Passage implements Comparable<Passage> {
	protected static final String LOGGER = Passage.class.getName();
	
	protected BookOrder order = BookOrder.KJV;
	
	protected Passage(BookOrder order) {
		this.order = order;
	}
	
	public abstract VerseRef getFirst();
	
	public abstract VerseRef getLast();
	
//	public abstract boolean contains(VerseRef ref);
	

	public BookOrder getBookOrder() {
		return this.order;
	}
	
	private int nullSafeCompareTo(VerseRef a, VerseRef b) {
	    if ((a != null) && (b != null)) {
            return a.compareTo(b);
        } else {
            return (a == b) ? 0 
                    : (a == null) ? -1 : 1;
        }
	}
	
	public int compareTo(Passage passage) {
		int result = nullSafeCompareTo(this.getFirst(), passage.getFirst());
		return (result != 0) ? result 
                : nullSafeCompareTo(this.getLast(), passage.getLast());
	}
	
	public boolean equals(Object o) {
	    return this.compareTo((Passage)o) == 0;
	}
}
