/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.idch.texts.TextModule;
import org.idch.texts.Work;
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
    
    public InstanceData(CommentaryModule module, EntryInstance inst) {
        this.instance = inst;
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
        return this.instance.getPassage().toString();
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getPrimaryPassage() {
        // TODO change to HCSB
        Work sblgnt = module.getWorkRepository().find(11L);
        return new PassageReference(module, sblgnt, instance);
    }
    
    /**
     * 
     * @return
     */
    public PassageReference getSecondaryPassage() {
        Work sblgnt = module.getWorkRepository().find(11L);
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
            Verse vs = Verse.getVerse(textModule, work, instance.getPassage().toString());
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
