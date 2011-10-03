/**
 * 
 */
package org.nttext.commentary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import openscriptures.ref.Passage;

/**
 * Represents a single entry in the commentary. . 
 * 
 * @author Neal Audenaert
 */
public class Entry {
	
    private UUID id;
	private Passage ref;
	private String overview;
	
	// TODO need to supply a comparator
	private SortedSet<VariationUnit> variants = new TreeSet<VariationUnit>();
	
	//======================================================================================
	// Constructors
	//======================================================================================
	
	/**
	 * Instantiates a new <tt>Entry</tt> from the provided reference. An entry must refer 
	 * to exactly one scripture reference. 
	 * 
	 * @param ref The scripture reference this entry refers to.  
	 */
	public Entry(Passage ref) {
		this.ref = ref;
	}
	
	//======================================================================================
    // ACCESSORS AND MUTATORS
    //======================================================================================
    
	/** Returns the unique identifier for this entry. */
	public String getId() {
	    return id.toString();
	}
	
	/** Returns the scripture passage this entry refers to. */
	public Passage getReference() {
		return this.ref;
	}
	
	/**
	 * Sets the overview text for this entry.
	 * 
	 * @param text The text to set.
	 */
	public void setOverview(String text) {
	    this.overview = text;
	}
	
	/** Returns the overview text associated with this entry. This is expected to be
	 *  plain UTF-8 text. */
	public String getOverview() {
	    return this.overview;
	}

	/**
	 * Adds a <tt>VariationUnit</tt> (VU) to this <tt>Entry</tt>. The supplied VU
	 * must refer to the same passage as this entry or to a sub-passage of this entry.
	 * 
	 * @param vu The VU to add
	 * @return <tt>true</tt> if the VU was added
	 */
	public boolean addVariationUnit(VariationUnit vu) throws InvaliedVariantUnitException {
	    if (!vu.getPassage().isSubPassageOf(ref)) {
	        throw new InvaliedVariantUnitException(
	                "Could not add VU (" + vu.getId() + "): Supplied VU does not refer to " +
	                "a sub-passage of this entry. Found " + vu.getPassage() +  
	                " but expected " + ref);
	    }
	    
	    return this.variants.add(vu);
	}

	/**
	 * Removes the supplied variation unit (VU).
	 * @param vu The VU to remove
	 * 
	 * @return <tt>true</tt> if this entry contained the provided VU. 
	 */
	public boolean removeVariationUnit(VariationUnit vu) {
	    return this.variants.remove(vu);
	}
	
	/**
	 * Returns an unmodifiable list of the variation units in this entry.
	 * 
	 * @return
	 */
	public SortedSet<VariationUnit> getVariationUnits() {
	    return Collections.unmodifiableSortedSet(this.variants);
	}
	
	public boolean isModifiable() {
	    // This is business logic and should be migrated to a separate class
	    // Entries should be modifiable only if they have not been published, and are not
	    // pending review. These states are not currently represented
	    return true;
	}
	
	//===================================================================================
	// STUB METHODS/MEMBERS TO ACCESS SOURCE TEXTS
    //===================================================================================
	
	/**
	 * TODO IMPLEMENT This is currently placeholder 
	 */
	private Map<String, String> passages = new HashMap<String, String>();
	
	public Set<String> listVersions() {
	    return passages.keySet();
	}
	
	public String getText(String version) {
	    return passages.get(version);
	}
	
	public String getMarkedText(String version) {
	    String text = passages.get(version);
	    
	    // mark this text
	    return text;
	}
	
	void setText(String version, String text) {
	    this.passages.put(version, text);
	}
	
	//===================================================================================
    // EXCEPTION CLASS
    //===================================================================================
    
	public static class NotModifiableException extends RuntimeException {
	    
	}
	
	public static class InvaliedVariantUnitException extends Exception {
	    InvaliedVariantUnitException(String msg) {
	        super(msg);
	    }
	}
}
