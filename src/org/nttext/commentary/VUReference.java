package org.nttext.commentary;

import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.StructureWrapper;
import openscriptures.text.Work;

/**
 * Identifies the base text of a variation unit in a specific edition of 
 * the New Testament.
 * 
 * @author Neal Audenaert
 */
public class VUReference extends StructureWrapper {
    public static final String STRUCTURE_NAME = "VURef";
    public static final String VU_ATTR = "vuId";
    
    /**
     * Initializes a reference for a variation unit to the form of that unit that 
     * appears in some edition of the New Testament. 
     * 
     * @param vu The variation unit that this refers to.
     * @param s The span of text corresponding to this variation unit in a specific
     *         edition.
     * 
     * @see openscriptures.text.Structure
     */
    public static VUReference init(CommentaryModule repo, Structure structure, VariationUnit vu) {
        VUReference ref = new VUReference(repo, structure);
        ref.vu = vu;
        
        String vuId = vu.getId().toString();
        String attr = structure.getAttribute(VU_ATTR);
        if (attr == null) {
            structure.setAttribute(VU_ATTR, vu.getId().toString());
        } else if (!attr.equals(vuId)) {
            assert false : "Tried to initialize an existing VU structure to a different VU.";
            // TODO do something more intelligent here. 
            ref = null;
        }
        
        return ref;
    }
    
    public static boolean isVUReference(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
    
    private final CommentaryModule repo;
    private VariationUnit vu = null;
    private String edition = null;
    
    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================

    public VUReference(CommentaryModule repo, Structure s) {
        super(s);
        
        this.repo = repo;
    }

    //=======================================================================================
    // ACCESSORS & MUTATORS
    //=======================================================================================
    
    /** 
     * @see openscriptures.text.StructureWrapper#accepts(openscriptures.text.Structure)
     */
    @Override
    public boolean accepts(Structure structure) {
        return isVUReference(structure);
    }
    
    /** 
     * Returns the base reading for the associated variation unit in the 
     * corresponding NT edition.
     */
    public String getBaseReading() {
        return this.getAttribute("baseReading");
    }
    
    /** Private setter for use by persistence layer. */
    public void setBaseReading(String value) {
        // set when initialized or on setStart/setEnd
        this.setAttribute("baseReading", value);
    }

    public VariationUnit getVariationUnit() {
        if (vu != null) 
            return vu;

        Long vuId = null;
        try {
            vuId = Long.parseLong(this.getAttribute(VU_ATTR));
            vu = repo.getVURepository().find(vuId);
        } catch (NumberFormatException nfe) {
            // TODO handle this somehow.
        }
        
        return vu;
    }
    
    public Work getWork() {
        UUID workUUID = this.getWorkUUID();
        return this.repo.getWorkRepository().find(workUUID);
    }
    
    public String getEdition() {
        if (edition != null) 
            return edition;
        
        Work w = this.getWork();
        if (w != null) {
            edition = w.getAbbreviation();
        }
        
        return (edition != null) ? edition : "unknown";
    }
}