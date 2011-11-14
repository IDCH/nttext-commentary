/**
 * 
 */
package org.nttext.commentary;

import org.idch.bible.ref.BookOrder;
import org.idch.bible.ref.VerseRange;
import org.idch.bible.ref.VerseRef;

/**
 * Model object for implementing navigational controls.
 * 
 * @author Neal Audenaert
 */
public class Navigation {
    
    private BookOrder order;
    private int bookId;
    private int ch;
    private int vs;
    
    private LinkDetails prevVerse;
    private LinkDetails nextVerse;
    private LinkDetails prevChapter;
    private LinkDetails nextChapter;
    
    public Navigation(EntryInstance instance) {
        if (instance == null) {
            prevChapter = new LinkDetails(null);
            prevVerse = new LinkDetails(null);
            nextVerse = new LinkDetails(null);
            nextChapter = new LinkDetails(null);
            return;
        }
            
        VerseRef first = instance.getPassage().getFirst();
        if (first == null) {
            // TODO BAD THINGS
        }
        
        this.order = first.getBookOrder();
        this.bookId = first.getBookIndex();
        this.ch = first.getChapter();
        this.vs = first.getVerse();

        this.initPrevChapter();
        this.initPrevVerse();
        this.initNextVerse();
        this.initNextChapter();
    }
    
    private LinkDetails getLinkDetails(int bk, int ch, int vs) {
        VerseRef ref = new VerseRef(order, bk, ch, vs, null);
        return new LinkDetails(new VerseRange(ref, ref));
    }
    
    private void initPrevChapter() {
        if (vs > 1) {
            prevChapter = getLinkDetails(bookId, ch, 1);
        } else if (ch > 1) {
            prevChapter = getLinkDetails(bookId, ch - 1, 1);
        } else {
            prevChapter = new LinkDetails(null);
        }
    }
    
    private void initNextChapter() {
        if (ch < 3) {
            nextChapter = getLinkDetails(bookId, ch + 1, 1);
        } else {
            nextChapter = new LinkDetails(null);
        }
    }
    
    private void initNextVerse() {
        if (vs < 30) {
            nextVerse = getLinkDetails(bookId, ch, vs + 1);
        } else {
            nextVerse = new LinkDetails(null);
        }
    }
    
    private void initPrevVerse() {
        if (vs > 1) {
            prevVerse = getLinkDetails(bookId, ch, vs - 1);
        } else {
            prevVerse = new LinkDetails(null);
        }
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
        private final VerseRange passage;
        
        LinkDetails(VerseRange passage) {
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
