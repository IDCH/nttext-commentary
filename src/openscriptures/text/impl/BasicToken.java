/**
 * 
 */
package openscriptures.text.impl;

import java.util.UUID;

import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * An atomic unit of text, such as a word, punctuation mark, or whitespace. 
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

    public static final String UNICODE_WORDSCHARS = 
        "[\\p{L}\\p{Sk}\\d]";
    public static final String UNICODE_PUNCTUATION = 
        "[\\p{P}]";
    public static final String UNICODE_WHITESPACE = 
        "[\\p{Z}\\u000a\\u0009]";
    public static final String UNICODE_UNKNOWN = 
        "[^\\p{L}\\p{Sk}\\d\\p{P}\\p{Z}]";
    
    public static final String TOKENIZATION_PATTERN =
        UNICODE_WORDSCHARS + "+|" +                 // Unicode letters
        UNICODE_PUNCTUATION + "+|" +                // Unicode punctuation
        UNICODE_WHITESPACE + "+|" +                 // Java whitespace (becase I can't figure out Unicode whitespace)
        UNICODE_UNKNOWN + "+";                      // everything else (typically ignored).
    
    /**
     * 
     * @param string
     * @return
     */
    public static Token.Type classify(String string) {
        Token.Type type = null;
        if (string.matches(UNICODE_WORDSCHARS + "+")) {
            type = Token.Type.WORD;
        } else if (string.matches(UNICODE_PUNCTUATION + "+")) {
            type = Token.Type.PUNCTUATION;
        } else if (string.matches(UNICODE_WHITESPACE + "+")) {
            type = Token.Type.WHITESPACE;
        }
        
        return type;
    }
    
	private UUID uuid;
	
	private Work work;
	private Type type;
	private String value;
	private int position;
	
	/**
	 * 
	 * @param work
	 * @param position
	 * @param text
	 */
	protected BasicToken(Work work, int position, String text) {
		this.uuid = UUID.randomUUID();
		
		this.work = work;
		this.value = text;
		this.type = classify(text);
		this.position = position;
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
    public int getPosition() { 
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
    
    public String toString() {
        return this.value;
    }
}
