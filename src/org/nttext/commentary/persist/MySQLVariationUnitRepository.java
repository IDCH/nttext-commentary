/**
 * 
 */
package org.nttext.commentary.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.apache.log4j.Logger;
import org.nttext.commentary.VariationUnit;

/**
 * @author Neal_2
 */
public class MySQLVariationUnitRepository implements VURepository {
    private static final Logger LOGGER = Logger.getLogger(MySQLVariationUnitRepository.class);

    private static final int PASSAGE = 1;
    private static final int OVERVIEW = 2;
    private static final int CREATED = 3;
    private static final int MODIFIED = 4;
    
    private static final int VU_ID = 5;
    
    private static final String FIELDS =  
            "passage, commentary, date_created, last_updated "; 
    
    private MySQLCommentaryRepo repo = null;
    
    MySQLVariationUnitRepository(MySQLCommentaryRepo repo) {
        this.repo = repo;
    }
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#create(openscriptures.ref.Passage)
     */
    @Override
    public VariationUnit create(Passage passage) {
        return this.create(new VariationUnit(passage));
    }
   
    /**
     * 
     * @param vu
     * @return
     */
    public VariationUnit create(VariationUnit vu) {
        assert (vu.getId() == null) : "This VU has already been created.";
        if (vu.getId() != null)
            return null;
        
        String sql = "INSERT INTO NTTEXTComm_VUs (" + FIELDS +") " +
                     "VALUES (?, ?, now(), now())";
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(PASSAGE, vu.getPassage().toString());
            stmt.setString(OVERVIEW, vu.getCommentary());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                vu.setId(id);
            } else {
                vu = null;
            }
            
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create VU: " + vu.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            vu = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return vu;
    }

    private VariationUnit restore(ResultSet results) throws SQLException {
        VariationUnit vu = null;
        
        long id = results.getLong(VU_ID);
        // TODO add caching
        String ref = results.getString(PASSAGE);
        String overview = results.getString(OVERVIEW);
        Date created = results.getDate(CREATED);
        Date modified = results.getDate(MODIFIED);
        
        Passage passage = new VerseRange(ref);
        vu = new VariationUnit(id, passage, overview, created, modified);
        
        return vu;
    }
    
    
    /* (non-Javadoc)
     */
    @Override
    public VariationUnit find(long id) {
        String sql = "SELECT " + FIELDS + ", vu_id " +
                     "  FROM NTTEXTComm_VUs " +
                     " WHERE vu_id = ?";
        
        VariationUnit vu = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, id);
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                vu = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve VU (" + id + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            vu = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return vu;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#lookup(openscriptures.ref.Passage)
     */
    @Override
    public VariationUnit find(Passage passage) {
        String sql = "SELECT " + FIELDS + ", vu_id " +
                     "  FROM NTTEXTComm_VUs " +
                     " WHERE passage = ?";
       
        VariationUnit vu = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, passage.toString());
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                vu = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve VU (" + passage + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            vu = null;
        } finally {
            repo.closeConnection(conn);
        }
       
        return vu;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#save(org.nttext.commentary.Entry)
     */
    @Override
    public boolean save(VariationUnit vu) {
        assert (vu.getId() != null) : "This VU has not been created.";
        if (vu.getId() == null)
            return false;
        
        int ID = 2, OVERVIEW = 1;
        String sql = "UPDATE NTTEXTComm_VUs SET " +
                     "  commentary = ?, " +
                     "  last_updated = now()" +
                     "WHERE vu_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(OVERVIEW, vu.getCommentary());
            stmt.setLong(ID, vu.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while saving a VU (" + vu.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create VU: " + vu.getPassage() + ". " + ex.getMessage();
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
    public boolean remove(VariationUnit vu) {
        assert (vu.getId() != null) : "This VU has not been created.";
        if (vu.getId() == null)
            return false;
        
        int ID = 1;
        String sql = "DELETE FROM NTTEXTComm_VUs " +
                     " WHERE vu_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(ID, vu.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while removing a VU (" + vu.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not remove VU: " + vu.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            success = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }

}
