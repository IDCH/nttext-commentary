/**
 * 
 */
package org.nttext.commentary;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;
import openscriptures.text.Structure;

/**
 * @author Neal Audenaert
 */
public class VariationUnit {
	
    //===================================================================================
    // MEMBER VARIABLES
    //===================================================================================
    
	private UUID id;
	private String commentary;
	private List<VariantReading> readings;

	private Passage ref;
	
	private Map<String, VUReference> references = new HashMap<String, VUReference>();
	
	
	//===================================================================================
    // CONSTRUCTORS
    //===================================================================================
    
	public VariationUnit() {
	    
	}
	
	public VariationUnit(String ref) {
	    this.ref = new VerseRange(ref);
	}
	
	//===================================================================================
    // ACCESSORS & MUTATORS
    //===================================================================================
    
	/** Returns a unique identifier for this variation unit. */
 	public String getId() {
	    return id.toString();
	}
	
 	/** Returns the passage this variation unit is found in. */
	public Passage getPassage() {
	    return this.ref;
	}
		

	/** Retrieves the commentary describing this variation unit. */
	public String getCommentary() {
	    return commentary;
	}
	
	/**
	 * Sets the commentary associated with this variation unit. This is expected to be
	 * plain text (UTF-8) or lightly marked HTML. 
	 * 
	 * @param commentary The commentary to set. 
	 */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    
    // REFEREMCE METHODS
    //=============================================================
    
    /** 
     * Lists all NT edition for which a reference to this VU has been defined.
     * @return the names of all of the NT editions for which a reference to 
     *      this VU has been defined.
     */
    public Set<String> listReferences() {
        return Collections.unmodifiableSet(references.keySet());
    }
    
    /**
     * Indicates whether this VU has a reference to the supplied variation unit.
     * @param edition The edition to check.
     * 
     * @return <tt>true</tt> if a reference to the supplied edition has been defined 
     *      for this VU.
     */
    public boolean hasReference(String edition) {
        return this.references.containsKey(edition);
    }

    /**
     * Returns a reference to this variation for the specified edition of the New Testament.
     * 
     * @param edition The short name for the edition to retrieve (e.g., SBLGNT, KJV, HCSB).
     * @return An object describing this reference.
     */
    public VUReference getReference(String edition) {
        return this.references.get(edition);
    }
    
	/**
	 * Adds a reference for this variation unit for a New Testament edition. 
	 * 
	 * @param ref The structure that defines the text span in an edition of the 
	 *     NT corresponding to this VU. 
	 * @throws InvalidReferenceException if this VU already has a reference to the 
	 *     corresponding edition.
	 */
	public VUReference addReference(Structure structure) {
	    VUReference r = new VUReference(this, structure);
	    String edition = r.getEdition();

	    synchronized (references) {
	        if (this.references.containsKey(edition)) {
	            throw new InvalidReferenceException("Could not add reference: " +
	            		"A reference to this edition (" + edition + ") already exists.");
	        } else { 
	            this.references.put(edition, r);
	        }
	    }
	    
	    return r;
	}
	
	/**
	 * Removes a reference to an NT edition.
	 * @param edition The edition that should be removed.
	 * @return the removed reference.
	 */
	public VUReference deleteReference(String edition) {
	    return this.references.remove(edition);
	}
	
	// READINGS METHODS
    //=============================================================
    
	/**
	 * Returns a list of all readings for this variation unit. 
	 * @return a list of all readings for this variation unit.
	 */
	public List<VariantReading> getReadings() {
	    return Collections.unmodifiableList(this.readings);
	}

	/**
	 * Creates a new reading for this variation unit.
	 * @return the newly created variant reading.
	 */
	public VariantReading createReading() {
	    // TODO move to repository method (probably)
	    VariantReading rdg;
	    synchronized(readings) {
	        rdg = new VariantReading(this);
	        readings.add(rdg);
	    }
	        
	    return rdg;
	}
	
	/**
	 * Deletes the indicated variant reading.
	 * 
	 * @param rdg The reading to delete.
	 * @return <tt>true</tt> if the supplied reading was a member of this VU. 
	 */
	public boolean deleteReading(VariantReading rdg) {
	    // TODO move to repository method.
	    return readings.remove(rdg);
	}
	
	// TODO Support to move a reading
	
	//=====================================================================================
	// INNER CLASSES
    //=====================================================================================
	
	/**
	 * This stands in for a <tt>Work</tt> object for now. 
	 */
	public static class TextProxy {
	    private String passageText;
        
        private String language;
        private String lgCode;
        private String edition;
        
        public TextProxy(String text, String language, String lgCode, String edition) {
            this.passageText = text;
            this.language = language;
            this.lgCode = lgCode;
            this.edition = edition;
        }
        
        public String resolve(Passage ref) {
            return passageText;
        }
        
        public String getLanguage() {
            return language;
        }
        
        public String getLgCode() {
            return this.lgCode;
        }
        
        public String getEdition() {
            return this.edition;
        }
	}
	
	//=====================================================================================
    // INVALID REFERENCE EXCEPTION
    //=====================================================================================
	public static class InvalidReferenceException extends RuntimeException {
	    InvalidReferenceException(String msg) {
	        super(msg);
	    }
	}
    
	
}
