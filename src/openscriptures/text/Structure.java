/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.UUID;

/**
 * @author Neal Audenaert
 */
public interface Structure extends TokenSequence {

	/** A globally unique identifier for this structure. */
	public UUID getUUID();
	
	/** 
	 * Returns the OSIS id associated with this structure. 
	 * 
	 * TODO Assuming that we want this to transcend an OSIS representation of 
	 * 		structures, this method is probably not appropriate.
	 * @return
	 */
	public String getOsisId(); 

	/**
	 * The name of this element. This corresponds to the name of the element tag in an OSIS
	 * (or other XML scheme). It must follow standard XML element naming restrictions.
	 */
	public String getStructureName();
	
	/**
	 * This returns the specific work that this structure is associate with. Note that it
	 * may be possible to interpret a structure in the context of many different works, 
	 * but they must be defined in the context of at least one specific work instance. Since
	 * different import strategies may result in  
	 */
	public Work getWork();
	
	public Token getStartToken();
	
	public Token getEndToken();
	
	public TokenSequence getTokens();
	
	
//========================================================================================
// METHODS FOR REPRESENTING ATTRIBUTES
//========================================================================================
		
	public StructureAttribute getAttribute(String name);
	
	public List<String> listAttributes();
	
}
