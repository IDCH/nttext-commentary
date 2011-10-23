/**
 * 
 */
package openscriptures.text.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.idch.util.StopWatch;

import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.TokenRepository;
import openscriptures.text.Work;
import openscriptures.text.WorkRepository;

/**
 * @author Neal_2
 */
public class MySQLTokenRepository implements TokenRepository {
    private final static Logger LOGGER = Logger.getLogger(MySQLTokenRepository.class);
    
    MySQLTextRepository repo = null;
    
    private final static int ID = 1;
    private final static int UUID = 2;
    private final static int WORK_ID = 3;
    private final static int POS = 4;
    private final static int TEXT = 5;
    private final static int TYPE = 6;
    
    private final static String FIELDS = 
            "token_id, uuid, work_id, token_pos, token_text, token_type";
    
    private static final String CREATE_SQL = 
            "INSERT INTO TEXTS_Tokens (uuid, work_id, token_pos, token_text, token_type) " +
                    "VALUES (?, ?, ?, ?, ?)";
    
    MySQLTokenRepository(MySQLTextRepository repo) {
        this.repo = repo;
    }

    private int nextTokenId(Connection conn, long wId) throws SQLException {
        int id = -1;
        String sql = "SELECT COALESCE(MAX(token_pos), -1) + 1 " +
                "  FROM TEXTS_Tokens" +
                " WHERE work_id = " + wId;
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(sql);
        if (results.next())
            id = results.getInt(1);
        
        return id;
    }
    
    private long getWorkId(Work w) {
        Long wId = w.getId();
        if (wId == null) {
            throw new RuntimeException("Invalid work: no ID specified.");
        }
        
        return wId;
    }
    /** (non-Javadoc)
     * @see openscriptures.text.TokenRepository#create(openscriptures.text.Token)
     */
    @Override
    public Token create(Token t) {

        Long wId = getWorkId(t.getWork());
        Connection conn = null;
        try {
            conn = repo.openConnection();

//            int pos = nextTokenId(conn, wId);
//            t.setPosition(pos);
            
            PreparedStatement stmt = conn.prepareStatement(CREATE_SQL, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, t.getUUID().toString());
            stmt.setLong(2, wId);
            stmt.setLong(3, t.getPosition());
            stmt.setString(4, t.getText());
            stmt.setString(5, t.getType().toString());
            
            int numRowsChanged = stmt.executeUpdate();
            ResultSet results = stmt.getGeneratedKeys();
            if (numRowsChanged == 1 && results.next()) {
                long id = results.getLong(1);
                t.setId(id);
            }
            
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            String msg = "Could not create token: " + t.getUUID() + ". " + ex.getMessage();
        
            LOGGER.warn(msg, ex);
            t = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return t;
    }
    
    StopWatch timer = new StopWatch("tokens", 1000);

    /* (non-Javadoc)
     * @see openscriptures.text.TokenRepository#create(java.util.List)
     */
    @Override
    public List<Token> create(List<Token> tokens) {
        if ((tokens == null) || (tokens.size() == 0)) {
            return tokens;
        }
        timer.start();
        Long wId = getWorkId(tokens.get(0).getWork());
        Connection conn = null;
        try {
            conn = repo.openConnection();
//            int pos = nextTokenId(conn, wId);

            PreparedStatement stmt = conn.prepareStatement(CREATE_SQL, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(2, wId);
            
            for (Token t : tokens) {
                if (t.getId() != null) {
                    continue;   // FIXME already created - something's wrong here. 
                }
                
//                t.setPosition(pos++);

                stmt.setString(1, t.getUUID().toString());
                stmt.setLong(3, t.getPosition());
                stmt.setString(4, t.getText());
                stmt.setString(5, t.getType().toString());

                int numRowsChanged = stmt.executeUpdate();
                ResultSet results = stmt.getGeneratedKeys();
                if (numRowsChanged == 1 && results.next()) {
                    long id = results.getLong(1);
                    t.setId(id);
                }
            }
            conn.commit();
        } catch (Exception ex) {
            repo.rollbackConnection(conn);
            String msg = "Could not create tokens: " + ex.getMessage();
        
            LOGGER.warn(msg, ex);
            tokens = null;      // should throw exception
        } finally {
            repo.closeConnection(conn);
        }
        
        timer.pause();
        return tokens;
    }

    /**
     * Restores a token from the provide result set. This assumes that the result set 
     * cursor is positioned at the row to be restored. This will not update the cursor.
     * 
     * @param results The result set to use to restore the token.
     * @return The restored token.
     * @throws SQLException if there are problems accessing the result set.
     */
    private Token restore(ResultSet results) throws SQLException {
        long id = results.getLong(ID);
        long workId = results.getLong(WORK_ID);
        int pos = results.getInt(POS);
        String uuid = results.getString(UUID);
        String text = results.getString(TEXT);
        String type = results.getString(TYPE);
        
        WorkRepository works = repo.getWorkRepository();
        Work w = works.find(workId);
        
        Token token = new Token(w, pos, text);
        token.setId(id);
        token.setUUIDString(uuid);
        token.setType(Token.Type.valueOf(type));
        
        return token;
    }
    
    /* (non-Javadoc)
     * @see openscriptures.text.TokenRepository#getMaxPosition(openscriptures.text.Work)
     */
    @Override
    public int getNumberOfTokens(Work work) {
        Long wId = getWorkId(work);
        int pos = 0;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            pos = nextTokenId(conn, wId);
        } catch (Exception ex) {
            String msg = "Could not retrieve max position for work (id=" + wId + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            pos = -1;
        } finally {
            repo.closeConnection(conn);
        }
        
        return pos;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenRepository#find(openscriptures.text.Work, int)
     */
    @Override
    public Token find(Work w, int pos) {
        Long wId = getWorkId(w);
        String sql = "SELECT " + FIELDS + 
                     "  FROM TEXTS_Tokens" +
                     " WHERE work_id = " + wId + " AND token_pos = " + pos;
        
        Token token = null;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);
            if (results.next()) {
                long id = results.getLong(ID);
                String uuidStr = results.getString(UUID);
                String text = results.getString(TEXT);
                String type = results.getString(TYPE);
                
                token = new Token(w, pos, text);
                token.setId(id);
                token.setUUIDString(uuidStr);
                token.setType(Token.Type.valueOf(type));
            }
        } catch (Exception ex) {
            String msg = "Could not retrieve token " +
            		"(workId=" + wId + ", pos=" + pos + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            token = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return token;
    }
    
    public Token find(UUID uuid) {
        String sql = "SELECT " + FIELDS + "FROM TEXTS_Tokens WHERE uuid = ?"; 
        
        Token token = null;
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid.toString());
            
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                token = restore(results);
            }
        } catch (Exception ex) {
            String msg = "Could not retrieve token (" + uuid.toString() + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            token = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return token;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenRepository#find(openscriptures.text.Work, int, int)
     */
    @Override
    public List<Token> find(Work w, int start, int end) {
        String sql = "SELECT " + FIELDS + "FROM TEXTS_Tokens WHERE uuid = ?"; 
        
        List<Token> tokens = new ArrayList<Token>(end - start);
        Connection conn = null;
        try {
            conn = repo.openReadOnlyConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                tokens.add(restore(results));
            }
        } catch (Exception ex) {
            String msg = "Could not retrieve tokens (" + "): " + ex.getMessage();
            LOGGER.warn(msg, ex);
            tokens = null;
        } finally {
            repo.closeConnection(conn);
        }
        
        return tokens;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenRepository#find(openscriptures.text.Structure)
     */
    @Override
    public List<Token> find(Structure s) {
        // TODO Auto-generated method stub
        return null;
    }
}
