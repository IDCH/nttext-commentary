/**
 * 
 */
package org.nttext.commentary;

import openscriptures.ref.Passage;
import openscriptures.ref.VerseRange;


/**
 * @author Neal Audenaert
 */
public class TestInstanceFactory {
    
    private static final Passage passage = new VerseRange("1Pet.2.20");
    
    private static final String ENTRY_OVERVIEW = 
            "<p>Although there are several variants, some of which have substantial support, " +
            "there is sufficient textual stability in this verse that there is unanimity in " +
            "the various Greek editions. None of the well attested variants differ in " +
            "meaning from that of the original text.</p>" +

            "<p>Only two rdgs have variants which actually affect the meaning of the verse. " +
            "Two variants (Var 1a, 1b) with limited and late attestation arose from an " +
            "original rdg which uses a word having two meanings, one more common and the " +
            "other less so; the more common meaning made the next word redundant, with " +
            "some scribes rewording the phrase to avoid the redundancy, while others " +
            "omitted one element or the other altogether. Another variant (Var 2b) arose " +
            "due to misperceptions of an idiom which could be altered slightly to " +
            "give a different meaning.</p>";
    
    private static final String VU1_COMMENTARY = 
            "<p>The Greek word for “suffer” was often (primarily?) used to convey " +
            "“endure,” in the sense of enduring a negative ordeal, and was viewed as " +
            "synonymous with the next Greek word. Perhaps to avoid the apparent " +
            "redundancy, some scribes altered the word, replacing it with the previous " +
            "word for “be beaten,” conveying “But when you do good and are beaten and " +
            "endure...” Other Scribes omitted it altogether, conveying “But when you do " +
            "good and endure,” allowing the notion of preserving in doing good without " +
            "explicit reference to suffering.</p>";
    
    private static final String VU2_COMMENTARY = 
            "<p>While the original text conveys that enduring through suffering on " +
            "account of doing good brings favor with God, the poorly attested late variant " +
            "indicates that enduring (i.e., the ability to endure?) such suffering is a " +
            "gift from God. The variant may have arisen accidentally due to the similarity " +
            "of spelling in a scribe’s exemplar (ΧΑΡΙCΠΑΡΑΘΩ) and the variant " +
            "(ΧΑΡΙCΜΑΠΑΡΑΘΩ). Alternatively, the variant might be an intended improvement " +
            "(or correction) whereby the original was deemed to be an unwarranted " +
            "redundancy due to the same expression being used at the beginning of v. 19.</p>";
    
    private static VariationUnit createVU1(CommentaryModule commentaryModule) {
        VURepository vuRepo = commentaryModule.getVURepository();
        VariantReadingRepository rdgRepo = commentaryModule.getRdgRepository();
        
        VariationUnit vu1 = vuRepo.create(passage);
        vu1.setCommentary(VU1_COMMENTARY);
        
        VariantReading rdg1a = rdgRepo.create(vu1);
        rdg1a.setEnglishReading("and suffer");
        rdg1a.setGreekReading("καὶ πάσχοντες");
        rdg1a.setWitnessDescription("All Greek witnesses except 8 manuscripts.");
        
        VariantReading rdg1b = rdgRepo.create(vu1);
        rdg1b.setEnglishReading("and are beaten");
        rdg1b.setGreekReading("καὶ κολφιζόμενοι");
        rdg1b.setWitnessDescription("61 326 1837");
        
        VariantReading rdg1c = rdgRepo.create(vu1);
        rdg1c.setEnglishReading("(omit)");
        rdg1c.setGreekReading("");
        rdg1c.setWitnessDescription("945 1729 1739 1881 2298");
        
        rdgRepo.save(rdg1a);
        rdgRepo.save(rdg1b);
        rdgRepo.save(rdg1c);
        
        vuRepo.save(vu1);
        
        return vu1;
    }
    
    private static VariationUnit createVU2(CommentaryModule commentaryModule) {
        VURepository vuRepo = commentaryModule.getVURepository();
        VariantReadingRepository rdgRepo = commentaryModule.getRdgRepository();
        
        VariationUnit vu2 = vuRepo.create(passage);
        vu2.setCommentary(VU2_COMMENTARY);
        
        VariantReading rdg2a = rdgRepo.create(vu2);
        rdg2a.setEnglishReading("brings favor with God");
        rdg2a.setGreekReading("χάρις παρὰ θεῷ");
        rdg2a.setWitnessDescription("All Greek witnesses except 8 manuscripts.");
        
        VariantReading rdg2b = rdgRepo.create(vu2);
        rdg2b.setEnglishReading("is a gift from God");
        rdg2b.setGreekReading("χαρισμα παρὰ θεῷ");
        rdg2b.setWitnessDescription("61 326 1837");
        
        rdgRepo.save(rdg2a);
        rdgRepo.save(rdg2b);
        
        vuRepo.save(vu2);
        
        return vu2;
    }
    
    /*
     * TODO TEMPORARY METHOD FOR TESTING PURPOSES
     */
    static EntryInstance getDefaultInstance(CommentaryModule commentaryModule) {
        InstanceRepository repo = commentaryModule.getInstanceRepository();
        
        EntryInstance instance = repo.find(new VerseRange("1Pet.2.20"));
        if (instance == null) {
            // normally throw a not found error, but for now, we'll create the entry.
            instance = repo.create(passage);
            instance.setOverview(ENTRY_OVERVIEW);
            
            // create variation units
            VariationUnit vu1 = createVU1(commentaryModule);
            VariationUnit vu2 = createVU2(commentaryModule);
            
            repo.associate(instance, vu1);
            repo.associate(instance, vu2);
            
            repo.save(instance);
        }
        
        return instance;
    }
}
