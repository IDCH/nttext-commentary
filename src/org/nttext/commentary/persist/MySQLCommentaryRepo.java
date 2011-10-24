/**
 * 
 */
package org.nttext.commentary.persist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.idch.persist.DBBackedRepository;
import org.idch.persist.DatabaseException;
import org.idch.persist.RepositoryAccessException;

/**
 * @author Neal Audenaert
 */
public class MySQLCommentaryRepo extends DBBackedRepository implements CommentaryRepo {

public final static String MODULE_NAME = "nttext_commentary";
    
    /** 
     * 
     * 
     * @return
     * @throws RepositoryAccessException
     */
    public static final MySQLCommentaryRepo get()
            throws RepositoryAccessException {
        return (MySQLCommentaryRepo)get(MODULE_NAME);
    }
    
    private EntryRepository entryRepo; 

    public MySQLCommentaryRepo() {
        entryRepo = new MySQLEntryRepository(this);
    }
    
    /**
     * @return
     */
    public EntryRepository getEntryRepository() {
        return entryRepo;
    }
    
    //========================================================================
    // DATABASE MANIPULATION METHODS
    //========================================================================
    public Connection openConnection() throws SQLException, DatabaseException {
        return super.openTransaction();
    }
    
    public Connection openReadOnlyConnection() throws SQLException, DatabaseException {
        return super.openReadOnly();
    }

    public void rollbackConnection(Connection conn) {
        super.rollback(conn);
    }
    
    public void closeConnection(Connection conn) {
        super.close(conn);
    }

    /**
     * Attempts to determine whether or not the proper tables are defined for 
     * use by the <code>PropertyRepository</code>. 
     * 
     * @return <code>true</code> if the required tables are defined, 
     *      <code>false</code> if they are not.
     */
    public boolean probe() {
        List<String> sql = new ArrayList<String>(3);
        sql.add("SELECT entry_id FROM NTTEXTComm_Entries;");
        sql.add("SELECT vu_id FROM NTTEXTComm_Vus;");
        sql.add("SELECT vu_id FROM NTTEXTComm_VUReference;");
        sql.add("SELECT rdg_id FROM NTTEXTComm_Rdgs;");
        sql.add("SELECT entry_id, vu_id FROM NTTEXTComm_EntryVUs;");
        
        sql.add("SELECT entry_id FROM NTTEXTComm_Entries;");
        
        sql.add("SELECT structure_id FROM TEXTS_Structures");
        return probe(sql);
    }

   

}
