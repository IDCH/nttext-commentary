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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

/**
 * Represents a single entry in the commentary.
 * 
 * @author Neal Audenaert
 */
//@Entity
//@Table(name="Entries")
public class Entry {
    
    
    //======================================================================================
    // MEMBER VARIABLES
    //======================================================================================
    
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
	    this.id = UUID.randomUUID();
		this.ref = ref;
	}
	
	//======================================================================================
    // ACCESSORS AND MUTATORS
    //======================================================================================
    
	/** Returns the unique identifier for this entry. */
	@Id
	public String getId() {
	    return id.toString();
	}
	
	void setId(String id) {
	    this.id = UUID.fromString(id);
	}
	
	/**
	 * Returns a stringified version of the scripture reference that this Entry describes.
	 */
	@Basic
	String getRefString() {
	    return this.ref.toString();
	}

	/** Sets the scripture reference this Entry describes. This is used by the 
	 *  persistence layer. */ 
	@SuppressWarnings("unused")
    private void setRefString(String ref) {
	    this.ref = new VerseRange(ref);
	}
	
	/** Returns the scripture passage this entry refers to. */
	@Transient
	public Passage getReference() {
		return this.ref;
	}
	
	/** Returns the overview text associated with this entry. This is expected to be
	 *  plain UTF-8 text. */
	@Lob
	public String getOverview() {
	    return this.overview;
	}
	
	/**
	 * Sets the overview text for this entry.
	 * 
	 * @param text The text to set.
	 */
	public void setOverview(String text) {
	    this.overview = text;
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
	@ManyToMany
	// TODO more detail here!!
	public SortedSet<VariationUnit> getVariationUnits() {
	    return Collections.unmodifiableSortedSet(this.variants);
	}
	
	/**
	 * Sets the variation units associated with this entry. This is used by the 
	 * persistence layer. 
	 * 
	 * @param variationUnits The variation units associated with this entry.
	 */
	@SuppressWarnings("unused")
    private void setVariationUnits(Set<VariationUnit> variationUnits) {
	    // TODO use a comparator
	    this.variants = new TreeSet<VariationUnit>();
	    this.variants.addAll(variationUnits);
	    
	}
	
	@Transient
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
	
	@Transient
	public String getText(String version) {
	    return passages.get(version);
	}
	
	@Transient
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
