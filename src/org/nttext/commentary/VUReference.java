package org.nttext.commentary;

import java.util.UUID;

import openscriptures.text.Structure;

import org.nttext.commentary.VariationUnit.TextProxy;

/**
 * Identifies the base text of a variation unit in a specific edition of 
 * the New Testament.
 * 
 * @author Neal Audenaert
 */
//@Entity
//@Table(name="VU_References")
public class VUReference {
    // TODO think about making this a TokenSequence

    //=======================================================================================
    // MEMBER VARIABLES
    //=======================================================================================
    
    private Long id;
    
    private VariationUnit vu;      // The variation unit this is a reference for

    /** Marks the token sequence that this VU refers to. */
    private Structure structure;

    /** The text of the identified VU. This is redundant to the text segment marked out
     *  by the start and end pointers and can be used to check that the matched text 
     *  has not changed. */
    private String baseReading;

    // Temporary place holders - these should be drawn form the Work object for this text. 
    private TextProxy source;

    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================
    
    /** Restricted scope default for use by persistence layer. */
    VUReference() {
        
    }
    
    /**
     * Creates a reference for a variation unit to the form of that unit that 
     * appears in some edition of the New Testament.
     * 
     * @param vu The variation unit that this refers to.
     * @param s The span of text corresponding to this variation unit in a specific
     *         edition.
     * 
     * @see openscriptures.text.Structure
     */
    public VUReference(VariationUnit vu, Structure s) {
        this.vu = vu;
        this.structure = s;
        this.baseReading = s.getText();
    }


    @Deprecated
    public VUReference(VariationUnit vu, TextProxy source, String match) {
        this.vu = vu;
        this.source = source;
        this.baseReading = match;
    }

    //=======================================================================================
    // ACCESSORS & MUTATORS
    //=======================================================================================
    
    /** Restricted access getter for use by persistence layer. */
    Long getId() {
        return this.id;
    }
    
    /** Private setter for use by persistence layer */
    void setId(Long value) {
        this.id = value;
    }
    
    /** 
     * Returns the base reading for the associated variation unit in the 
     * corresponding NT edition.
     */
    public String getBaseReading() {
        return this.baseReading;
    }
    
    /** Private setter for use by persistence layer. */
    @SuppressWarnings("unused")
    private void setBaseReading(String value) {
        this.baseReading = value;
    }
    
    
    /**
     * Returns the structure that mark the associated variation unit in the
     * corresponding NT edition. 
     * 
     * @return The structure that marks this variation unit.
     */
    public Structure getReference() {
        return this.structure;
    }
    
    /** Returns the unique identifier for the structure that defines this reference. This is
     *  used by the persistence layer. */
    String getReferenceId() {
        // TODO IMPLEMENT
        return null;
//        return this.structure.getLocalId();
    }
    
    /** Used by the persistence layer to restore the scripture reference. */
    void setReferenceId(String id) {
        UUID uuid = UUID.fromString(id);
        Commentary commentary = Commentary.getInstance();
        
        this.structure = commentary.getStructureById(uuid);
        // TODO Make sure this worked.
    }
    
    
    
    /**
     * Returns the variation unit that this reference describes. 
     * @return the variation unit that this reference describes.
     */
    public VariationUnit getVariationUnit() {
        return this.vu;
    }
    
    /** Private setter for use by persistence framework. */
    @SuppressWarnings("unused")
    private void setVariationUnit(VariationUnit vu) {
        this.vu = vu;
    }
    
    /** 
     * Returns the abbreviated name of the corresponding NT edition (e.g., HCSB or 
     * SBLGNT). This is not the full tile of the edition.
     * 
     * @return The name of the corresponding NT edition.
     */
    public String getEdition() {
        // TODO IMPLEMENT
//        WorkId id = this.structure.getWorkUUID().getWorkId();
//        return id.getName();
        return null;
    }

    /** 
     * The name of the language (in English) that the corresponding NT edition
     * is written in.
     * 
     * @return The language name of the corresponding NT edtion.
     */
    public String getLanguage() {
        return this.source.getLanguage();
    }

    /**
     * Returns the three letter code for the language that the corresponding NT 
     * edition is written in.
     * 
     * @return The three letter language code.
     */
    public String getLgCode() {
        return this.source.getLgCode();
    }
}