/**
 * 
 */
package org.nttext.util;

import java.util.List;

/**
 * @author Neal Audenaert
 */
public interface LocationRepository {
    
    public List<Location> find(String name);
    
    public Location findFirst(String name);
    
    public Location findOrCreate(String name);

}
