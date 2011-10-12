/**
 * 
 */
package openscriptures.text;

import openscriptures.utils.Language;
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
    
    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================
    
    //=======================================================================================
    // ACCESSORS
    //=======================================================================================
    
    
    public Language getLanguage(String lgCode) {
        // TODO load language repository from XML file (or other configured source)
        //      and lookup the appropriate language
        return null;
    }
    
    public License getLicense(String license) {
        // 
        return null;
    }
    

    //=======================================================================================
    // CONFIGURATION METHODS
    //=======================================================================================
    
}
