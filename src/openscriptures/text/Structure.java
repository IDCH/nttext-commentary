/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Marks an analytical structure (such as a verse, paragraph, personal name, etc) as a 
 * range of tokens within a work and stores attributes, paratextual content and 
 * hierarchical relationships with other structures for that strcuture. This is the primary 
 * mechanism for implementing stand-off markup within a <tt>Work</tt> in the OpenScriptures 
 * model. 
 * 
 * @author Neal Audenaert
 */
public interface Structure extends TokenSequence {
    // TODO support sub-token level structures (char offsets).
    
	/** A globally unique identifier for this structure. */
	public UUID getUUID();
	
	/**
	 * The specific work that this structure is associate with. Note that it
	 * may be possible to interpret a structure in the context of many different works, 
	 * but they must be defined in the context of at least one specific work instance. Since
	 * different import strategies may result in  
	 */
	public Work getWork();
	
	//========================================================================================
	// ACCESSORS
	//========================================================================================
	
	/** 
	 * A locally unique identifier for this structure. This is (optionally) used to 
     * provide access to domain specific identification schemes for structures, especially 
     * those that may need to be referenced across different texts (e.g., OSIS ids for 
     * books, chapters and verses).
     * 
	 * @return A locally unique identifier for this structure.
	 */
	public String getId(); 
    
	/**
	 * The name of this element. This corresponds to the name of the element tag in an OSIS
	 * (or other XML scheme). It must follow standard XML element naming restrictions.
	 */
	public String getName();
	
	/**
     * The first token of the content range identified by this structure.
     * 
     * @return
     */
	public Token getStartToken();
	
	/**
	 * The end token of the content range identified by this structure or <tt>null</tt> 
	 * if this structure does not have any content.
	 * 
	 * @return
	 */
	public Token getEndToken();
	
	//========================================================================================
    // MUTATORS
    //========================================================================================
	
	/**
     * Sets the locally unique ID for this structure.
     * 
     * @param value The id value
     *  
     * @throws UnsupportedOperationException
     * TODO throw something on name conflict 
     */
    public void setId(String value) throws UnsupportedOperationException;
    
    /**
     * 
     * @param value
     * @throws UnsupportedOperationException
     */
    public void setName(String value) throws UnsupportedOperationException;
    
	/**
	 * Sets the start token for this structure.
	 * 
	 * @param token The token to set
	 * 
	 * @throws UnsupportedOperationException If this operation is not implemented for a 
	 *         particular structure (e.g. for a structure accessed via a REST API that 
	 *         doesn't support updates).
	 * @throws InvalidTokenException If the supplied token is invalid. This might be because
	 *         the token's work does not match this structure's work or because the token 
	 *         does not occur before the end token.
	 *         
	 */
	public void setStartToken(Token token) 
	throws UnsupportedOperationException, InvalidTokenException;
	
	/**
     * Sets the end token for this structure.
     * 
     * @param token The token to set
     * 
     * @throws UnsupportedOperationException If this operation is not implemented for a 
     *         particular structure (e.g. for a structure accessed via a REST API that 
     *         doesn't support updates).
     * @throws InvalidTokenException If the supplied token is invalid. This might be because
     *         the token's work does not match this structure's work or because the token 
     *         does not occur after the start token.
     *         
     */
    public void setEndToken(Token token) 
    throws UnsupportedOperationException, InvalidTokenException;
    
    /**
     * Sets the start and end tokens for this structure.
     * 
     * @param start The start token to set
     * @param end The end token to set
     * 
     * @throws UnsupportedOperationException If this operation is not implemented for a 
     *         particular structure (e.g. for a structure accessed via a REST API that 
     *         doesn't support updates).
     * @throws InvalidTokenException If the supplied token is invalid. This might be because
     *         the token's work does not match this structure's work or because the start 
     *         token does not occur before the end token.
     *         
     */
    public void setTokens(Token start, Token end) 
    throws UnsupportedOperationException, InvalidTokenException;
	
	
//========================================================================================
// METHODS FOR REPRESENTING ATTRIBUTES, CONTENT, AND HIERARCHICAL STRUCTURES
//========================================================================================
		
    public Set<String> listAttributes();
    
	public String getAttribute(String name);
	
	public String setAttribute(String name, String value);
	
	public String getContent();
	
	public void setContent(String value) throws UnsupportedOperationException;
	
	/**
	 * Retrieves the parent of this structure. This along with {@see #getChildren()} 
	 * allows for the hierarchical nesting of structures. While the structures within
	 * a text are not strictly or exclusively hierarcical, hierarchies do represent an 
	 * important set of relationships between structures. For example, verses are not 
	 * simply structures that happen to be found only within chapters, verses are 
	 * specifically a sub-division
	 *   
	 * @return
	 */
	public Structure getParent();

	public List<Structure> listChildren();
	
	public String getPerspective();
	
}
