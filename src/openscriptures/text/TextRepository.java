/**
 * 
 */
package openscriptures.text;

import java.util.List;

/**
 * @author Neal Audenaert
 */
public interface TextRepository {

    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository();
    
    public StructureRepository getStructureRepository();
    
    public Work getWork(TokenSequence s);
    public List<Token> getTokens(TokenSequence s);
    public String toString(List<Token> tokens);
    public String toString(TokenSequence s);
    public Token getTokenAt(TokenSequence s, int index);
    public Token getTokenAt(List<Token> tokens, int index);
}
