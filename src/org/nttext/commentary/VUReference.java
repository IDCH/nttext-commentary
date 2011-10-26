package org.nttext.commentary;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.StructureRepository;
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
    public static VUReference init(Structure structure, VariationUnit vu) {
        VUReference ref = new VUReference(vu, structure);
        
        String vuId = vu.getId().toString();
        String attr = structure.getAttribute(VU_ATTR);
        if (attr == null) {
            structure.setAttribute(VU_ATTR, vuId);
        } else if (!attr.equals(vuId)) {
            assert false : "Tried to initialize an existing VU structure to a different VU.";
            // TODO do something more intelligent here. 
            ref = null;
        }
        
        return ref;
    }
    
    public static VUReference findReference(StructureRepository repo, Work w, VariationUnit vu) {
        SortedSet<Structure> structures = 
                repo.find(w, STRUCTURE_NAME, VU_ATTR, vu.getId().toString());
        if (structures.size() > 1) {
//          LOGGER.warn("Found multiple references for the work: " + workId);
        }
          
        VUReference ref = null;
        if (structures.size() > 0) {
            ref = new VUReference(vu, structures.first());
        }
        
        return ref;
    }
    
    /**
     * 
     * @param repo
     * @param vu
     * @return
     */
    public static Map<UUID, VUReference> findReferences(
            StructureRepository repo, VariationUnit vu) {
        
        Map<UUID, SortedSet<Structure>> structures =
                repo.find(STRUCTURE_NAME, VU_ATTR, vu.getId().toString());

        Map<UUID, VUReference>  references = new HashMap<UUID, VUReference>();
        for (UUID workId : structures.keySet()) {
            SortedSet<Structure> structs = structures.get(workId); 
            if (structs.size() > 1) {
//                LOGGER.warn("Found multiple references for the work: " + workId);
            }
            
            if (structs.size() > 0) {
                VUReference ref = new VUReference(vu, structs.first());
                references.put(workId, ref);
            }
        }
        
        return references;
    }
    
    
    public static boolean isVUReference(Structure s) {
        return s.getName().equals(STRUCTURE_NAME);
    }
    
//    private final CommentaryModule repo;
    private VariationUnit vu = null;
    
    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================

    public VUReference(VariationUnit vu, Structure s) {
        super(s);
        this.vu = vu;
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
        return vu;
    }
}