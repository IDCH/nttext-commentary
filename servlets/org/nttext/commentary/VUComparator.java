/**
 * 
 */
package org.nttext.commentary;

import java.util.Comparator;

import openscriptures.text.StructureComparator;
import openscriptures.text.StructureRepository;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class VUComparator implements Comparator<VariationUnit> {

    Work work = null;
    StructureRepository repo = null;
    StructureComparator comp = new StructureComparator();
    
    VUComparator(StructureRepository repo, Work w) {
        this.repo = repo;
        this.work = w;
    }
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(VariationUnit vu1, VariationUnit vu2) {
        
        VUReference ref1 = VUReference.findReference(repo, work, vu1);
        VUReference ref2 = VUReference.findReference(repo, work, vu2);
        
        if ((ref1 != null) && (ref2 != null)) {
            return comp.compare(ref1, ref2);
        } else if ((ref1 == null) && (ref2 == null)) {
            return vu1.getId().compareTo(vu2.getId());
        } else if (ref1 == null) {
            return 1;
        } else {
            return -1;
        }
    }

}
