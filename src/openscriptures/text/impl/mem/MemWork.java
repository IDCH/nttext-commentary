/**
 * 
 */
package openscriptures.text.impl.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import openscriptures.text.MutableWork;
import openscriptures.text.Structure;
import openscriptures.text.Token;
import openscriptures.text.WorkId;
import openscriptures.text.impl.BasicToken;
import openscriptures.text.impl.BasicWork;
import openscriptures.utils.Contributor;
import openscriptures.utils.Language;
import openscriptures.utils.License;

/**
 * @author Neal Audenaert
 */
public class MemWork extends BasicWork implements MutableWork  {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MemWork.class.getName());
	
//============================================================================================
// MEMBER VARIABLES
//============================================================================================	
	// TODO need to cross-ref this info with Dublin Core and create a Dublin core adapter.
	// TODO need to provide lookup mechanisms. Might implement as a separate service
	
	// In practice, this should come from a database as the tokens sequence 
	// will be too large to fit in memory
	private List<Token> tokens = new ArrayList<Token>();
	
	private boolean skipUnrecognizedTokens = true;

//============================================================================================
// CONSTRUCTORS
//============================================================================================	
	public MemWork(WorkId id) {
		super(id);
	}
	
	public MemWork(WorkId id, String title, String abbr, String desc) {
		super(id, title, abbr, desc);
	}
	
//============================================================================================
// METHODS TO CREATE AND QUERY THE CONTENTS OF THIS WORK
//============================================================================================

	// TODO HMM. . . .HOW TO REPRESENT THESE? FOR NOW WE'LL ASSUME LOCAL DB ACCESS
	
	public Token addToken(String token) {
	    Token t;
	    synchronized (tokens) {
	        t = new BasicToken(this, tokens.size(), token);
	        tokens.add(t);
	    }
	    
	    return t;
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public List<Token> tokenize(String text) {
	    
	    Token t;
	    List<Token> results = new ArrayList<Token>();
	    Matcher mat = Pattern.compile(BasicToken.TOKENIZATION_PATTERN).matcher(text);
	    
	    synchronized (tokens) {
	        while (mat.find()) {
	            String token = mat.group();
	            if (BasicToken.classify(token) == null && skipUnrecognizedTokens)
	                continue;
	            
	            t = new BasicToken(this, tokens.size(), token);
	            tokens.add(t);
	            results.add(t);
	        }
	    }
        return results;
	}
	
	public Structure createStructure(Token start, Token end) {
		// TODO IMPLEMENT STUB METHOD
		
		return null;
	}
	
	
//============================================================================================
// MUTATORS
//============================================================================================

	/** Sets the title. */
	@Override
	public void setTitle(String title) { 
	    this.title = title; 
	}

	/** Sets the abbreviated form of the title. */
	@Override
	public void setAbbreviation(String abbr) { 
	    this.abbreviation = abbr; 
	}

	/** Sets the description of this work. */
	@Override
	public void setDescription(String desc) { 
	    this.description = desc; 
	}

	/** Sets the URL where this resource was originally obtained. */ 
	@Override
	public void setSourceURL(String url) { 
	    this.sourceUrl = url; 
	}

	/** Sets the creator of this work. */
	@Override
	public void setCreator(String creator) {  
	    this.creator = creator; 
	}

	/** Sets the publisher of this work. */
    @Override
    public void setPublisher(String value) {
        this.publisher = value;
    }
    
    /** Sets the date this work was published. */
    @Override
    public void setPublicationDate(Date date) {
        this.publicationDate = date;
    }
    
    /** Sets the type of work (e.g., Bible, Quran, Commentary) of this work. */
    @Override
    public void setType(String value) {
        this.type = value;
        
    }

    /** Sets the primary language used in this work. */
    @Override
    public void setLanguage(Language value) {
        this.language = value;
    }
    
	/** Sets the copyright holder of this work. */
	public void setCopyright(String copyright) { 
	    this.copyright = copyright;
	}

	/** Sets the license under which this work is made available. */
	@Override
	public void setLicense(License license) { 
	    this.license = license; 
	}

	/** Sets the date that this version of the work was imported. */
	@Override
	public void setImportDate(Date importDate) { 
	    this.importDate = importDate; 
	}

	/**
	 * Adds the indicated contributor. 
	 * 
	 * @param contributor The contributor to add.
	 * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
	 * 		contributor was not already in the set of contributors).
	 */
	@Override
	public boolean addContribotr(Contributor contributor) {
		return this.contributors.add(contributor);
	}
	
	/** 
	 * Removes the indicated contributor. 
	 * 
	 * @param contributor The contributor to remove.
	 * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
	 * 		contributor was in the set of contributors).
	 */
	@Override
	public boolean removeContributor(Contributor contributor) {
		return this.contributors.remove(contributor);
	}
	
	/** Sets the contributors to this work. */
	protected void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}
	
    /** Sets the scope (e.g. range of scripture) covered by this work. */
    @Override
    public void setScope(String value) {
        this.scope = value;
    }


    /** Sets the reference system used by this work. */
    @Override
    public void setRefSystem(String value) {
        this.refSystem = value;
    }
    
    
//============================================================================================
// TOKEN SEQUENCE METHODS
//============================================================================================

	/** Returns the token at the specified index. */
	@Override
	public Token get(int index) {
	    return this.tokens.get(index);
	}

}
