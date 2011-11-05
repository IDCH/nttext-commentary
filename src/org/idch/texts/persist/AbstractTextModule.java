/**
 * 
 */
package org.idch.texts.persist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.idch.persist.DBBackedRepository;
import org.idch.texts.Structure;
import org.idch.texts.TextModule;
import org.idch.texts.Token;
import org.idch.texts.TokenSequence;
import org.idch.texts.Work;

/**
 * @author Neal Audenaert
 */
public abstract class AbstractTextModule extends DBBackedRepository implements TextModule {

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
        Work w = this.getWorkRepository().find(uuid);
        if (w == null) {
            // throw new InaccessibleWorkException(uuid)
        }
        
        return w;
    }
    
    /** 
     * Returns the tokens associate with a particular structure. 
     * 
     * @param s The structure for which to return the tokens.
     * @return The list of tokens belonging to this particular structure.
     */
    public List<Token> getTokens(TokenSequence s) {
        return this.getTokenRepository().find(getWork(s), s.getStart(), s.getEnd());
    }
    
    /**
     * Creates structures in the provided sequence for each of the matched the occurrences of 
     * some text.
     * 
     * @param seq The token sequence from which to create the structure.
     * @param name The name of the structure to create.
     * @param match The text to match
     * @return
     */
    public List<Structure> createStructures(TokenSequence seq, String name, String match) {
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
     * Creates a structure based on a matched text. This resolves multiple possible matches
     * by looking for a unique prefix and matching the first occurrence after that prefix. 
     * 
     * @param seq
     * @param name
     * @param match
     * @param prefix
     * @return
     */
    public Structure createStructure(TokenSequence seq, String name, String match, String prefix) {
        String seqText = toString(seq);
        
        int ix = 0;
        int startAt = seqText.indexOf(prefix);
        if (startAt < 0) {
            System.out.println("Prefix not found: '" + prefix + "'. ");
            ix = seqText.indexOf(match);
        } else {
            ix = seqText.indexOf(match, startAt + prefix.length());
        }
        
        Token start = getTokenAt(seq, ix);
        Token end = getTokenAt(seq, ix + match.length() - 1);
        Structure s = new Structure(getWork(seq), name, start, end);
        
        return s;
    }
    
    /**
     * Creates a structure based on a matched text. This resolves multiple possible matches
     * by returning the <tt>ct</tt><sup>th</sup> matching structure. 
     * 
     * @param seq
     * @param name
     * @param match
     * @param ct
     * @return
     */
    public Structure createStructure(TokenSequence seq, String name, String match, int ct) {
        String seqText = toString(seq);
        
        int i = 0;
        int ix = seqText.indexOf(match);
        while (ix >= 0) {
            i++;
            if (++i == ct) {
                Token start = getTokenAt(seq, ix);
                Token end = getTokenAt(seq, ix + match.length() - 1);
                return new Structure(getWork(seq), name, start, end);
            }
            
            ix = seqText.indexOf(match, ix + 1);
        }
        
        return null;
    }
    
    /**
     * Creates a structure in the provided sequence that matches the first occurrence of 
     * some text.
     * 
     * @param seq The token sequence from which to create the structure.
     * @param name The name of the structure to create.
     * @param match The text to match
     * @return
     */
    public Structure createStructure(TokenSequence seq, String name, String match) {
        String seqText = toString(seq);
        
        int ix = seqText.indexOf(match);
        Token start = getTokenAt(seq, ix);
        Token end = getTokenAt(seq, ix + match.length() - 1);
        Structure s = new Structure(getWork(seq), name, start, end);
        
        return s;
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
