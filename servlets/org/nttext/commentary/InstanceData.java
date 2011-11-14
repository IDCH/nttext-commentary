/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRef;
import org.idch.texts.Structure;
import org.idch.texts.TextModule;
import org.idch.texts.Work;
import org.idch.texts.WorkRepository;
import org.idch.texts.structures.Verse;


/**
 * A lightweight facade to provide Java bean access to the content of an instance 
 * as needed for use in the view layer. 
 * 
 * @author Neal Audenaert
 */
public class InstanceData {
    private EntryInstance instance;
    private CommentaryModule module;
    
    private VUComparator sblgntRefComparator;
    private Work sblgnt;
    private Work hcsb;
    public InstanceData(CommentaryModule module, EntryInstance inst) {
        this.instance = inst;
        this.module = module;
        
        WorkRepository repo = module.getWorkRepository();
        
        List<Work> works = repo.findByAbbr("SBLGNT");
        if (works.size() > 0) {
            sblgnt = works.get(0);
        }
        
        works = repo.findByAbbr("HCSB");
        if (works.size() > 0) {
            hcsb = works.get(0);
        }
        
        sblgntRefComparator = new VUComparator(module.getStructureRepository(), sblgnt);
    }
    
    /**
     * 
     * @return
     */
    public String getPassage() {
        // TODO convert to English language reference (e.g. 1Pet.2.20 to 1 Peter 2:20)
        //      In theory, we should use a general purpose formatter :
        //              "${bkName} ${chapterNumber!""}:${verseNumber!""}"  
        //      with the appropriate revisions to ensure that the colon is handled properly
        //      and to support multi-verse passages.
        return this.instance.getPassage().toString();
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getPrimaryPassage() {
        return new PassageReference(module, hcsb, instance);
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getSecondaryPassage() {
        return new PassageReference(module, sblgnt, instance);
    }
    
    /**
     * 
     * @return
     */
    public String getOverview() {
        return this.instance.getOverview();
    }
    
    public List<VUData> getVariationUnits() {
        
        // get the variants as a sorted set based on their SBLGNT reference
        SortedSet<VariationUnit> vus = new TreeSet<VariationUnit>(sblgntRefComparator);
        vus.addAll(this.instance.getVariationUnits());
        
        int ix = 0;
        List<VUData> variants = new ArrayList<VUData>();
        for (VariationUnit vu : vus) {
            variants.add(new VUData(vu, ++ix));
        }
        
        return variants;
    }
    
    public boolean getShowTable() {
        return !this.instance.getVariationUnits().isEmpty();
    }
    
    //===================================================================================
    // INNER CLASS TO REPRESENT A SCRIPTURE REFERENCE FOR A PASSAGE
    //===================================================================================
    /**
     * 
     * @author Neal Audenaert
     */
    public static class PassageReference {
        private CommentaryModule module;
        private Work work;
        private EntryInstance instance;
        
        public PassageReference(CommentaryModule module, Work w, EntryInstance inst) {
            this.module = module;
            this.work = w;
            this.instance = inst;
        }
        
        public String getMarkedText() {
            TextModule textModule = module.getTextRepository();
            Passage passage = instance.getPassage();
            VerseRef start = passage.getFirst();
            VerseRef end = passage.getLast();
            
            Structure s = Verse.getVerse(textModule, work, start.toOsisId());
            
            if (!start.equals(end)) {
                Verse endVs = Verse.getVerse(textModule, work, end.toOsisId());
                
                s = new Structure(work.getUUID(), "passage", 
                        s.getStartToken(), endVs.getEndToken());
            }
            
            return textModule.toString(s);
        }
        
        public String getLanguage() {
            return (work != null && work.getLanguage() != null)
                        ? this.work.getLanguage().getName()
                        : "Unknown";
            
        }
        
        public String getLg() {
            return ((work != null) && work.getLgCode() != null)
                    ? work.getLgCode()
                    : "unk";
        }
        
        public String getVersion() {
            return this.work.getAbbreviation();
        }
    }
}
