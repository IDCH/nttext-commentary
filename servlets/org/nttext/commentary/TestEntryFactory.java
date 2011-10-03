/**
 * 
 */
package org.nttext.commentary;

import org.nttext.commentary.VariationUnit.TextProxy;

/**
 * @author Neal Audenaert
 */
public class TestEntryFactory {
    
//        String enScripture = 
//            "For what credit is there if you endure when you sin and are beaten? " +
//            "But when you do good <span class=\"var var-01\">and suffer,<sup>1</sup></span>" +
//            "if you endure, it brings <span class=\"var var-02\">favor with God.<sup>2</sup></span>";
//        
//        String gkScripture = 
//            "ποῖον γὰρ κλέος εἰ ἁμαρτάνοντες καὶ κολαφιζόμενοι ὑπομενεῖτε; ἀλλʼ εἰ  " +
//            "ἀγαθοποιοῦντες <span class=\"var var-01\">καὶ πάσχοντες<sup>1</sup></span>" +
//            "ὑπομενεῖτε, τοῦτο <span class=\"var var-02\">χάρις παρὰ θεῷ.<sup>2</sup></span>";
    
    private static String enScripture = 
        "For what credit is there if you endure when you sin and are beaten? " +
        "But when you do good and suffer, if you endure, it brings favor with God.";
    
    private static String gkScripture = 
        "ποῖον γὰρ κλέος εἰ ἁμαρτάνοντες καὶ κολαφιζόμενοι ὑπομενεῖτε; ἀλλʼ εἰ  " +
        "ἀγαθοποιοῦντες καὶ πάσχοντες ὑπομενεῖτε, τοῦτο χάρις παρὰ θεῷ.";
    
    private static String reference = "1Pet.2.20";
//        String reference = "1 Peter 2:20";
    
    private static String var1HCSB = "and suffer";
    private static String var2HCSB = "favor with God";
    
    private static String var1SBLGNT = "καὶ πάσχοντες";
    private static String var2SBLGNT = "χάρις παρὰ θεῷ";
    
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
    
    /*
     * TODO TEMPORARY METHOD FOR TESTING PURPOSES
     */
    static Entry getDefaultEntry() {
//        Entry entry = new Entry(reference);
//        entry.setOverview(overview);
//        
//        TextProxy HCSB = new TextProxy(enScripture, "English", "en", "HCSB");
//        TextProxy SBLGNT = new TextProxy(gkScripture, "Greek", "el", "SBLGNT");
//        
//        VariationUnit vu1 = new VariationUnit(reference);
//        vu1.addReference(HCSB, var1HCSB);
//        vu1.addReference(SBLGNT, var1SBLGNT);
//        entry.addVariationUnit(vu1);
//        
//        VariationUnit vu2 = new VariationUnit(reference);
//        vu2.addReference(HCSB, var2HCSB);
//        vu2.addReference(SBLGNT, var2SBLGNT);
//        entry.addVariationUnit(vu2);
//        
//        return entry;
        return null;
    }
}