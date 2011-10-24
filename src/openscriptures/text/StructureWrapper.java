/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Base class for implementing domain specific utility methods on top of structure objects. 
 * This class wraps an existing structure object and provides pass through methods for
 * functionality defined by the <tt>Structure</tt> interface. It is intended to be sub-classed
 * to provide custom accessor and mutator methods to allow more convenient interaction with 
 * domain specific structures like the book/chapter/verse hierarchy, textual variants or 
 * notes. <tt>StructureWrapper</tt> sub-classes are expected not to maintain their own state
 * independent of the low-level structure that they wrap.
 * 
 * @author Neal Audenaert
 */
public abstract class StructureWrapper extends Structure {
	// TODO (maybe) connect to repository so that we can write changes through.
    // TODO This is dangerous. We're inheriting structure, but there is no requirement
    //      that we override the interface methods. Inconsistent Changes Some may default 
    
	private final Structure me;
	
//========================================================================================
// CONSTRUCTORS
//========================================================================================
	
	/**
	 * 
	 */
	protected StructureWrapper(Structure structure) {
	    if (!accepts(structure)) {
            throw new InvalidStructureException(structure);
        }
	    
		me = structure;
	}
	
	public Long getId() {
	    return me.getId();
	}
	
	public void setId(Long id) {
	    me.setId(id);
	}
	
	/** 
	 * Determines whether the supplied structure is an acceptable instance of this
	 * wrapper. This will be called as the first step in construction (before member variables
	 * have been initialized). If this returns false, the constructor will throw an exception.
	 *  
	 * @param sturcture The structure being used to create this wrapper.
	 * @return <tt>true</tt> if the supplied structure is an acceptable instance of this 
	 *     wrapper. 
	 */
	public abstract boolean accepts(Structure sturcture);
	
	public int getStart() {
        return me.getStart();
    }
     
    public int getEnd() {
        return me.getEnd();
    }
//========================================================================================
// ACCESSORS
//========================================================================================

	
	/** 
	 *  Returns the unique identifier for this <tt>Structure</tt> 
     *  @see openscriptures.text.Structure#getUUID()
     */
    @Override
    public UUID getUUID() {
        return me.getUUID();
    }
    
    /** 
     *  Returns the <tt>Work</tt> that this structure is found in. 
     *  @see openscriptures.text.Structure#getWorkUUID()
     */
    @Override
    public UUID getWorkUUID() {
        return me.getWorkUUID();
    }

    /** Return the name of this structure. This corresponds to an element name 
     *  in an XML document.
     * @see openscriptures.text.Structure#getName()
     */
    @Override
    public String getName() {
        return me.getName();
    }

    /** Returns the token at which this structure starts.
     * @see openscriptures.text.Structure#getStartToken()
     */
    @Override
    public Token getStartToken() {
        return me.getStartToken();
    }

    /* Returns the last token in this 
     * @see openscriptures.text.Structure#getEndToken()
     */
    @Override
    public Token getEndToken() {
        return me.getEndToken();
    }
    
    //========================================================================================
    // MUTATORS
    //========================================================================================
    
    /**
     * 
     * @param value
     * @throws UnsupportedOperationException
     */
    public void setName(String value) throws UnsupportedOperationException {
        me.setName(value);
    }
    
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
    throws UnsupportedOperationException, InvalidTokenException {
        me.setStartToken(token);
    }
    
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
    throws UnsupportedOperationException, InvalidTokenException {
        me.setEndToken(token);
    }
    
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
    throws UnsupportedOperationException, InvalidTokenException {
        me.setTokens(start, end);
    }
    
//========================================================================================
// METHODS FOR REPRESENTING ATTRIBUTES, CONTENT, AND HIERARCHICAL STRUCTURES
//========================================================================================
    /*
     * 
     */
    @Override
	public String getAttribute(String name) {
        return me.getAttribute(name);
	}

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#listAttributes()
     */
    @Override
    public Set<String> listAttributes() {
        return me.listAttributes();
    }
        
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
    @Override
    public Structure getParent() {
        return me.getParent();
    }

    @Override
    public List<Structure> listChildren() {
        return me.listChildren();
    }
    
    @Override
    public String getPerspective() {
        return me.getPerspective();
    }
    
    @Override
    public void setPerspective(String perspective) {
        me.setPerspective(perspective);
    }

    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public String setAttribute(String name, String value) {
        return me.setAttribute(name, value);
	}
    
    public static class InvalidStructureException extends RuntimeException {
        private static final long serialVersionUID = 6909912872812332032L;
        
        private Structure s;
        
        InvalidStructureException(Structure s) {
            super("Cannot create a WrappedStructure. The supplied structure instance (" +
            		s.getName() + ") is not acceptd.");
        }
        
        public Structure getStructureInstance() {
            return s;
        }
        
    }
}
