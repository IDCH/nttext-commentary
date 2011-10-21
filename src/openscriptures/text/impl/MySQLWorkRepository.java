/**
 * 
 */
package openscriptures.text.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.idch.util.Cache;

import openscriptures.text.TokenRepository;
import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.text.WorkRepository;

/**
 * @author Neal_2
 */
public class MySQLWorkRepository implements WorkRepository {
    private final static Logger LOGGER = Logger.getLogger(MySQLWorkRepository.class);
    
    private final static int UUID        = 1;
    private final static int TITLE       = 2;
    private final static int ABBV        = 3;
    private final static int DESC        = 4;
    
    private final static int CREATOR     = 5;
    private final static int PUBLISHER   = 6;
    private final static int LANGUAGE    = 7;
    private final static int WORK_TYPE   = 8;
    private final static int RIGHTS      = 9;
    private final static int SCOPE       = 10;
    private final static int REF_SYSTEM  = 11;
    private final static int SOURCE_URL  = 12;
    
    private final static int PUB_DATE    = 13;
    private final static int IMPORT_DATE = 14;
    
    private final static int WORK_ID     = 15;
    
    private final static String FIELDS = 
            "uuid, title, abbreviation, description, " +
            "creator, publisher, language, work_type, copyright, scope, ref_system, soruce_url, " +
            "publication_date, import_date";
    
    MySQLTextRepository repo = null;
    
    private Cache<String, Work> worksByUUID = new Cache<String, Work>("works", 20);
    private Cache<Long, Work> worksById = new Cache<Long, Work>("works", 20);
    private Object cacheMutex = new Object();
    
    MySQLWorkRepository(MySQLTextRepository repo) {
        this.repo = repo;
    }
    

    /**
     * 
     */
    @Override
    public TokenRepository getTokenRepository() {
        return repo.getTokenRepository();
    }

    /**
     * 
     */
    @Override
    public Work create(String workId) {
        return create(new Work(new WorkId(workId)));
    }
    
    public Work create(Work w) {
        assert (w.getId() == null) : "This work has already been created.";
        if (w.getId() != null)
            return null;
        
        String sql = "INSERT INTO TEXTS_Works (" + FIELDS +") " +
        		     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            // build the statement
            conn = repo.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(UUID, w.getUUID().toString());
            stmt.setString(TITLE, w.getTitle());
            stmt.setString(ABBV, w.getAbbreviation());
            stmt.setString(DESC, w.getDescription());
            
            stmt.setString(CREATOR, w.getCreator());
            stmt.setString(PUBLISHER, w.getPublisher());
            stmt.setString(PUB_DATE, w.getPublicationDate());
            stmt.setString(LANGUAGE, w.getLgCode());
            stmt.setString(WORK_TYPE, w.getType());
            stmt.setString(RIGHTS, w.getCopyright());
            stmt.setString(SCOPE, w.getScope());
            stmt.setString(REF_SYSTEM, w.getRefSystem());
            stmt.setString(SOURCE_URL, w.getSourceUrl());
            
            Date d = w.getImportDate();
            if (d != null)
                stmt.setDate(IMPORT_DATE, new java.sql.Date(d.getTime()));
            else
                stmt.setDate(IMPORT_DATE, null);
            
            // execute the query
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                w.setId(id);
            }
            
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            
            String msg = "Could not create work: " + w.getWorkId() + ". " + ex.getMessage();
            LOGGER.warn(msg, ex);
            w = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return w;
    }

    private Work restore(Work w, ResultSet results) throws SQLException {
        w.setUUIDString(results.getString(UUID));
        w.setTitle(results.getString(TITLE));
        w.setAbbreviation(results.getString(ABBV));
        w.setDescription(results.getString(DESC));
        
        w.setCreator(results.getString(CREATOR));
        w.setPublisher(results.getString(PUBLISHER));
        w.setPublicationDate(results.getString(PUB_DATE));
        w.setLgCode(results.getString(LANGUAGE));
        w.setType(results.getString(WORK_TYPE));
        w.setCopyright(results.getString(RIGHTS));
        w.setScope(results.getString(SCOPE));
        w.setRefSystem(results.getString(REF_SYSTEM));
        w.setSourceUrl(results.getString(SOURCE_URL));
        w.setImportDate(results.getDate(IMPORT_DATE));
        
        return w;
    }
    
    
    public Work find(long id) {
        Work w = null;
        synchronized (cacheMutex) {
            w = worksById.get(id);
            if (w == null) {
                w = synchronize(new Work(id));
                
                if (w != null) {
                    worksByUUID.cache(w.getUUID().toString(), w);
                    worksById.cache(w.getId(), w);
                }
            }
        }
        
        return w;
    }
    
    public Work find(UUID id) {
        Work w = null;
        synchronized (cacheMutex) {
            w = worksByUUID.get(id.toString());
            if (w == null) {
                w = synchronize(new Work(id));
                
                if (w != null) {
                    worksByUUID.cache(w.getUUID().toString(), w);
                    worksById.cache(w.getId(), w);
                }
            }
        }
        
        return w;
    }
    
    /**
     * Synchronizes the state of the supplied work with the database. Any changes to the 
     * work will be overridden with the latest values from the database. 
     * 
     * @param w The work to be synchronized.
     * @return The synchronized work instance.
     */
    public Work synchronize(Work w) {
        String sql = null;
        String ident = null;
        boolean useUUID = false;
        if (w.getId() != null) {
            sql = "SELECT " + FIELDS + " FROM TEXTS_Works WHERE work_id = ?";
            ident = w.getId().toString();
        } else if (w.getUUID() != null) {
            ident = w.getUUID().toString();
            sql = "SELECT " + FIELDS + ", work_id FROM TEXTS_Works WHERE uuid = ?";
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
                stmt.setString(1, w.getUUID().toString());
            } else {
                stmt.setLong(1, w.getId());
            }
            
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                if (useUUID) 
                    w.setId(results.getLong(WORK_ID));
                
                w = restore(w, results);
            } else {
                w = null;
            }
        } catch (Exception ex) {
            String msg = "Could not retrieve work (" + ident + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            w = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return w;
    }
    
    
    public void save(Work work) {
        
    }
   

}
