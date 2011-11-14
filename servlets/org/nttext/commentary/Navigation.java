/**
 * 
 */
package org.nttext.commentary;

import org.idch.bible.ref.Passage;

/**
 * Model object for implementing navigational controls.
 * 
 * @author Neal Audenaert
 */
public class Navigation {
    
    private LinkDetails prevVerse;
    private LinkDetails nextVerse;
    private LinkDetails prevChapter;
    private LinkDetails nextChapter;
    
    public Navigation(InstanceRepository repo, EntryInstance instance) {
        if (instance == null) {
            prevChapter = new LinkDetails(null);
            prevVerse = new LinkDetails(null);
            nextVerse = new LinkDetails(null);
            nextChapter = new LinkDetails(null);
            return;
        }

        Passage[] passages = repo.findNavigationalPassages(instance);
        prevChapter = new LinkDetails(passages[0]);
        prevVerse   = new LinkDetails(passages[1]);
        nextVerse   = new LinkDetails(passages[2]);
        nextChapter = new LinkDetails(passages[3]);
    }
    
    public LinkDetails getPrevCh() {
        return this.prevChapter;
    }
    
    public LinkDetails getPrevVs() {
        return this.prevVerse;
    }
    
    public LinkDetails getNextVs() {
        return this.nextVerse;
    }
    
    public LinkDetails getNextCh() {
        return this.nextChapter;
    }
    
    //===================================================================================
    // LINK DETAILS CLASS
    //===================================================================================
    public static class LinkDetails {
        private final Passage passage;
        
        LinkDetails(Passage passage) {
            this.passage = passage;
        }
        
        public boolean getAvailable() {
            return passage != null;
        }
        
        public String getUrl() {
            return getAvailable() ? passage.toOsisId() : "#";
        }
        
        public String getLabel() {
            return getAvailable() ? passage.format() : "";
        }
    }
}
