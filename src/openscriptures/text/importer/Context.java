/**
 * 
 */
package openscriptures.text.importer;

import java.util.HashMap;
import java.util.Map;

import openscriptures.text.MutableWork;

/**
 * Provides a shared context for use by both the main <tt>Importer</tt> and individual 
 * structure handlers. This is designed as a lightweight class that provides direct access to
 * its member variables. Use with care.
 * 
 * @author Neal Audenaert
 */
public class Context {
    
    private Map<String, Boolean> flags = new HashMap<String, Boolean>();
    
    private Map<String, String> props = new HashMap<String, String>();
    
    /** The <tt>Work</tt> that is being imported. */
    public MutableWork work = null;
    
    /** Indicates that the parser is in the header section of the document. */
    public boolean inHeader = false;
    
    /** Indicates that the parser is in the front matter section of the document. */
    public boolean inFront = false;
    
    /** Indicates that the parser is in the text of the document. While this is 
     *  set to true, the importer will add tokens from the character data that 
     *  it encounters. */
    public boolean inText = false;
    
 
    public int currentVerse = 0;
    public int currentChapter = 0;
    
    /**
     * Sets a numeric property of the context.
     *  
     * @param key The key for the property to set.
     * @param value The value to be set.
     * 
     * @return The previous value for this property.
     */
    public String set(String key, double value) {
        return this.props.put(key, Double.toString(value));
    }

    /**
     * Sets a string-valued property of this context.
     * 
     * @param key The key for the property to set.
     * @param value The value to be set
     * 
     * @return The previous value for this property.
     */
    public String set(String key, String value) {
        return this.props.put(key, value);
    }
    
    /**
     * Returns the value of a string-valued property.
     * @param key The property whose value should be returned.
     * 
     * @return 
     */
    public String get(String key) {
        return this.props.get(key);
    }
    
    /**
     * Returns the value of a numeric property.  
     * 
     * @param key The property whose value should be returned
     * @return The set value or <tt>Double.NaN</tt> if the set value is null.
     */
    public double getn(String key) {
        double d = Double.NaN;
        String value = this.props.get(key);
        if (value != null) {
            try {
                d = Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                // TODO log warning
                d = Double.NaN;
            }
        }
        
        return d;
    }
    
    public void flag(String state, Boolean value) {
        flags.put(state, value);
    }
    
    public boolean check(String state) {
        return flags.containsKey(state) 
               ? flags.get(state) 
               : false;
    }
}