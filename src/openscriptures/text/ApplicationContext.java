/**
 * 
 */
package openscriptures.text;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.idch.util.PersistenceUtil;

import openscriptures.text.impl.JPAStructureRepository;
import openscriptures.text.impl.JPAWorkRepository;
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
    
    public static ApplicationContext getApplicationContext(TextRepo repo) {
        // TODO allow for multiple contexts? Right now this implements a simple 
        //      singleton pattern.
        if (instance == null) {
            instance = new ApplicationContext(repo);
        }
        
        return instance;
    }

    
    //=======================================================================================
    // MEMBER VARIABLES
    //=======================================================================================
    
    private EntityManagerFactory m_emf;
    private TextRepo m_repo;
    
    private Map<String, Language> languages = new HashMap<String, Language>();
    
    private WorkRepository works = null;
    
    /** Used to create structure instances. */
    private StructureRepository structures = null;
    
    //=======================================================================================
    // CONSTRUCTORS
    //=======================================================================================
    ApplicationContext() {
        loadLanguageDefinitions();
        
        // TODO need to make this configurable
        this.m_emf = PersistenceUtil.getEMFactory("nttext");
        
        works = new JPAWorkRepository(m_emf);
        structures = new JPAStructureRepository(m_emf);
    }
    
    ApplicationContext(TextRepo repo) {
        m_repo = repo;
        works = m_repo.getWorkRepository();
        structures = m_repo.getStructureRepository();
    }
    
    public void shutdown() {
        PersistenceUtil.shutdown();
        
        // ... and other cleanup tasks.
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
    

    /**
     * Returns the token repository for use with the specified work.
     * 
     * @param w
     * @return
     */
    public TokenRepository getTokens(Work w) {
        // TODO look up the appropriate work repo for the supplied work
        return works.getTokenRepository();
    }
    
    public StructureRepository getStructureRepo(String key) {
        return this.structures;
    }
    
    //=======================================================================================
    // CONFIGURATION METHODS
    //=======================================================================================
    
}
