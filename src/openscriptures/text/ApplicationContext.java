/**
 * 
 */
package openscriptures.text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import org.idch.util.PersistenceUtil;

import openscriptures.text.impl.jpa.JPAStructureRepository;
import openscriptures.text.impl.jpa.JPAWorkRepository;
import openscriptures.utils.Language;
import openscriptures.utils.Language.Direction;
import openscriptures.utils.License;

/**
 * @author Neal Audenaert
 */
@Deprecated
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
    
    public static ApplicationContext getApplicationContext(TextRepository repo) {
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
    private TextRepository m_repo;
    
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
    
    ApplicationContext(TextRepository repo) {
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
    
    public WorkRepository getWorkRepository(UUID id) {
        return this.works;
    }
    
    public Work getWork(UUID id) {
        return this.works.find(id);
    }
    
    public Token getToken(UUID workId, int pos) {
        Work w = getWork(workId);
        if (w == null) {
            throw new InaccessibleWorkException(workId);
        }
        
        if (pos >= w.size()) {
            throw new ArrayIndexOutOfBoundsException(
                    "The requested token (pos='" + pos + "') does not exist for this work. " + 
                    "This work (" + w.getUUIDString() + ") has " + w.size() + " tokens.");
        }
        
        return w.get(pos);
    }
    
    //=======================================================================================
    // CONFIGURATION METHODS
    //=======================================================================================
    
    
    //========================================================================================
    // INNER CLASSES
    //========================================================================================
    public static class InaccessibleWorkException extends RuntimeException {
        private static final long serialVersionUID = -1033727611861247876L;

        InaccessibleWorkException(UUID id) {
            super("Could not locate the work '" + id + "'");
        }
    }
}
