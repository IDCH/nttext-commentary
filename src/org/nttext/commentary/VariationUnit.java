/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.idch.bible.ref.Passage;


/**
 * @author Neal Audenaert
 */
public class VariationUnit {
	
    //===================================================================================
    // MEMBER VARIABLES
    //===================================================================================
    
	private Long id;
	private String commentary;
	private Passage ref;
	
	private List<VariantReading> readings = new ArrayList<VariantReading>();
	
	//===================================================================================
    // CONSTRUCTORS
    //===================================================================================
    
	public VariationUnit() {
	    
	}
	
	public VariationUnit(Long id, Passage ref, String commentary, Date created, Date modified) {
	    this.id = id;
	    this.ref = ref;
	    this.commentary = commentary;
    }
	
	public VariationUnit(Passage ref) {
	    this.ref = ref;
	}
	
	//===================================================================================
    // ACCESSORS & MUTATORS
    //===================================================================================
    
	/** Returns a unique identifier for this variation unit. */
 	public Long getId() { return id; }
	
	/** Used by the persistence layer to set the unique id of this VU. */ 
	public void setId(long id) { this.id = id; }
	
 	/** Returns the passage this variation unit is found in. */
	public Passage getPassage() {
	    return this.ref;
	}
	
	public void setPassage(Passage ref) { this.ref = ref; }
		

	/** Retrieves the commentary describing this variation unit. */
	public String getCommentary() {  return commentary; }
	
	/**
	 * Sets the commentary associated with this variation unit. This is expected to be
	 * plain text (UTF-8) or lightly marked HTML. 
	 * 
	 * @param commentary The commentary to set. 
	 */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
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
     * @param rdgs
     */
    public void setReadings(List<VariantReading> rdgs) {
        this.readings = rdgs;
    }
	
	public void addReading(VariantReading rdg) {
	    this.readings.add(rdg);
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
        private static final long serialVersionUID = 1183679262481129955L;

        InvalidReferenceException(String msg) {
	        super(msg);
	    }
	}

    
	
}
