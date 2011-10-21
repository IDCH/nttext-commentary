/**
 * 
 */
package openscriptures.text.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.StructureRepository;
import openscriptures.text.TextRepo;
import openscriptures.text.Token;
import openscriptures.text.TokenRepository;
import openscriptures.text.Work;
import openscriptures.text.WorkRepository;

import org.idch.persist.DBBackedRepository;
import org.idch.persist.DatabaseException;
import org.idch.persist.RepositoryAccessException;

/**
 * @author Neal_2
 */
public class MySQLTextRepository extends DBBackedRepository implements TextRepo {

    public final static String MODULE_NAME = "nttext";
    
    /** 
     * 
     * 
     * @return
     * @throws RepositoryAccessException
     */
    public static final MySQLTextRepository get()
            throws RepositoryAccessException {
        return (MySQLTextRepository)get(MODULE_NAME);
    }
    
    private WorkRepository works = new MySQLWorkRepository(this);
    private TokenRepository tokens = new MySQLTokenRepository(this);;
    private StructureRepository structures = new MySQLStructureRepository(this);;
    
    public WorkRepository getWorkRepository() {
        return this.works;
    }
    
    public TokenRepository getTokenRepository() {
        return this.tokens;
    }
    
    public StructureRepository getStructureRepository() {
        return this.structures;
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
        sql.add("SELECT work_id" +
                "  FROM TEXTS_Works;");
        sql.add("SELECT token_id  " +
                "  FROM TEXTS_Tokens");
        sql.add("SELECT structure_id" +
                "  FROM TEXTS_Structures");
        return probe(sql);
    }
   
    

}
