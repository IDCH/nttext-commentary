/**
 * 
 */
package openscriptures.text.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import openscriptures.text.Token;
import openscriptures.text.Work;
import openscriptures.text.WorkId;
import openscriptures.utils.Contributor;
import openscriptures.utils.Language;
import openscriptures.utils.License;

/**
 * Provides an abstract implementation of the Work class that represents the main metadata
 * 
 * @author Neal Audenaert
 */
public abstract class BasicWork implements Work {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Work.class.getName());
	
	// TODO We need to factor this into an Inteface with a DB backed implementation and
	//		a RESTful web-based implementation
	
	protected UUID id;

	protected WorkId osisID;
		
	protected String title;
	protected String abbreviation;
	protected String description;
	
	protected String creator;		    // TODO Make this a list? 
	protected String publisher;
	protected Date publicationDate;
	protected Language language;
	protected String type;
	protected Set<Contributor> contributors = new HashSet<Contributor>();
	protected String copyright;	
	protected License license;	
	protected String scope;
	protected String refSystem;
	
	/** URL where this resource was originally obtained. */
	protected String sourceUrl;		// URL where this resource was originally obtained.  
	
	protected Date importDate;
	// TODO need to implement servers.

//============================================================================================
// MEMBER VARIABLES
//============================================================================================	
	
	public BasicWork() {
		
	}
	
	public BasicWork(WorkId id) {
		this.osisID = id;
	}
	
	public BasicWork(WorkId id, String title, String abbr, String desc) {
		this.id = UUID.randomUUID();
		
		this.osisID = id;
		this.title = title;
		this.abbreviation = abbr;
		this.description = desc;
	}
	
//============================================================================================
// ACCESSORS AND MUTATORS
//============================================================================================
	
	// ACCESSORS
	
	/**
	 * Internal ID used to represent a specific local instance of a work. Note that a single 
	 * logical work as represented by a <tt>WorkId</tt> (e.g., the ESV 2001 edition) may 
	 * have multiple representations in the system if, for example, it was imported at 
	 * different times or with different import strategies.
	 */
	@Override
	public UUID getId() { return this.id; }
		
	/** The <tt>WorkId</tt> of this work. */
	@Override
	public WorkId getWorkId() { return this.osisID; }
	
	/** The title of this work. */
	@Override
	public String getTitle() { return title; }
	
	/** An abbreviation of this work's title. */
	@Override
	public String getAbbreviation() { return this.abbreviation; }
	
	/** A description of this work. */
	@Override
	public String getDescription() { return this.description; }
	
	/** The URL where this resource was originally obtained. */
	@Override
	public String getSourceURL() {  return this.sourceUrl; }

	/** The individual or organization responsible for creating this work. */
	@Override
	public String getCreator() { return this.creator; }
	
	/** Returns a description of the copyright holder. */
	@Override
	public String getCopyright() { return copyright; }
	
	/** Returns a license under which this work is made available.  */
	@Override
	public License getLicense() { return license; }
	
	/** Return the date that this version of the work was imported. */
	@Override
	public Date getImportDate() { return importDate; }
	
	/** Returns an unmodifiable <tt>Set</tt> of the contributors to this work. */
	@Override
	public Set<Contributor> getContributors() { 
		return Collections.unmodifiableSet(this.contributors); 
	}
	
    /** Returns the date this work was published. */
    @Override
    public Date getPublicationDate() { return this.publicationDate; }

    /** Return the publisher of this work. */
    @Override
    public String getPublisher() { return this.publisher; }

    /** Returns the type of this work (e.g., Bible, Quran, etc.) */
    @Override
    public String getType() { return this.type; }

    /** Returns the primary language used in this work. */
    @Override
    public Language getLanguage() { return this.language; }

    /** Returns the scope (e.g., scripture range) of this work. */
    @Override
    public String getScope() { return this.scope; }

    /** Returns the reference system used by this work. */
    @Override
    public String getRefSystem() { return this.refSystem; }
    
//============================================================================================
// TOKEN SEQUENCE METHODS
//============================================================================================


    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#getWork()
     */
    @Override
    public Work getWork() {
        return this;
    }

    
    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#getText()
     */
    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        
        Iterator<Token> i = this.iterator(); 
        while (i.hasNext()) {
            sb.append(i.next().getText());
        }
        
        return sb.toString();
    }

	
}
