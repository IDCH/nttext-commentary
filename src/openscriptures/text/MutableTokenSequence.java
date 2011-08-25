/**
 * 
 */
package openscriptures.text;

import openscriptures.text.impl.BasicStructure;
import openscriptures.text.impl.BasicToken;


/**
 * @author Neal Audenaert
 */
public interface MutableTokenSequence extends TokenSequence {
    
    public Token addToken(String token);
    
   
	
//	/* (non-Javadoc)
//	 * @see java.util.List#add(java.lang.Object)
//	 */
//	public boolean append(Token e);
//	
//	/* (non-Javadoc)
//	 * @see java.util.List#addAll(java.util.Collection)
//	 */
//	public boolean append(TokenSequence c);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#add(int, java.lang.Object)
//	 */
//	public void insert(int index, Token element);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#addAll(int, java.util.Collection)
//	 */
//	public boolean insert(int index, TokenSequence c);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#clear()
//	 */
//	public void clear();
//	
//	/* (non-Javadoc)
//	 * @see java.util.List#remove(java.lang.Object)
//	 */
//	public boolean remove(Token o);
//	
//	/* (non-Javadoc)
//	 * @see java.util.List#remove(int)
//	 */
//	public Token remove(int index);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#removeAll(java.util.Collection)
//	 */
//	public boolean remove(TokenSequence tokens);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#retainAll(java.util.Collection)
//	 */
//	public boolean retainAll(TokenSequence tokens);
//
//	/* (non-Javadoc)
//	 * @see java.util.List#set(int, java.lang.Object)
//	 */
//	public Token set(int index, Token element);

}
