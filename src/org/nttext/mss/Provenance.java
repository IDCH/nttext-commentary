/**
 * 
 */
package org.nttext.mss;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.nttext.util.Institution;
import org.nttext.util.Location;

/**
 * @author Neal Audenaert
 */

public class Provenance {
	// Provenance & ownership history, & current location
	
    private Manuscript ms;
	
    /** A narrative description of the provenance of this manuscript. */
    private String narrative = "";
	
	private Set<ProvenanceDetails> details;
	
	//=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================
	
	/**
	 * 
	 */
	Provenance() {
	    
	}
    
	/**
	 * 
	 * @param ms
	 */
	public Provenance(Manuscript ms) {
	    this.ms = ms;
	    narrative = "";
	    details = new HashSet<ProvenanceDetails>();
	}
	
	//=======================================================================================
    // ACCESSORS & MUTATORS
    //=======================================================================================
    
	/**
	 * 
	 * @return
	 */
	public String getNarrative() {
	    return narrative;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setNarrative(String value) {
	    this.narrative = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public Manuscript getManuscript() {
	    return ms;
	}
	
	/**
	 * 
	 * @param ms
	 */
	void setManuscript(Manuscript ms) {
	    this.ms = ms;
	}
	
	public void addEpisode() {
	    
	}
	
	
	// TODO add references to bibliographic support for the narrative
	
	//======================================================================================
	// INNER CLASS 
	//======================================================================================
	/**
	 * Represents the details of a single stage in the provenance of a MS.
	 * @author Neal Audenaert
	 */
	public static class ProvenanceDetails {
		/** The individual or institution that held the MS during this time period. */ 
	    Institution holder = null;		// TODO change to a class so that we can enforce stronger 
									//      name authority constraints
		
	    /** The institution (or individual) who held the object during this period. */
	    Location location;
	    
		/** The start date for this period. */
		Date beginning = null;		// TODO use fuzzy dates
		
		/** The ending date for this period. */
		Date ending    = null;
		
		/** A description of the disposition of the object during this period. */ 
		String narrative;
		
		public ProvenanceDetails() {
		    
		}
		
		public ProvenanceDetails(Institution holder) {
		    
		}
		
		public ProvenanceDetails(Institution holder, Date start, Date end) {
            
        }
		
		// TODO add references to bibliographic support for this history
	}
}
