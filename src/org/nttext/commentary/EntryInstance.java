/**
 * 
 */
package org.nttext.commentary;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import openscriptures.ref.Passage;

/**
 * Represents a single instance in the commentary.
 * 
 * @author Neal Audenaert
 */
public class EntryInstance {
    
    //======================================================================================
    // MEMBER VARIABLES
    //======================================================================================
    
    private Long id;
	private Passage ref;
	private String overview;
	
	// TODO need to supply a comparator
	private Set<VariationUnit> variants = new HashSet<VariationUnit>();
	
	//======================================================================================
	// Constructors
	//======================================================================================
	
	protected EntryInstance() {
	    
	}
	
	public EntryInstance(Long id, Passage ref, String overview, Date created, Date modified) {
	    this.id = id;
	    this.ref = ref;
	    this.overview = overview;
	}
	
	/**
	 * Instantiates a new <tt>EntryInstance</tt> from the provided reference. An instance 
	 * must refer to exactly one scripture reference. 
	 * 
	 * @param ref The scripture reference this instance refers to.  
	 */
	public EntryInstance(Passage ref) {
		this.ref = ref;
	}
	
	//======================================================================================
    // ACCESSORS AND MUTATORS
    //======================================================================================
    
	/** Returns the unique identifier for this instance. */
	public Long getId() {
	    return id;
	}
	
	public void setId(long id) {
	    this.id = id;
	}
	
	/** Returns the scripture passage this instance refers to. */
	public Passage getPassage() {
		return this.ref;
	}
	
	public void setReference(Passage ref) {
	    this.ref = ref;
	}
	
	/** Returns the overview text associated with this instance. This is expected to be
	 *  plain UTF-8 text. */
	public String getOverview() {
	    return this.overview;
	}
	
	/**
	 * Sets the overview text for this instance.
	 * 
	 * @param text The text to set.
	 */
	public void setOverview(String text) {
	    this.overview = text;
	}
	

	/**
	 * Adds a <tt>VariationUnit</tt> (VU) to this <tt>EntryInstance</tt>. The supplied VU
	 * must refer to the same passage as this instance or to a sub-passage of this instance.
	 * 
	 * @param vu The VU to add
	 * @return <tt>true</tt> if the VU was added
	 */
	public boolean addVariationUnit(VariationUnit vu) throws InvaliedVariantUnitException {
	    if (!vu.getPassage().isSubPassageOf(ref)) {
	        throw new InvaliedVariantUnitException(
	                "Could not add VU (" + vu.getId() + "): Supplied VU does not refer to " +
	                "a sub-passage of this instance. Found " + vu.getPassage() +  
	                " but expected " + ref);
	    }
	    
	    return this.variants.add(vu);
	}

	/**
	 * Removes the supplied variation unit (VU).
	 * @param vu The VU to remove
	 * 
	 * @return <tt>true</tt> if this instance contained the provided VU. 
	 */
	public boolean removeVariationUnit(VariationUnit vu) {
	    return this.variants.remove(vu);
	}
	
	/**
	 * Returns an unmodifiable list of the variation units in this instance.
	 * 
	 * @return
	 */
	// TODO more detail here!!
	public Set<VariationUnit> getVariationUnits() {
	    return Collections.unmodifiableSet(this.variants);
	}
	
	/**
	 * Sets the variation units associated with this instance. This is used by the 
	 * persistence layer. 
	 * 
	 * @param variationUnits The variation units associated with this instance.
	 */
    public void setVariationUnits(Set<VariationUnit> variationUnits) {
	    // TODO use a comparator
	    this.variants = new HashSet<VariationUnit>();
	    this.variants.addAll(variationUnits);
	    
	}
	
	public boolean isModifiable() {
	    // This is business logic and should be migrated to a separate class
	    // Entries should be modifiable only if they have not been published, and are not
	    // pending review. These states are not currently represented
	    return true;
	}

	//===================================================================================
    // EXCEPTION CLASS
    //===================================================================================
    
	@SuppressWarnings("serial")
    public static class NotModifiableException extends RuntimeException {
	    
	}
	
	@SuppressWarnings("serial")
    public static class InvaliedVariantUnitException extends Exception {
	    InvaliedVariantUnitException(String msg) {
	        super(msg);
	    }
	}
}
