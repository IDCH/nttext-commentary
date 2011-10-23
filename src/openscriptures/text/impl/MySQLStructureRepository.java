/**
 * 
 */
package openscriptures.text.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.apache.log4j.Logger;

import openscriptures.text.Structure;
import openscriptures.text.StructureRepository;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal_2
 */
public class MySQLStructureRepository implements StructureRepository {
    
    //===================================================================================
    // SYMBOLIC CONSTANTS
    //===================================================================================
    private final static Logger LOGGER = Logger.getLogger(MySQLStructureRepository.class);
    
    private static final int S_UUID       = 1;
    private static final int WORK_UUID    = 2;
    private static final int NAME         = 3;
    private static final int PERSPECTIVE  = 4;
    private static final int START        = 5;
    private static final int END          = 6;
    
    private static final int STRUCTURE_ID = 7;
    
    private static final String FIELDS =
            "uuid, work_uuid, structure_name, perspective, " + 
            "start_pos, end_pos ";
    
    //===================================================================================
    // STATIC METHODS
    //===================================================================================
    
    /**
     * 
     * @param stmt
     * @param param
     * @param value
     * @throws SQLException
     */
    public static void setParameter(PreparedStatement stmt, int param, Integer value) throws SQLException {
        if (value != null) {
            stmt.setInt(param, value);
        } else { 
            stmt.setNull(param, Types.INTEGER);
        }
    }
    
    public static Integer getParameter(ResultSet results, int param, Class<Integer> clazz) 
            throws SQLException {
        Integer result = results.getInt(param);
        if (results.wasNull()) {
            result = null;
        }
        
        return result;
    }
    
    //===================================================================================
    // MEMBER VARIABLES
    //===================================================================================
    
    MySQLTextRepository repo = null;
    
    //===================================================================================
    // CONSTRUCTORS
    //===================================================================================
    
    /**
     * 
     * @param repo
     */
    MySQLStructureRepository(MySQLTextRepository repo) {
        this.repo = repo;
    }

    //===================================================================================
    // METHOS
    //===================================================================================

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String)
     */
    @Override
    public Structure create(Work work, String name) {
        Structure s = new Structure(work, name);
        return create(s);
    }

    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#create(openscriptures.text.Work, java.lang.String, openscriptures.text.Token, openscriptures.text.Token)
     */
    @Override
    public Structure create(Work work, String name, Token start, Token end) {
        Structure s = new Structure(work, name, start, end);
        return create(s);
    }

    
    public Structure create(Structure s) {
        assert (s.getId() == null) : "This structure has already been created.";
        if (s.getId() != null)
            return null;
        
        String sql = "INSERT INTO TEXTS_Structures (" + FIELDS +") " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(S_UUID, s.getUUID().toString());
            stmt.setString(WORK_UUID, s.getWorkUUID().toString());
            stmt.setString(NAME, s.getName());
            stmt.setString(PERSPECTIVE, s.getPerspective());
            setParameter(stmt, START, s.getStartTokenPosition());
            setParameter(stmt, END, s.getEndTokenPosition());
            
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                s.setId(id);
            }
            
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create structure: " + s.getName() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            s = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return s;
    }
    
    /* (non-Javadoc)
     * @see openscriptures.text.StructureRepository#hasStructuresFor(openscriptures.text.Work)
     */
    @Override
    public boolean hasStructuresFor(UUID workId) {
        String sql = "SELECT structure_id FROM TEXTS_Structures WHERE work_uuid = ? LIMIT 1";

        boolean hasStructures = false;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, workId.toString());
            ResultSet results = stmt.executeQuery();
            hasStructures = results.next(); 
        } catch (Exception ex) {
            String msg = "Failed to determine if structures exist for the specified work " +
            		"(" + workId.toString() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            hasStructures = false;
        } finally {
            repo.closeConnection(conn);
        }
        
        return hasStructures;
    }
    
    public Structure find(long id) {
        return synchronize(new Structure(id));
    }
    
    public Structure find(UUID id) {
        return synchronize(new Structure(id));
    }
    
    private Structure restore(Structure s, ResultSet results) throws SQLException {
        s.setUUIDString(results.getString(S_UUID));
        s.setName(results.getString(NAME));
        s.setPerspective(results.getString(PERSPECTIVE));
        s.setStartTokenPosition(getParameter(results, START, Integer.class));
        s.setEndTokenPosition(getParameter(results, END, Integer.class));
        
        String wId = results.getString(WORK_UUID);
        assert wId != null : "No identifier for this token's work.";         // should be enforced by DB constraints
        s.setWork(UUID.fromString(wId));

        return s;
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public Structure synchronize(Structure s) {
        String sql = null;
        String ident = null;
        boolean useUUID = false;
        if (s.getId() != null) {
            sql = "SELECT " + FIELDS + " FROM TEXTS_Structures WHERE structure_id = ?";
            ident = s.getId().toString();
        } else if (s.getUUID() != null) {
            ident = s.getUUID().toString();
            sql = "SELECT " + FIELDS + ", structure_id FROM TEXTS_Structures WHERE uuid = ?";
            useUUID = true;
        } else {
            String msg = "Could not retrieve work. No identifier supplied.";
            LOGGER.warn(msg);
            return null;
        }
        
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (useUUID) {
                stmt.setString(1, s.getUUID().toString());
            } else {
                stmt.setLong(1, s.getId());
            }
            
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                if (useUUID) 
                    s.setId(results.getLong(STRUCTURE_ID));
                
                s = restore(s, results);
            } else {
                s = null;
            }
        } catch (Exception ex) {
            String msg = "Could not retrieve structure (" + ident + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            s = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return s;
    }

    /**
     * 
     * @see openscriptures.text.StructureRepository#save(openscriptures.text.Structure)
     */
    @Override
    public boolean save(Structure s) {
        int NAME = 1, PERSPECTIVE = 2, START = 3, END = 4, ID = 5;
        String sql = 
                "UPDATE TEXTS_Structures SET " +
                "    structure_name = ?, " +
                "    perspective = ?, " + 
                "    start_pos = ?, " + 
                "    end_pos = ?, " +
                "WHERE structure_id = ?";
        
        boolean success = false;
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql); 
            
            stmt.setString(NAME, s.getName());
            stmt.setString(PERSPECTIVE, s.getPerspective());
            setParameter(stmt, START, s.getStartTokenPosition());
            setParameter(stmt, END, s.getEndTokenPosition());
            stmt.setLong(ID, s.getId());

            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            success = numRowsChanged == 1;
            if (numRowsChanged > 1) {
                LOGGER.warn("Bizarre number of rows changed (" + numRowsChanged + ") " + 
                            "while saving a structure (" + s.getUUID() + "). Expected 1.");
                repo.rollbackConnection(conn);
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create structure: " + s.getName() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            s = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return success;
    }
}
