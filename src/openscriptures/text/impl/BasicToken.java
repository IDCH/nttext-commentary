/**
 * 
 */
package openscriptures.text.impl;

import java.util.UUID;

import openscriptures.text.Token;
import openscriptures.text.Work;

import org.apache.commons.lang.StringUtils;

/**
 * An atomic unit of text, such as a word, punctuation mark, or whitespace line break. 
 * Corresponds to OSIS w elements.
 * 
 * What to do about tokens that don't appear in a verse? i.e. when an osisID isn't fully 
 * specified or doesn't exist at all? For example, introductory material or titles? We could 
 * just use the best available osisID, e.g. it could be as basic as just bookName or 
 * bookName.chapterNumber.
 * 
 * If a hash is a duplicate with a previous hash, should the subsequent word merely increase 
 * the look-behind for the n-gram? This way, the subsequent change won't ripple. But if we 
 * change a word in the middle of a verse, and that ID conflicts with a subsequent one, do we 
 * really want to change the subsequent ID alone? It wasn't even the token that changed in the 
 * first place.
 * 
 * @author Neal Audenaert
 */
public class BasicToken implements Token {
	
	private UUID uuid;
	
	private Work work;
	private Type type;
	private String value;
	private long position;
	
	/**
	 * 
	 * @param work
	 * @param position
	 * @param text
	 */
	protected BasicToken(Work work, int position, String text) {
		// determine what type this text is
		if (StringUtils.isBlank(text)) {
			this.type = Type.WHITESPACE;
		} else if (text.matches("^\\w*$")) {
			this.type = Type.WORD;
		} else { 
			this.type = Type.PUNCTUATION;
		}
		
		this.uuid = UUID.randomUUID();
		this.position = position;
		this.value = text;
		this.work = work;
	}
	
	public Work getWork() {
	    return work;
	}

	public UUID getUUID() {
	    return this.uuid;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Token token) {
        if (this.position < token.getPosition()) return -1;
        else if (this.position > token.getPosition()) return 1;
        else return 0;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Token#getPosition()
     */
    @Override
    public long getPosition() { 
        return this.position; 
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Token#getType()
     */
    @Override
    public Type getType() { 
        return this.type; 
    }

    /** Returns the textual value of this token. */
    @Override
    public String getText() {
        return this.value;
    }
}
