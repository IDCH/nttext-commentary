/**
 * 
 */
package org.nttext.commentary.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nttext.commentary.VariantReading;
import org.nttext.commentary.VariationUnit;

/**
 * @author Neal Audenaert
 */
public class MySQLVariantReadingRepository implements VariantReadingRepository {
    private static final Logger LOGGER = Logger.getLogger(MySQLEntryRepository.class);

    private static final int VU_ID = 1;
    private static final int GREEK_RDG = 2;
    private static final int ENGLISH_RDG = 3;
    private static final int WITNESSES = 4;
    @SuppressWarnings("unused")
    private static final int SEQ_NO = 5;
    
    private static final int RDG_ID = 6;
    
    private static final String FIELDS =  
                "vu_id, greek_rdg, english_rdg, witnesses, seq_no ";        // , rdg_id
    
    MySQLCommentaryRepo repo = null;
    
    MySQLVariantReadingRepository(MySQLCommentaryRepo repo) {
        this.repo = repo;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.VariantReadingRepository#create(org.nttext.commentary.VariationUnit)
     */
    @Override
    public VariantReading create(VariationUnit vu) {
        return create(new VariantReading(vu));
    }

    @Override
    public VariantReading create(VariationUnit vu, String english, String greek) {
        return create(new VariantReading(vu, english, greek));
    }
    
    /**
     * 
     * @param conn
     * @param rdg
     * @return
     * @throws SQLException
     */
    private VariantReading create(Connection conn, VariantReading rdg) throws SQLException {
        assert (rdg.getId() == null) : "This reading has already been created.";
        if (rdg.getId() != null)
            return null;
        
        int ID = 5;
        String sql = "INSERT INTO NTTEXTComm_Rdgs (" + FIELDS + ") " +
                     "SELECT ?, ?, ?, ?, COALESCE(max(seq_no), -1) + 1 " +
                     "  FROM NTTEXTComm_Rdgs" +
                     " WHERE vu_id = ?";
        
        PreparedStatement stmt = conn.prepareStatement(sql, 
                PreparedStatement.RETURN_GENERATED_KEYS);
        
        long vuId = rdg.getVariationUnit().getId();
        stmt.setLong(VU_ID, vuId);
        stmt.setString(GREEK_RDG, rdg.getGreekReading());
        stmt.setString(ENGLISH_RDG, rdg.getEnglishReading());
        stmt.setString(WITNESSES, rdg.getWitnessDescription());
        
        stmt.setLong(ID, vuId);
       
        // execute the query
        int numRowsChanged = stmt.executeUpdate();
        ResultSet results = stmt.getGeneratedKeys();
        if (numRowsChanged == 1 && results.next()) {
            long id = results.getLong(1);
            rdg.setId(id);
            
            // add the reading to the VU.
            VariationUnit vu = rdg.getVariationUnit();
            vu.addReading(rdg);
        } else {
            rdg = null;
        }
        
        return rdg;
    }
    
    public VariantReading create(VariantReading rdg) {
        Connection conn = null;
        try {
            conn = repo.openConnection();
            rdg = create(conn, rdg);
            if (rdg != null) {
                conn.commit();
            } else {
                repo.rollbackConnection(conn);
            }
            
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            String msg = "Could not create variant reading for VU: " + 
                        rdg.getVariationUnit().getId() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            rdg = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return rdg;
    }
    
    private VariantReading restore(VariantReading rdg, ResultSet results) throws SQLException {
        long vuId = results.getLong(VU_ID);
        if (!rdg.getVariationUnit().getId().equals(vuId)) {
            return null;
        }
        
        rdg.setEnglishReading(results.getString(ENGLISH_RDG));
        rdg.setGreekReading(results.getString(GREEK_RDG));
        rdg.setWitnessDescription(results.getString(WITNESSES));
        
        return rdg;
    }
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.VariantReadingRepository#find(org.nttext.commentary.VariationUnit)
     */
    @Override
    public List<VariantReading> find(VariationUnit vu) {
        String sql = "SELECT " + FIELDS + ", rdg_id " +
                     "  FROM NTTEXTComm_Rdgs " +
                     " WHERE vu_id = ?" +
                     " ORDER BY seq_no ASC";

        List<VariantReading> readings = new ArrayList<VariantReading>();
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, vu.getId());

            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                long id = results.getLong(RDG_ID);
                VariantReading rdg = new VariantReading(id, vu);
                rdg = restore(rdg, results);
                if (rdg != null) {
                    readings.add(rdg);
                } else {
                    LOGGER.warn("Failed to restore reading: " + id);
                    readings.clear();
                    break;
                }

            }
        } catch (Exception ex) {
            String msg = "Could not retrieve variant readings for (" + vu.getId() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            readings.clear();
        } finally {
            repo.closeConnection(conn);
        }

        return readings;
    }
    
    public VariantReading synchronize(VariantReading rdg) {
        String sql = "SELECT " + FIELDS + ", rdg_id " +
                    "  FROM NTTEXTComm_Rdgs " +
                    " WHERE rdg_id = ?";
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, rdg.getId());

            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                rdg = restore(rdg, results);
            } else {
                rdg = null;
            }
        } catch (Exception ex) {
            String msg = "Could not synchronize variant readings (" + rdg.getId() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            rdg = null;
        } finally {
            repo.closeConnection(conn);
        }

        return rdg;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.VariantReadingRepository#save(org.nttext.commentary.VariantReading)
     */
    @Override
    public boolean save(VariantReading rdg) {
        assert (rdg.getId() != null) : "This reading has already been created.";
        if (rdg.getId() == null)
            return false;
        
        int ENGLISH = 1, GREEK = 2, WITNESSES = 3, ID = 4;
        String sql = "UPDATE NTTEXTComm_Rdgs SET " +
                     "  english_rdg = ?, " +
                     "  greek_rdg = ?," +
                     "  witnesses = ?" +
                     " WHERE rdg_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(GREEK, rdg.getGreekReading());
            stmt.setString(ENGLISH, rdg.getEnglishReading());
            stmt.setString(WITNESSES, rdg.getWitnessDescription());
            
            stmt.setLong(ID, rdg.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = numRowsChanged == 1;
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while saving a reading (" + rdg.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            String msg = "Could not create variant reading for VU: " + 
                        rdg.getVariationUnit().getId() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            rdg = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.VariantReadingRepository#remove(org.nttext.commentary.VariantReading)
     */
    @Override
    public boolean remove(VariantReading rdg) {
        // TODO Auto-generated method stub
        return false;
    }

}
