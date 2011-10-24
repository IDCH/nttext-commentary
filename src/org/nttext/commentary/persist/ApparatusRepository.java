/**
 * 
 */
package org.nttext.commentary.persist;

import org.nttext.commentary.VariantReading;
import org.nttext.commentary.VariationUnit;

/**
 * Defines methods for creating, finding and removing variation units and variant readings.
 * Implementations of this class are used to encapsulate interactions with a specific 
 * persistence layer.  
 * 
 * @author Neal Audenaert
 */
public interface ApparatusRepository {

    public VariationUnit createVariationUnit();
    
    public VariantReading createVariantReading(VariationUnit vu);
    
    public void remove(VariationUnit vu);
    
    public void remove(VariantReading rdg);
    
}
