/**
 * 
 */
package openscriptures.text.impl;

import java.util.SortedSet;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import openscriptures.text.Structure;
import openscriptures.text.StructureRepository;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class JPAStructureRepository extends JPARepository<Structure> implements StructureRepository {

    public JPAStructureRepository(EntityManagerFactory emf) {
        super(emf);
    }
    
    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String)
     */
    public Structure create(Work work, String name) {
        return create(new Structure(work, name));
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String, openscriptures.text.Token, openscriptures.text.Token)
     */
    @Override
    public Structure create(Work work, String name, Token start, Token end) {
        return create(new Structure(work, name, start, end));
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#hasStructuresFor(openscriptures.text.Work)
     */
    @Override
    public boolean hasStructuresFor(UUID workId) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(java.util.UUID)
     */
    @Override
    public Structure find(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(long)
     */
    @Override
    public Structure find(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#synchronize(openscriptures.text.Structure)
     */
    @Override
    public Structure synchronize(Structure s) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean save(Structure s) {
        super.save(s);
        return true;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(openscriptures.text.Work, java.lang.String)
     */
    public SortedSet<Structure> find(Work w, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(openscriptures.text.Work, int)
     */
    public SortedSet<Structure> find(Work w, int position) {
        // TODO Auto-generated method stub
        return null;
    }
//
//    /* (non-Javadoc)
//     * @see openscriptures.text.StructureRepository#find(openscriptures.text.Work, java.lang.String, openscriptures.text.TokenSequence)
//     */
//    public SortedSet<Structure> find(Work w, String name, TokenSequence seq) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(openscriptures.text.Work, java.lang.String, int, int)
     */
    public SortedSet<Structure> find(Work w, String name, int start, int end) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#find(openscriptures.text.Work, java.lang.String, int, int, boolean)
     */
    public SortedSet<Structure> find(Work w, String name, int start, int end,
            boolean strict) {
        // TODO Auto-generated method stub
        return null;
    }
}
