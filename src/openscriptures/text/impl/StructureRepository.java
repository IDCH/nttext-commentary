/**
 * 
 */
package openscriptures.text.impl;

import java.util.SortedSet;

import openscriptures.text.Structure;
import openscriptures.text.StructureFacade;
import openscriptures.text.Token;
import openscriptures.text.TokenSequence;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public interface StructureRepository {

    public Structure create(Work work, String name);
    
    public Structure create(Work work, String name, Token start, Token end);
    
    public boolean hasStructuresFor(Work work);
    
    public StructureFacade getStructureFacade(Work w);
    
    public SortedSet<Structure> find(Work w, String name);
    
    public SortedSet<Structure> find(Work w, int position);
    
    public SortedSet<Structure> find(Work w, String name, TokenSequence seq);
    
    public SortedSet<Structure> find(Work w, String name, int start, int end);
    
    public SortedSet<Structure> find(Work w, String name, int start, int end, boolean strict);
    
    public boolean save(Structure s);
}
