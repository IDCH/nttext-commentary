/**
 * 
 */
package org.nttext.commentary;

/**
 * A lightweight facade to provide Java bean access to the content of an entry 
 * as needed for use in the view layer. 
 * 
 * @author Neal Audenaert
 */
public class EntryData {
    private Entry entry;
    
    public EntryData(Entry e) {
        this.entry = e;
    }
    
    public String getReference() {
        return this.entry.getPassage().toString();
    }
    
    public ScriptureReference getPrimaryScripture() {
        String text = this.entry.getMarkedText("HCSB");
        return new ScriptureReference("English", "en", "HCSB", text);
    }
    
    public ScriptureReference getSecondaryScripture() {
        String text = this.entry.getMarkedText("SBLGNT");
        return new ScriptureReference("Greek", "el", "SBLGNT", text);
    }
    
    public String getOverview() {
        return this.entry.getOverview();
    }
    
    public static class ScriptureReference {
        private String markedText;
        private String language;
        private String lg;
        private String version;
        
        public ScriptureReference(String language, String lg, String version, String text) {
            this.markedText = text;
            this.language = language;
            this.lg = lg;
            this.version = version;
        }
        
        public String getMarkedText() {
            return this.markedText;
        }
        
        public String getLanguage() {
            return this.language;
        }
        
        public String getLg() {
            return this.lg;
        }
        
        public String getVersion() {
            return this.version;
        }
    }
}
