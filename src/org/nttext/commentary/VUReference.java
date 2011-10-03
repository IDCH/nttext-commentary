package org.nttext.commentary;

import openscriptures.text.Structure;
import openscriptures.text.WorkId;

import org.nttext.commentary.VariationUnit.TextProxy;

/**
 * Identifies the base text of a variation unit in a specific edition of 
 * the New Testament.
 * 
 * @author Neal Audenaert
 */
public class VUReference {
    // TODO think about making this a TokenSequence

    private VariationUnit vu;      // The variation unit this is a reference for

    /** Marks the token sequence that this VU refers to. */
    private Structure structure;

    /** The text of the identified VU. This is redundant to the text segment marked out
     *  by the start and end pointers and can be used to check that the matched text 
     *  has not changed. */
    private String baseReading;

    // Temporary place holders - these should be drawn form the Work object for this text. 
    private TextProxy source;

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

    /** 
     * Returns the base reading for the associated variation unit in the 
     * corresponding NT edition.
     */
    public String getBaseReading() {
        return this.baseReading;
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
    
    /**
     * Returns the variation unit that this reference describes. 
     * @return the variation unit that this reference describes.
     */
    public VariationUnit getVariationUnit() {
        return this.vu;
    }
    
    /** 
     * Returns the abbreviated name of the corresponding NT edition (e.g., HCSB or 
     * SBLGNT). This is not the full tile of the edition.
     * 
     * @return The name of the corresponding NT edition.
     */
    public String getEdition() {
        WorkId id = this.structure.getWork().getWorkId();
        return id.getName();
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