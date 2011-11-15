/**
 * 
 */
package org.nttext.commentary.persist.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import org.apache.log4j.Logger;
import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRange;
import org.idch.bible.ref.VerseRef;
import org.nttext.commentary.EntryInstance;
import org.nttext.commentary.InstanceRepository;
import org.nttext.commentary.VariationUnit;

/**
 * @author Neal Audenaert
 */
public class MySQLEntryInstanceRepository implements InstanceRepository {
    private static final Logger LOGGER = Logger.getLogger(MySQLEntryInstanceRepository.class);

    private static final int PASSAGE = 1;
    private static final int OVERVIEW = 2;
    private static final int CREATED = 3;
    private static final int MODIFIED = 4;
    
    private static final int INSTANCE_ID = 5;
    
    private static final String FIELDS =  
            "passage, overview, date_created, last_updated "; 
    
    private static final int INS_BOOK = 3;
    private static final int INS_S_CH = 4;
    private static final int INS_S_VS = 5;
    private static final int INS_E_CH = 6;
    private static final int INS_E_VS = 7;
    private static final int INS_HASH = 8;
    
    private static final String PASSAGE_FIELDS = 
            "book_id, start_ch, start_vs, end_ch, end_vs, vs_hash";
    
    private MySQLCommentaryModule repo = null;
    
    MySQLEntryInstanceRepository(MySQLCommentaryModule repo) {
        this.repo = repo;
    }
    
    /* (non-Javadoc)
     */
    @Override
    public EntryInstance create(Passage passage) {
        return this.create(new EntryInstance(passage));
    }
   
    private int hash(Passage passage) {
        VerseRef start = passage.getFirst();
        int bk = start.getBookIndex();
        int ch = (start.isChapterSpecified()) ? start.getChapter() : -1;
        int vs = (start.isVerseSpecified()) ? start.getVerse() : -1;
        
        return hash(bk, ch, vs);
    }
    
    private int hash(int bk, int ch, int vs) {
        int hash = ((bk > 0) ? 1000000 * bk : 0) +
                   ((ch > 0) ?    1000 * ch : 0) +
                   ((vs > 0) ?           vs : 0);
        return hash;
    }
    
    /**
     * 
     * @param instance
     * @return
     */
    public EntryInstance create(EntryInstance instance) {
        assert (instance.getId() == null) : "This instance has already been created.";
        if (instance.getId() != null)
            return null;
        
        String sql = "INSERT INTO nttextcomm_instances (" + FIELDS +", " + PASSAGE_FIELDS +") " +
                     "VALUES (?, ?, now(), now(), ?, ?, ?, ?, ?, ?)";
        
        Passage passage = instance.getPassage();
        VerseRef start = passage.getFirst();
        VerseRef end = passage.getLast();

        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(PASSAGE, passage.toOsisId());
            stmt.setString(OVERVIEW, instance.getOverview());
            stmt.setInt(INS_BOOK, start.getBookIndex());
            stmt.setInt(INS_S_CH, start.getChapter());
            stmt.setInt(INS_S_VS, start.getVerse());
            stmt.setInt(INS_E_CH, end.getChapter());
            stmt.setInt(INS_E_VS, end.getVerse());
            stmt.setInt(INS_HASH, hash(passage));
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                instance.setId(id);
            } else {
                instance = null;
            }
            
            conn.commit();
            
            if (instance != null) LOGGER.info("Creatied entry instance: " + passage.toOsisId());
            else                  LOGGER.warn("Failed to create entry instance: " + passage.toOsisId());
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create instance: " + instance.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return instance;
    }

    private EntryInstance restore(ResultSet results) throws SQLException {
        EntryInstance instance = null;
        
        long id = results.getLong(INSTANCE_ID);
        // TODO add caching
        String ref = results.getString(PASSAGE);
        String overview = results.getString(OVERVIEW);
        Date created = results.getDate(CREATED);
        Date modified = results.getDate(MODIFIED);
        
        Passage passage = new VerseRange(ref);
        instance = new EntryInstance(id, passage, overview, created, modified);
        
        Connection conn = results.getStatement().getConnection();
        Set<VariationUnit> variationUnits = this.getVU(conn, instance);
        instance.setVariationUnits(variationUnits);
        return instance;
    }
    
    
    /* (non-Javadoc)
     */
    @Override
    public EntryInstance find(long id) {
        String sql = "SELECT " + FIELDS + ", instance_id " +
                     "  FROM nttextcomm_instances " +
        		     " WHERE instance_id = ?";
        
        EntryInstance instance = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, id);
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                instance = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve instance (" + id + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return instance;
    }
    
    
    
    /* (non-Javadoc)
     */
    @Override
    public EntryInstance find(Passage passage) {
        // TODO this will need to be reworked when we allow multiple instances per Entry
        String sql = "SELECT " + FIELDS + ", instance_id " +
                     "  FROM nttextcomm_instances " +
                     " WHERE passage = ?";
       
        EntryInstance instance = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, passage.toOsisId());
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                instance = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve instance (" + passage + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
       
        return instance;
    }
    
    private Passage findPrecedingChapter(Connection conn, int hash) 
            throws SQLException {
        
        String sql = "SELECT passage, max(book_id), max(start_ch), min(start_vs) " +
                     "  FROM nttextcomm_instances " +
                     " WHERE vs_hash < ?" +
                     " ORDER BY vs_hash DESC";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, hash);
        
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String ref = results.getString(1);
            if (ref != null)
                return new VerseRange(ref);
        }
        
        return null;
    }
    
    private Passage findPrecedingVerse(Connection conn, int hash) 
            throws SQLException {
        String sql = "SELECT passage FROM nttextcomm_instances " +
                     " WHERE vs_hash < ?" +
                     " ORDER BY vs_hash DESC" +
                     " LIMIT 1";
       
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, hash);
       
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String ref = results.getString(1);
            if (ref != null)
                return new VerseRange(ref);
        }
       
        return null;
    }
    
    private Passage findFollowingVerse(Connection conn, int hash) 
            throws SQLException {
        String sql = "SELECT passage FROM nttextcomm_instances " +
                     " WHERE vs_hash > ?" +
                     " ORDER BY vs_hash ASC" +
                     " LIMIT 1";
       
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, hash);
       
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String ref = results.getString(1);
            if (ref != null)
                return new VerseRange(ref);
        }
       
        return null;
    }
    
    private Passage findFollowingChapter(Connection conn, int bk, int ch) 
            throws SQLException {
        
        String sql = "SELECT passage, min(book_id), min(start_ch), min(start_vs) " +
                     "  FROM nttextcomm_instances " +
                     " WHERE vs_hash > ?";
        
        int hash = hash(bk, ch + 1, 0);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, hash);
        
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String ref = results.getString(1);
            if (ref != null)
                return new VerseRange(ref);
        }
        
        return null;
    }
    
   
    /**
     * Finds the passages needed to support next/previous navigation. This will 
     * return an array of four (possibly <tt>null</tt>) passages. The first will be 
     * the first entry for the immediately preceding chapter, followed by the preceding 
     * verse, following verse, and following chapter.
     * 
     * @param instance
     * @return
     */
    public Passage[] findNavigationalPassages(EntryInstance instance) {
        Passage[] passages = new Passage[4];
        
        VerseRef ref = instance.getPassage().getFirst();
        int bk = ref.getBookIndex();
        int ch = (ref.isChapterSpecified()) ? ref.getChapter() : -1;
        
        int hash = hash(ref);
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            
            passages[0] = findPrecedingChapter(conn, hash);
            passages[1] = findPrecedingVerse(conn, hash);
            passages[2] = findFollowingVerse(conn, hash);
            passages[3] = findFollowingChapter(conn, bk, ch);
            
        } catch (Exception ex) {
            String msg = "Could not retrieve navigational passages for entry (" + 
                    instance.getPassage() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
       
        return passages;
    }

    /* (non-Javadoc)
     */
    @Override
    public boolean save(EntryInstance instance) {
        assert (instance.getId() != null) : "This instance has not been created.";
        if (instance.getId() == null)
            return false;
        
        int ID = 2, OVERVIEW = 1;
        String sql = "UPDATE nttextcomm_instances SET " +
        		     "  overview = ?, " +
        		     "  last_updated = now()" +
        		     "WHERE instance_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(OVERVIEW, instance.getOverview());
            stmt.setLong(ID, instance.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while saving an instance (" + instance.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create instance: " + instance.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            success = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }
    
    /* (non-Javadoc)
     */
    @Override
    public boolean remove(EntryInstance instance) {
        assert (instance.getId() != null) : "This instance has not been created.";
        if (instance.getId() == null)
            return false;
        
        int ID = 1;
        String sql = "DELETE FROM nttextcomm_instances " +
                     " WHERE instance_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(ID, instance.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while removing an instance (" + instance.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not remove instance: " + instance.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            success = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }

    /* (non-Javadoc)
     */
    @Override
    public boolean associate(EntryInstance instance, VariationUnit vu) {
        int INSTANCE_ID = 1, VU_ID = 2;
        String sql = "INSERT INTO nttextcomm_entryvus (instance_id, vu_id)" +
        		     "VALUES (?, ?)";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(INSTANCE_ID, instance.getId());
            stmt.setLong(VU_ID, vu.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = numRowsChanged == 1;
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while associating a variation unit (" + vu.getId() + ") " +
                            "with and instance (" + instance.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                if (success) instance.addVariationUnit(vu);
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String vuId = (vu != null) ? vu.getId() + "" : "null";
            String id = (instance != null) ? instance.getId() + "" : "null";
            String msg = "Could not associate a variation unit (" + vuId + ") " +
                         "with an instance (" + id + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }

    /* (non-Javadoc)
     */
    @Override
    public boolean disassociate(EntryInstance instance, VariationUnit vu) {
        int INSTANCE_ID = 1, VU_ID = 2;
        String sql = "DELETE FROM nttextcomm_entryvus " +
        		     " WHERE instance_id = ? AND vu_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(INSTANCE_ID, instance.getId());
            stmt.setLong(VU_ID, vu.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = numRowsChanged == 1;
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while disassociating a variation unit (" + vu.getId() + ") " +
                            "with and instance (" + instance.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                if (success) instance.removeVariationUnit(vu);
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not disassociate a variation unit (" + vu.getId() + ") " +
                         "with an instance (" + instance.getId() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }
    
    Set<VariationUnit> getVU(Connection conn, EntryInstance instance) throws SQLException {
        int INSTANCE_ID = 1, VU_ID = 1;
        String sql = "SELECT vu_id FROM nttextcomm_entryvus WHERE instance_id = ? ";
        
        MySQLVariationUnitRepository vuRepo = 
                (MySQLVariationUnitRepository)repo.getVURepository();
        Set<VariationUnit> VUs = new HashSet<VariationUnit>();
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(INSTANCE_ID, instance.getId());
        
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            long vuId = results.getLong(VU_ID);
            VUs.add(vuRepo.find(conn, vuId));
        }
       
        return VUs;
    }

    /* (non-Javadoc)
     */
    @Override
    public Set<VariationUnit> getVU(EntryInstance instance) {
        Set<VariationUnit> VUs = new HashSet<VariationUnit>();
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            VUs = getVU(conn, instance);
            
        } catch (Exception ex) {
            String msg = "Could not retrieve associated VUs for  (" + instance.getId()+ "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            instance = null;
        } finally {
            repo.closeConnection(conn);
        }
       
        return VUs;
    }
}
