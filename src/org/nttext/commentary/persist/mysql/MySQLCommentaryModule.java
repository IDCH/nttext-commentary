/**
 * 
 */
package org.nttext.commentary.persist.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import openscriptures.text.StructureRepository;
import openscriptures.text.TextModule;
import openscriptures.text.TextModuleInstance;
import openscriptures.text.TokenRepository;
import openscriptures.text.WorkRepository;

import org.idch.persist.DBBackedRepository;
import org.idch.persist.DatabaseException;
import org.idch.persist.RepositoryAccessException;
import org.nttext.commentary.CommentaryModule;
import org.nttext.commentary.EntryRepository;
import org.nttext.commentary.VURepository;
import org.nttext.commentary.VariantReadingRepository;

/**
 * @author Neal Audenaert
 */
public class MySQLCommentaryModule extends DBBackedRepository implements CommentaryModule {

public final static String MODULE_NAME = "nttext_commentary";
    
    /** 
     * 
     * 
     * @return
     * @throws RepositoryAccessException
     */
    public static final MySQLCommentaryModule get()
            throws RepositoryAccessException {
        return (MySQLCommentaryModule)get(MODULE_NAME);
    }
    
    private EntryRepository entryRepo;
    private VURepository vuRepo; 
    private VariantReadingRepository rdgRepo;
    
    private TextModule textRepo;

    public MySQLCommentaryModule() throws RepositoryAccessException {
        entryRepo = new MySQLEntryRepository(this);
        vuRepo = new MySQLVariationUnitRepository(this);
        rdgRepo = new MySQLVariantReadingRepository(this);
        
        // connect to texts repository.
        textRepo = TextModuleInstance.get();
    }
    
    /**
     * @return
     */
    public EntryRepository getEntryRepository() {
        return entryRepo;
    }
    
    public VURepository getVURepository() {
        return vuRepo;
    }
    
    public VariantReadingRepository getRdgRepository() {
        return rdgRepo;
    }
    
    public TextModule getTextRepository() {
        return this.textRepo;
    }
    
    public WorkRepository getWorkRepository() { 
        return textRepo.getWorkRepository(); 
    } 
    
    public TokenRepository getTokenRepository() { 
        return textRepo.getTokenRepository(); 
    }
    
    public StructureRepository getStructureRepository() { 
        return textRepo.getStructureRepository(); 
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
