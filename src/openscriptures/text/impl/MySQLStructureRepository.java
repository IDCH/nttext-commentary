/**
 * 
 */
package openscriptures.text.impl;

import openscriptures.text.Structure;
import openscriptures.text.StructureFacade;
import openscriptures.text.StructureRepository;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal_2
 */
public class MySQLStructureRepository implements StructureRepository {
    
    
    MySQLTextRepository repo = null;
    
    MySQLStructureRepository(MySQLTextRepository repo) {
        this.repo = repo;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String)
     */
    @Override
    public Structure create(Work work, String name) {
        
        // TODO Auto-generated method stub
        return new Structure(work, name);
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String, openscriptures.text.Token, openscriptures.text.Token)
     */
    @Override
    public Structure create(Work work, String name, Token start, Token end) {
        // TODO Auto-generated method stub
        return new Structure(work, name, start, end);
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#hasStructuresFor(openscriptures.text.Work)
     */
    @Override
    public boolean hasStructuresFor(Work work) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#getStructureFacade(openscriptures.text.Work)
     */
    @Override
    public StructureFacade getStructureFacade(Work w) {
        return new StructureFacade(w, this);
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#save(openscriptures.text.Structure)
     */
    @Override
    public void save(Structure s) {
        // TODO Auto-generated method stub
        
    }
}
