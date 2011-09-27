/**
 * 
 */
package openscriptures.text;

/**
 * @author Neal Audenaert
 */
public interface Token extends Comparable<Token> {

	public static enum Type {
		WORD, PUNCTUATION, WHITESPACE
	}
	
	/** 
	 * Returns the work for this token. All tokens exist only in the context of a work 
	 * and are uniquely specified by the combination of this work and their position with 
	 * the work. 
	 */
	public Work getWork();
	
	public int getPosition();

	/**
	 * Returns the type of this Token. One of, <tt>WORD</tt>, <tt>PUNCTUATION</tt>,
	 * <tt>WHITESPACE</tt>
	 */
	public Type getType();
	
	public String getText();
	
	public boolean hasNext();
    
    public Token next();
    
    public Token next(boolean ignoreWhitespace);
    
    public boolean hasPrev();
    
    public Token prev();
    
    public Token prev(boolean ignoreWhitespace);
	
}
