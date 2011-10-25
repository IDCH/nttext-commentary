/**
 * 
 */
package org.nttext.commentary.persist.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;

import org.apache.log4j.Logger;
import org.nttext.commentary.Entry;
import org.nttext.commentary.EntryRepository;

/**
 * @author Neal_2
 */
public class MySQLEntryRepository implements EntryRepository {
    private static final Logger LOGGER = Logger.getLogger(MySQLEntryRepository.class);

    private static final int PASSAGE = 1;
    private static final int OVERVIEW = 2;
    private static final int CREATED = 3;
    private static final int MODIFIED = 4;
    
    private static final int ENTRY_ID = 5;
    
    private static final String FIELDS =  
            "passage, overview, date_created, last_updated "; 
    
    private MySQLCommentaryRepo repo = null;
    
    MySQLEntryRepository(MySQLCommentaryRepo repo) {
        this.repo = repo;
    }
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#create(openscriptures.ref.Passage)
     */
    @Override
    public Entry create(Passage passage) {
        return this.create(new Entry(passage));
    }
   
    /**
     * 
     * @param entry
     * @return
     */
    public Entry create(Entry entry) {
        assert (entry.getId() == null) : "This entry has already been created.";
        if (entry.getId() != null)
            return null;
        
        String sql = "INSERT INTO NTTEXTComm_Entries (" + FIELDS +") " +
                     "VALUES (?, ?, now(), now())";
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(PASSAGE, entry.getPassage().toString());
            stmt.setString(OVERVIEW, entry.getOverview());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                entry.setId(id);
            } else {
                entry = null;
            }
            
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create entry: " + entry.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            entry = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return entry;
    }

    private Entry restore(ResultSet results) throws SQLException {
        Entry entry = null;
        
        long id = results.getLong(ENTRY_ID);
        // TODO add caching
        String ref = results.getString(PASSAGE);
        String overview = results.getString(OVERVIEW);
        Date created = results.getDate(CREATED);
        Date modified = results.getDate(MODIFIED);
        
        Passage passage = new VerseRange(ref);
        entry = new Entry(id, passage, overview, created, modified);
        
        return entry;
    }
    
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#find(long)
     */
    @Override
    public Entry find(long id) {
        String sql = "SELECT " + FIELDS + ", entry_id " +
                     "  FROM NTTEXTComm_Entries " +
        		     " WHERE entry_id = ?";
        
        Entry entry = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, id);
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                entry = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve entry (" + id + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            entry = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return entry;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#lookup(openscriptures.ref.Passage)
     */
    @Override
    public Entry find(Passage passage) {
        String sql = "SELECT " + FIELDS + ", entry_id " +
                     "  FROM NTTEXTComm_Entries " +
                     " WHERE passage = ?";
       
        Entry entry = null;;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, passage.toString());
            
            ResultSet results = stmt.executeQuery();
            if (results.next())
                entry = restore(results);
        } catch (Exception ex) {
            String msg = "Could not retrieve entry (" + passage + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            entry = null;
        } finally {
            repo.closeConnection(conn);
        }
       
        return entry;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#save(org.nttext.commentary.Entry)
     */
    @Override
    public boolean save(Entry entry) {
        assert (entry.getId() != null) : "This entry has not been created.";
        if (entry.getId() == null)
            return false;
        
        int ID = 2, OVERVIEW = 1;
        String sql = "UPDATE NTTEXTComm_Entries SET " +
        		     "  overview = ?, " +
        		     "  last_updated = now()" +
        		     "WHERE entry_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(OVERVIEW, entry.getOverview());
            stmt.setLong(ID, entry.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while saving an entry (" + entry.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create entry: " + entry.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            success = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }

    /* (non-Javadoc)
     * @see org.nttext.commentary.persist.EntryRepository#remove(org.nttext.commentary.Entry)
     */
    @Override
    public boolean remove(Entry entry) {
        assert (entry.getId() != null) : "This entry has not been created.";
        if (entry.getId() == null)
            return false;
        
        int ID = 1;
        String sql = "DELETE FROM NTTEXTComm_Entries " +
                     " WHERE entry_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(ID, entry.getId());
           
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = (numRowsChanged == 1);
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while removing an entry (" + entry.getId() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not remove entry: " + entry.getPassage() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            success = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }
}
