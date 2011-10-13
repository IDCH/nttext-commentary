/**
 * 
 */
package openscriptures.text;

import java.util.HashMap;
import java.util.Map;

import openscriptures.utils.Language;
import openscriptures.utils.Language.Direction;
import openscriptures.utils.License;

/**
 * @author Neal Audenaert
 */
public class ApplicationContext {
    
    public static ApplicationContext instance = null;
    
    /**
     * Returns the instance of the ApplicationContext singleton.
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        // TODO allow for multiple contexts? Right now this implements a simple 
        //      singleton pattern.
        if (instance == null) {
            instance = new ApplicationContext();
        }
        
        return instance;
    }

    
    //=======================================================================================
    // MEMBER VARIABLES
    //=======================================================================================
    
    private Map<String, Language> languages = new HashMap<String, Language>();
    
    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================
    ApplicationContext() {
        loadLanguageDefinitions();
    }
    
    
    private void loadLanguageDefinitions() {
        // TODO load from file.
        // TODO use ISO codes.
        defineLanguage("eng", "English", Direction.LTR);
        defineLanguage("grc", "Greek", Direction.LTR);
    }
    
    private Language defineLanguage(String lgCode, String name, Language.Direction dir) {
        Language lg = null;
        synchronized (languages) {
            lg = this.languages.get(lgCode);
            if (lg == null) {
                lg = new Language(name, lgCode, dir);
                this.languages.put(lgCode, lg);
            }
        }
        
        return lg;
    }
    
    //=======================================================================================
    // ACCESSORS
    //=======================================================================================
    
    
    public Language getLanguage(String lgCode) {
        return languages.get(lgCode);
    }
    
    public License getLicense(String license) {
        // 
        return null;
    }
    

    //=======================================================================================
    // CONFIGURATION METHODS
    //=======================================================================================
    
}
