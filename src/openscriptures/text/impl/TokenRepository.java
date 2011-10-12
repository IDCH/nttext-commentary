/**
 * 
 */
package openscriptures.text.impl;

import java.util.List;
import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public interface TokenRepository {
    
    public Token createToken(Work w, String value);
    
    public Token createToken(Work w, String value, int pos);
    
    public List<Token> tokenize(Work w, String text);
    
    public List<Token> tokenize(Work w, String text, int pos); 
    
    public int getMaxPosition(Work work);
    
    public Token find(UUID id);
    
    public Token find(Work w, int pos);
    
    public List<Token> find(Work w, int start, int end);
    
    public List<Token> find(Structure s);
    
    public void save(Token t);
}
