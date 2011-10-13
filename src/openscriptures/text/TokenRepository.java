/**
 * 
 */
package openscriptures.text;

import java.util.List;
import java.util.UUID;


/**
 * @author Neal Audenaert
 */
public interface TokenRepository {
    
    public Token append(Work w, String value);
    
    public List<Token> appendAll(Work w, String text);
    
    public int getMaxPosition(Work work);
    
    public Token find(UUID id);
    
    public Token find(Work w, int pos);
    
    public List<Token> find(Work w, int start, int end);
    
    public List<Token> find(Structure s);
    
    public void save(Token t);
}
