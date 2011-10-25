/**
 * 
 */
package org.nttext.commentary;

import java.util.List;


/**
 * @author Neal Audenaert
 */
public interface VariantReadingRepository {

    /** Creates a new variant reading for the supplied VU. The new reading will be added 
     *  as the last in the list readings for the supplied VU. */
    public VariantReading create(VariationUnit vu);
    
    /** Creates a new VariantReading with the specified English and Greek glosses. */
    public VariantReading create(VariationUnit vu, String english, String greek);
    
    /** Retrieves all variant readings for the supplied variation unit. */
    public List<VariantReading> find(VariationUnit vu);
    
    public VariantReading synchronize(VariantReading rdg);
    
    /** Saves any changes in the supplied variant reading to the database. */
    public boolean save(VariantReading rdg);
    
    /** Removes the supplied variant reading from the database. */
    public boolean remove(VariantReading rdg);
}
