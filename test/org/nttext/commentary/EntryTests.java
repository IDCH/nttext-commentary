/**
 * 
 */
package org.nttext.commentary;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;
import openscriptures.ref.VerseRef;
import junit.framework.TestCase;
/**
 * @author Neal Audenaert
 */
public class EntryTests extends TestCase {
    
    private static  String reference = "1Pet.2.20";
    
    private static String enScripture = 
        "For what credit is there if you endure when you sin and are beaten? " +
        "But when you do good <span class=\"var var-01\">and suffer,<sup>1</sup></span>" +
        "if you endure, it brings <span class=\"var var-02\">favor with God.<sup>2</sup></span>";
    
    private static String gkScripture = 
        "ποῖον γὰρ κλέος εἰ ἁμαρτάνοντες καὶ κολαφιζόμενοι ὑπομενεῖτε; ἀλλʼ εἰ  " +
        "ἀγαθοποιοῦντες <span class=\"var var-01\">καὶ πάσχοντες<sup>1</sup></span>" +
        "ὑπομενεῖτε, τοῦτο <span class=\"var var-02\">χάρις παρὰ θεῷ.<sup>2</sup></span>";
    
    
    private static String overview = 
        "<p>Although there are several variants, some of which have substantial " +
        "support, there is sufficient textual stability in this verse that there is " +
        "unanimity in the various Greek editions. None of the well attested variants " +
        "differ in meaning from that of the original text.</p>" +
        
        "<p>Only two rdgs have variants which actually affect the meaning of the verse. " +
        "Two variants (Var 1a, 1b) with limited and late attestation arose from an " +
        "original rdg which uses a word having two meanings, one more common and the " +
        "other less so; the more common meaning made the next word redundant, with some " +
        "scribes rewording the phrase to avoid the redundancy, while others omitted " +
        "one element or the other altogether. Another variant (Var 2b) arose due to " +
        "misperceptions of an idiom which could be altered slightly to give a " +
        "different meaning.</p>";
    
    
    private Entry entry;
    
    public void setUp() {
        entry = new Entry(new VerseRef(reference));
        entry.setOverview(overview);
        
        entry.setText("SBLGNT", gkScripture);
        entry.setText("HCSB", enScripture);
    }
    
    public void tearDown() {
        
    }
    
    public void testAccessors() {
        Passage range = new VerseRange(reference); 
        
        assertEquals("Incorrect passage.", range, entry.getReference());
        assertEquals("Incorrect SBLGNT text", gkScripture, entry.getText("SBLGNT"));
        assertEquals("Incorrect HCSB text", enScripture, entry.getText("HCSB"));
        assertEquals("Incorrect overview", overview, entry.getOverview());
    }
    

}
