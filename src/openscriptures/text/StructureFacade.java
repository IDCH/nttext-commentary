/**
 * 
 */
package openscriptures.text;

import java.util.SortedSet;
import java.util.UUID;


public class StructureFacade {

    private Work work;
    
    private StructureRepository repo;

    StructureFacade(Work work, StructureRepository repo) {
        this.work = work;
        this.repo= repo;
    }
    
    public UUID getWorkId() {
        return this.work.getUUID();
    }

    public Structure create(String name) {
        return repo.create(work, name);
    }
    
    public boolean save(Structure s) {
        return repo.save(s);
    }
    
    public SortedSet<Structure> find(String name) {
        return repo.find(work, name);
    }

    public SortedSet<Structure> find(String name, int start, int end) {
        return find(name, start, end, false);
    }
    
    public SortedSet<Structure> find(String name, int start, int end, boolean strict) {
        return repo.find(work, name, start, end, strict);
    }

    public SortedSet<Structure> find(String name, TokenSequence seq) {
        return find(name, seq.getStart(), seq.getEnd(), false);
    }

    public SortedSet<Structure> find(int position) {
        return repo.find(work, position);
    }
    
}