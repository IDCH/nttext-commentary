/**
 * 
 */
package org.idch.texts.persist.mem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.idch.texts.Structure;
import org.idch.texts.StructureRepository;
import org.idch.texts.TextModule;
import org.idch.texts.Token;
import org.idch.texts.TokenRepository;
import org.idch.texts.TokenSequence;
import org.idch.texts.Work;
import org.idch.texts.WorkRepository;

/**
 * @author Neal Audenaert
 */
public class MemTextModule implements TextModule {

    private WorkRepository m_worksRepo = new MemWorkRepository(this);
    private TokenRepository m_tokensRepo = new MemTokenRepository(this);
    private StructureRepository m_structuresRepo = new MemStructureRepository(this);
    
    //========================================================================
    // REPOSITORY GETTERS
    //========================================================================
    public WorkRepository getWorkRepository() {
        return this.m_worksRepo;
    }
    
    public TokenRepository getTokenRepository() {
        return this.m_tokensRepo;
    }
    
    public StructureRepository getStructureRepository() {
        return this.m_structuresRepo;
    }
    
    //========================================================================
    // UTILITY METHODS FOR INTERACTING WITH STRUCTURES
    //========================================================================

    /**
     * Attempts to return the work associated with the provided structure. Note that, 
     * since structures provide remote standoff markup, this module may not have access
     * to the repository in which the associated work is stored.
     * 
     * @param s
     * @return
     */
    public Work getWork(TokenSequence s) {
        UUID uuid = s.getWorkUUID();
        Work w = m_worksRepo.find(uuid);
        if (w == null) {
            // throw new InaccessibleWorkException(uuid)
        }
        
        return w;
    }
    
    /**
     * Creates a structure in the provided sequence that matches the first occurrence of 
     * some text.
     * 
     * @param seq The token sequence from which to create the structure.
     * @param match The text to match
     * @param name The name of the structure to create.
     * @return
     */
    public List<Structure> createStructures(TokenSequence seq, String match, String name) {
        String seqText = toString(seq);
        
        List<Structure> structures = new ArrayList<Structure>();
        int ix = seqText.indexOf(match);
        while (ix >= 0) {
            Token start = getTokenAt(seq, ix);
            Token end = getTokenAt(seq, ix + match.length() - 1);
            Structure s = new Structure(getWork(seq), name, start, end);
            structures.add(s);
            
            ix = seqText.indexOf(match, ix + 1);
        }
        
        return structures;
    }
    
    
    /**
     * Creates a structure in the provided sequence that matches the first occurrence of 
     * some text.
     * 
     * @param seq The token sequence from which to create the structure.
     * @param match The text to match
     * @param name The name of the structure to create.
     * @return
     */
    public Structure createStructure(TokenSequence seq, String match, String name) {
        String seqText = toString(seq);
        
        int ix = seqText.indexOf(match);
        Token start = getTokenAt(seq, ix);
        Token end = getTokenAt(seq, ix + match.length() - 1);
        Structure s = new Structure(getWork(seq), name, start, end);
        
        return s;
    }
    
    /** 
     * Returns the tokens associate with a particular structure. 
     * 
     * @param s The structure for which to return the tokens.
     * @return The list of tokens belonging to this particular structure.
     */
    public List<Token> getTokens(TokenSequence s) {
        return m_tokensRepo.find(getWork(s), s.getStart(), s.getEnd());
    }

    /**
     * 
     * @param tokens
     * @return
     */
    public String toString(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            sb.append(t.getText());
        }
        
        return sb.toString();
    }
    
    public String toString(TokenSequence s) {
        return toString(getTokens(s));
    }
    
    
    public Token getTokenAt(TokenSequence s, int index) {
        return getTokenAt(getTokens(s), index);
    }
    
    /**
     * Returns the token in the specified list having the 
     * 
     * @param tokens
     * @param index
     * @return
     */
    public Token getTokenAt(List<Token> tokens, int index) {
        int ix = 0;
        
        Token token = null;
        for (Token t : tokens) {
            ix += t.getText().length();
            if (ix > index) {
                token = t; 
                break;
            }
        }
        
        return token;
    }
}
