/**
 * 
 */
package openscriptures.text;

import java.util.UUID;

/**
 * @author Neal Audenaert
 */
public interface StructureProviderFactory {

    public StructureFacade getStructureProvider(UUID id);
    
    public StructureFacade getStructureProvider(WorkId id);
    
    public StructureFacade getStructureProvider(Work work);
}
