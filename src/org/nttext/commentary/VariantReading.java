/**
 * 
 */
package org.nttext.commentary;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.nttext.mss.Manuscript;

/**
 * Provides information about a witnessed reading of a variation unit. 
 * 
 * @author Neal Audenaert
 */
public class VariantReading {
    // TODO better documentation - this needs (somewhere) to do a better job of 
    //      describing variation units and variant readings.
    
    //=================================================================================
    // MEMBER VARIABLES
    //=================================================================================
    
    private UUID id;
    
    private VariationUnit vu;
    private String englishRdg = "";
    private String greekRdg = "";
    
    // TODO need to create a comparator. 
    SortedSet<Manuscript> witnesses = new TreeSet<Manuscript>();
    String witnessDescription = null;
    
    //=================================================================================
    // CONSTRUCTORS
    //=================================================================================
    
    /**
     * 
     * @param vu
     */
    public VariantReading(VariationUnit vu) {
        this.vu = vu;
    }
    
    public VariantReading(VariationUnit vu, String greek) {
        this.vu = vu;
        this.greekRdg = greek;
    }
    
    public VariantReading(VariationUnit vu, String english, String greek) {
        this.vu = vu;
        this.englishRdg = english;
        this.greekRdg = greek;
    }
    
    //================================================================================
    // ACCESSORS & MUTATORS
    //=================================================================================
    
    
    /** Returns the unique identifier for this reading. */
    public String getId() {
        return id.toString();
    }
    
    /** Returns the variation unit this is a reading of. */
    public VariationUnit getVariationUnit() {
        return vu;
    }

    /** Returns the English language gloss of this reading. */
    public String getEnglishReading() {
        return this.englishRdg;
    }
    
    /** Sets the English language gloss for this reading. */
    public void setEnglishReading(String rdg) {
        this.englishRdg = rdg;
    }
    
    /** 
     * Returns the Greek language form of this reading as found in existing manuscripts 
     * or in critical editions of the Greek New Testament. Note that some critical editions
     * may supply 'eclectic' readings that are supposed to correct scribal errors but that 
     * are not found in any extant manuscript. 
     */
    public String getGreekReading() {
        return this.greekRdg;
    }

    /**
     * Sets the Greek language form of this reading as found in existing manuscripts
     * or in critical edition of the Greek New Testament. 
     */
    public void setGreekReading(String rdg) {
        this.greekRdg = rdg;
    }
    
    /**
     * Returns a textual description of the manuscripts in which this reading is found. 
     * A variant reading will typically supply a list of manuscripts in which that 
     * particular reading is found. However, it is often the case that the 'base' reading
     * of the text is shared by nearly all manuscript witnesses. In those cases, a short 
     * descriptive summary (for example, "All but 8 manuscripts", or "all Byzantine 
     * manuscripts" may be more readable than enumerating all of the witnesses. The 
     * witness description property can be used in these cases in addition to or instead 
     * of enumerating the manuscripts witnesses.  
     *  
     * @return a textual description of the manuscripts in which this reading is found.
     */
    public String getWitnessDescription() {
        return this.witnessDescription;
    }
    
    /** Returns a list of manuscripts that are known to have this reading. */
    public SortedSet<Manuscript> getWitnesses() {
        return Collections.unmodifiableSortedSet(this.witnesses);
    }
    
    /** 
     * Adds a manuscript to the list of witnesses to this reading.
     * @param ms the manuscript to add
     * @return <tt>true</tt> if the manuscript was not already listed as a witness to 
     *      this reading.  
     */
    public boolean addWitness(Manuscript ms) {
        return this.witnesses.add(ms);
    }
    
    /**
     * Removes the specified manuscript from the list of witnesses to this reading. 
     *  
     * @param ms The manuscript to remove
     * @return <tt>true</tt> if the manuscript was already listed as a witness to 
     *      this reading.
     */
    public boolean removeWitness(Manuscript ms) {
        return this.witnesses.remove(ms);
    }
}
