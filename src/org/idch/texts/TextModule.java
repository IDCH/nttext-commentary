/**
 * 
 */
package org.idch.texts;

import java.util.List;

/**
 * @author Neal Audenaert
 */
public interface TextModule {

    public WorkRepository getWorkRepository(); 
    
    public TokenRepository getTokenRepository();
    
    public StructureRepository getStructureRepository();
    
    public Work getWork(TokenSequence s);
    public List<Token> getTokens(TokenSequence s);
    public Structure createStructure(TokenSequence seq, String name, String match);
    public Structure createStructure(TokenSequence seq, String name, String match, String prefix);
    public Structure createStructure(TokenSequence seq, String name, String match, int ct);
    public List<Structure> createStructures(TokenSequence seq, String name, String match);
    public String toString(List<Token> tokens);
    public String toString(TokenSequence s);
    public Token getTokenAt(TokenSequence s, int index);
    public Token getTokenAt(List<Token> tokens, int index);
}
