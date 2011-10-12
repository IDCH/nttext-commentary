/**
 * 
 */
package openscriptures.text.impl.mem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.StructureComparator;
import openscriptures.text.StructureFacade;
import openscriptures.text.StructureFactory;
import openscriptures.text.Token;
import openscriptures.text.TokenSequence;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class MemStructureFactory implements StructureFactory {

    private Map<String, StructureCollection> collections = 
        new HashMap<String, StructureCollection>();
    
    
    public StructureCollection getCollection(String id) {
        return collections.get(id);
    }
    
//=======================================================================================
// FACTORY METHODS
//=======================================================================================  
     
    /**
     * Creates a new named <tt>Structure</tt> for the specified <tt>Work</tt>.
     *  
     * @param work The work that the structure annotates
     * @param name The name of the structure to create
     * @param start The starting token for the structure (or null if the starting token
     *      will be assigned later)
     * @param end The ending token (exclusive) for the structure (or null if the ending 
     *      token will be assigned later)
     * @return The newly created structure.
     * @throws TODO this should throw something if the structure cannot be created 
     *      for whatever reason.
     */
    @Override
    public Structure createStructure(Work work, String name, Token start, Token end) {
        return new Structure(work, name, start, end);
    }
    
    /** 
     * 
     * @param s
     * @return
     */
    @Override
    public boolean save(Structure s) {
        UUID id = s.getWork().getUUID();
        StructureCollection collection; 
        synchronized (collections) {
            collection = collections.get(id.toString());
            if (collection == null) {
                collection = new StructureCollection(id);
                collections.put(id.toString(), collection);
            }
        }
        
        collection.add(s);
        return true;
    }
    
    /**
     * Indicates whether or not this factory has strcutures for the provided work.
     * 
     * @param work The work to check.
     * @return <tt>true</tt> if structures are present, false if they are not.
     */
    public boolean hasStructuresFor(Work work) {
        boolean haveStructures;
        synchronized (collections) {
            haveStructures = collections.containsKey(work.getUUID().toString());
        }
        
        return haveStructures;
    }
    
    @Override
    public StructureFacade getStructureFacade(Work w) {
        if (collections.containsKey(w.getUUID().toString()))
            return new MemStructureFacade(w, this);
        else return null;
    }
    
    
//=======================================================================================
// INNER CLASS - STRUCTURE COLLECTION
//=======================================================================================  
    
    /**
     * Represents the structures associated with a particular <tt>{@link Work}</tt>. 
     * 
     * @author Neal Audenaert
     */
    private static class StructureCollection {
        private static final StructureComparator COMPARATOR = new StructureComparator();
        
        private UUID textId = null;
        
        private SortedSet<Structure> structures =
            new TreeSet<Structure>(COMPARATOR);
        
        // indices
        private Map<String, SortedSet<Structure>> nameIndex = 
            new HashMap<String, SortedSet<Structure>>();
            
        StructureCollection(UUID id) {
            this.textId = id;
        }
        
        /**
         * 
         * @param id
         */
        StructureCollection(String id) {
            this.textId = UUID.fromString(id);
        }

        public UUID getTextId() {
            return this.textId;
        }
        
        public void add(Structure s) {
            structures.add(s);
            
            // add to names index
            synchronized (nameIndex) {
                SortedSet<Structure> set = nameIndex.get(s.getName());
                if (set == null) {
                    set = new TreeSet<Structure>(COMPARATOR);
                }
                
                set.add(s);
                nameIndex.put(s.getName(), set);
            }
        }
    }
    
//=======================================================================================
// INNER CLASS - STRUCTURE FACADE
//=======================================================================================  
     
    private static class MemStructureFacade implements StructureFacade {

        private Work work;
        
        private MemStructureFactory factory;
       
        MemStructureFacade(Work work, MemStructureFactory factory) {
            this.work = work;
            this.factory = factory;
        }
        
        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#getWorkId()
         */
        @Override
        public UUID getWorkId() {
            return this.work.getUUID();
        }

        @Override
        public Structure create(String name) {
            return new Structure(work, name, null, null);
        }
        
        @Override
        public boolean save(Structure s) {
            return factory.save(s);
        }
        
        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#find(java.lang.String)
         */
        @Override
        public SortedSet<Structure> find(String name) {
            StructureCollection collection = factory.getCollection(work.getUUID().toString());
            SortedSet<Structure> structures = collection.nameIndex.get(name);
            
            return Collections.unmodifiableSortedSet(structures);
        }

        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#find(java.lang.String, int, int)
         */
        @Override
        public SortedSet<Structure> find(String name, int start, int end) {
            return find(name, start, end, false);
        }
        
        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#find(java.lang.String, int, int)
         */
        @Override
        public SortedSet<Structure> find(String name, int start, int end, boolean strict) {
            // FIXME this has really poor performance
            SortedSet<Structure> result =
                new TreeSet<Structure>(new StructureComparator());
            for (Structure struct : this.find(name)) {
                int s = struct.getStart(),
                    e = struct.getEnd();
                
                if (e <= start)         // we haven't entered our search range yet.
                    continue;
                else if (s >= end)      // we've reached the end of our search range 
                    break;
                
                if (!strict) 
                    result.add(struct);
                else if (s >= start && e <= end)
                    result.add(struct);
            }
            
            return result;
        }

        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#find(java.lang.String, openscriptures.text.Structure)
         */
        @Override
        public SortedSet<Structure> find(String name, TokenSequence seq) {
            return find(name, seq.getStart(), seq.getEnd(), false);
        }

        /* (non-Javadoc)
         * @see openscriptures.text.StructureFacade#find(int)
         */
        @Override
        public SortedSet<Structure> find(int position) {
            // FIXME this has really poor performance
            StructureCollection collection = factory.collections.get(work.getUUID());
            SortedSet<Structure> result = new TreeSet<Structure>(new StructureComparator());
            for (Structure s : collection.structures) {
                if (s.getEnd() <= position)
                    continue;
                
                if (s.getStart() > position)
                    break;
                
                result.add(s);
            }
            
            return result;
        }
        
    }
    
}
