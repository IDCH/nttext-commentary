/**
 * 
 */
package org.nttext.commentary.persist.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.idch.persist.DBBackedRepository;
import org.idch.persist.DatabaseException;
import org.idch.persist.RepositoryAccessException;
import org.idch.texts.StructureRepository;
import org.idch.texts.TextModule;
import org.idch.texts.TextModuleInstance;
import org.idch.texts.TokenRepository;
import org.idch.texts.WorkRepository;
import org.nttext.commentary.CommentaryModule;
import org.nttext.commentary.InstanceRepository;
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
    
    private InstanceRepository instanceRepo;
    private VURepository vuRepo; 
    private VariantReadingRepository rdgRepo;
    
    private TextModule textRepo;

    public MySQLCommentaryModule() throws RepositoryAccessException {
        instanceRepo = new MySQLEntryInstanceRepository(this);
        vuRepo = new MySQLVariationUnitRepository(this);
        rdgRepo = new MySQLVariantReadingRepository(this);
        
        // connect to texts repository.
        textRepo = TextModuleInstance.get();
    }
    
    /**
     * @return
     */
    public InstanceRepository getInstanceRepository() {
        return instanceRepo;
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
        sql.add("SELECT instance_id FROM nttextcomm_instances;");
        sql.add("SELECT vu_id FROM nttextcomm_vus;");
        sql.add("SELECT vu_id FROM nttextcomm_vureference;");
        sql.add("SELECT rdg_id FROM nttextcomm_rdgs;");
        sql.add("SELECT instance_id, vu_id FROM _entryvus;");
        
        
        sql.add("SELECT structure_id FROM texts_structures");
        return probe(sql);
    }

   

}
