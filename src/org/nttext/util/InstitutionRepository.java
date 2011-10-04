/**
 * 
 */
package org.nttext.util;

import java.util.List;

/**
 * @author Neal Audenaert
 */
public interface InstitutionRepository {

    public List<Institution> find(String name);
    
    public Institution findFirst(String name);
    
    public Institution findOrCreate(String name);
}
