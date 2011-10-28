/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import openscriptures.text.TextModule;
import openscriptures.text.Work;
import openscriptures.text.structures.Verse;

/**
 * A lightweight facade to provide Java bean access to the content of an entry 
 * as needed for use in the view layer. 
 * 
 * @author Neal Audenaert
 */
public class EntryData {
    private Entry entry;
    private CommentaryModule module;
    
    private VUComparator sblgntRefComparator;
    
    public EntryData(CommentaryModule module, Entry e) {
        this.entry = e;
        this.module = module;
        
        Work sblgnt = module.getWorkRepository().find(11L);
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
        return this.entry.getPassage().toString();
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getPrimaryPassage() {
        // TODO change to HCSB
        Work sblgnt = module.getWorkRepository().find(11L);
        return new PassageReference(module, sblgnt, entry);
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getSecondaryPassage() {
        Work sblgnt = module.getWorkRepository().find(11L);
        return new PassageReference(module, sblgnt, entry);
    }
    
    /**
     * 
     * @return
     */
    public String getOverview() {
        return this.entry.getOverview();
    }
    
    public List<VUData> getVariationUnits() {
        
        // get the variants as a sorted set based on their SBLGNT reference
        SortedSet<VariationUnit> vus = new TreeSet<VariationUnit>(sblgntRefComparator);
        vus.addAll(this.entry.getVariationUnits());
        
        int ix = 0;
        List<VUData> variants = new ArrayList<VUData>();
        for (VariationUnit vu : vus) {
            variants.add(new VUData(vu, ++ix));
        }
        
        return variants;
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
        private Entry entry;
        
        public PassageReference(CommentaryModule module, Work w, Entry e) {
            this.module = module;
            this.work = w;
            this.entry = e;
        }
        
        public String getMarkedText() {
            TextModule textModule = module.getTextRepository();
            Verse vs = Verse.getVerse(textModule, work, entry.getPassage().toString());
            return textModule.toString(vs);
        }
        
        public String getLanguage() {
            return this.work.getLanguage().getName();
        }
        
        public String getLg() {
            return this.work.getLgCode();
        }
        
        public String getVersion() {
            return this.work.getAbbreviation();
        }
    }
}
