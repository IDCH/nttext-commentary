/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRef;
import org.idch.texts.Structure;
import org.idch.texts.StructureComparator;
import org.idch.texts.StructureRepository;
import org.idch.texts.TextModule;
import org.idch.texts.Token;
import org.idch.texts.TokenSequence;
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
        private TextModule textModule;
        private Work work;
        private EntryInstance instance;
        
        public PassageReference(CommentaryModule module, Work w, EntryInstance inst) {
            textModule = module.getTextRepository();
            this.work = w;
            this.instance = inst;
        }
        
        /** Returns a structure for the scripture passage referenced by this instance. */
        private Structure getPassageReference() {
            Passage passage = instance.getPassage();
            VerseRef start = passage.getFirst();
            VerseRef end = passage.getLast();
            
            Structure s = Verse.getVerse(textModule, work, start.toOsisId());
            
            if (!start.equals(end)) {
                Verse endVs = Verse.getVerse(textModule, work, end.toOsisId());
                
                s = new Structure(work.getUUID(), "passage", 
                        s.getStartToken(), endVs.getEndToken());
            }
            
            return s;
        }
        
        private SortedSet<VUReference> getVUReferences() {
            // TODO this needs to be done at a higher level - we don't know how to order
            //      VUs unless we have a base text.
            SortedSet<VUReference> vuRefs = new TreeSet<VUReference>(new StructureComparator());
            StructureRepository sRepo = textModule.getStructureRepository();

            Set<VariationUnit> vus = instance.getVariationUnits();
            for (VariationUnit vu : vus) {
                VUReference ref = VUReference.findReference(sRepo, work, vu);
                vuRefs.add(ref);
            }

            return vuRefs;
        }
        
        public String toString(List<Token> tokens) {
            StringBuilder sb = new StringBuilder();
            for (Token t : tokens) {
                sb.append(t.getText());
            }
            
            return sb.toString();
        }
        
      
        String START_LINK = "<a href=\"#VAR_ID\" class=\"var VAR_ID\" title=\"View the details of this variation unit\">";
        String END_LINK = "<sup>IX</sup></a>";
        
        private void markRefs(Token t, SortedSet<VUReference> vuRefs, StringBuilder sb) {
            int ix = 0;
            for (VUReference ref : vuRefs) {
                //  <a href="#var-01" class="var var-01" title="View the details of this variation unit">καὶ πάσχοντες<sup>1</sup></a>
                
                ix++;
                if (ref.getStart() == t.getPosition()) {
                    String varId = ((ix < 10) ? "var-0" + ix : "var-" + ix);
                    sb.append(START_LINK.replaceAll("VAR_ID", varId));
                } else if (ref.getEnd() == t.getPosition()) {
                    sb.append(END_LINK.replaceAll("IX", Integer.toString(ix)));
                }
                
            }
        }
        
        /**
         * Returns the text of the referenced scripture passage, marked with the variation 
         * unit references corresponding to this entry instance.
         */
        public String getMarkedText() {
            Structure s = getPassageReference();
            List<Token> tokens = textModule.getTokens(s);
            SortedSet<VUReference> vuRefs = getVUReferences();
            
            StringBuilder sb = new StringBuilder();
            for (Token t : tokens) {
                markRefs(t, vuRefs, sb);
                sb.append(t.getText());
            }
            return sb.toString();
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
