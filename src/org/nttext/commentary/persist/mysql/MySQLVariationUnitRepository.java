/**
 * 
 */
package org.nttext.commentary.persist.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


import org.apache.log4j.Logger;
import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRange;
import org.nttext.commentary.VURepository;
import org.nttext.commentary.VariantReading;
import org.nttext.commentary.VariationUnit;

/**
 * @author Neal Audenaert
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
    
    private MySQLCommentaryModule repo = null;
    
    MySQLVariationUnitRepository(MySQLCommentaryModule repo) {
        this.repo = repo;
    }
    
    /* (non-Javadoc)
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
    
//    public VUReference createReference(VariationUnit vu, Token start, Token end) {
//        Work w = start.getWork();
//        StructureRepository structRepo = this.repo.getStructureRepository();
//
//        
//        Structure s = structRepo.create(w, VUReference.STRUCTURE_NAME, start, end);
//        VUReference ref = VUReference.init(s, vu);
//        boolean success = structRepo.save(ref);
//        
//        return success ? ref : null;
//    }
    
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
        
        Connection conn = results.getStatement().getConnection();
        MySQLVariantReadingRepository rdgRepo = 
                (MySQLVariantReadingRepository)this.repo.getRdgRepository();
        List<VariantReading> readings = rdgRepo.find(conn, vu);
        vu.setReadings(readings);
        
        return vu;
    }
    
    VariationUnit find(Connection conn, long id) throws SQLException {
        String sql = "SELECT " + FIELDS + ", vu_id " +
                     "  FROM NTTEXTComm_VUs " +
                     " WHERE vu_id = ?";
        
        VariationUnit vu = null;;
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setLong(1, id);
        
        ResultSet results = stmt.executeQuery();
        if (results.next())
            vu = restore(results);
        
        return vu;
    }
    
    /* (non-Javadoc)
     */
    @Override
    public VariationUnit find(long id) {
        VariationUnit vu = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            vu = find(conn, id);
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
     */
    @Override
    public boolean save(VariationUnit vu) {
        // Don't need to save changes to variant readings. These should only be 
        // manipulated via the ReadingRepository.
        
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
        // DB will cascade this deletion to the associate readings
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
