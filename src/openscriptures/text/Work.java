/**
 * 
 */
package openscriptures.text;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import openscriptures.text.Work;
import openscriptures.utils.Contributor;
import openscriptures.utils.Language;
import openscriptures.utils.License;

/**
 * Provides an abstract implementation of the Work class that represents the main metadata
 * 
 * @author Neal Audenaert
 */
@Entity
@Table(name="OpenS_Works")
public class Work extends AbstractTokenSequence {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Work.class.getName());
	

	//============================================================================================
	// MEMBER VARIABLES
	//============================================================================================  
	    
	protected Long id;
	protected UUID uuid;
	protected WorkId osisID;
	
	protected String title;             /** The title of this work. */
	protected String abbreviation;      /** An abbreviated title for this work. */
	protected String description;       /** A description of this work. */
	
	protected String creator;		    /** The individual or institution primarily responsible for creating this work. */ 
	protected String publisher;         /** The publisher of this work */
	protected String language;          /** The primary language of this work. */
	protected String type;              /** The type of this work (e.g., Bible, Commentary, Quran). TODO need better semantics for this. */
	protected String copyright;         /** A copyright statement for this work. */	
	protected String scope;             /** The scope of this work (e.g. Gen-Rev, New Testament) */
	protected String refSystem;         /** The reference system used to identify and resolve passages. */
	protected String sourceUrl;         /** URL where this resource was originally obtained. */
	
	protected Date publicationDate;
	protected Date importDate;
	
	// create as embeddable class? This is currently very simplified. 
	protected Set<Contributor> contributors = new HashSet<Contributor>();
	
	// TODO need to implement support for providing license definitions. 
	protected License license;	
	
	private TokenRepository tokenRepo = null;

//============================================================================================
// CONSTRUCTORS
//============================================================================================	
    	
	public Work() {
		
	}
	
	public Work(WorkId id) {
	    this.uuid = UUID.randomUUID();
	    
		this.osisID = id;
	}
	
	public Work(WorkId id, String title, String abbr, String desc) {
		this.uuid = UUID.randomUUID();
		
		this.osisID = id;
		this.title = title;
		this.abbreviation = abbr;
		this.description = desc;
	}
	
	/** 
	 * Sets the token repository to be used to obtain tokens for this work.
	 * @param repo
	 */
	public void setTokenRepository(TokenRepository repo) {
	    this.tokenRepo = repo;
	}

	//============================================================================================
	// METHODS TO CREATE AND QUERY THE CONTENTS OF THIS WORK
	//============================================================================================

	@Deprecated
    public Token addToken(String token) {
        return this.tokenRepo.append(this, token);
    }
    
    /**
     * 
     * @param text
     * @return
     */
	@Deprecated
    public List<Token> tokenize(String text) {
        return this.tokenRepo.appendAll(this, text);
    }
	
	@Deprecated
	public Structure createStructure(Token start, Token end) {
        return null;
    }
	
//============================================================================================
// ACCESSORS AND MUTATORS
//============================================================================================
	
	/** Returns the unique persistent identifier for this work. */
	@Id @GeneratedValue public Long getId() { return this.id; }
	/** Sets the unique persistent identifier for this work. */
	void setId(Long id) { this.id = id; }
	
	/**
	 * Internal ID used to represent a specific local instance of a work. Note that a single 
	 * logical work as represented by a <tt>WorkId</tt> (e.g., the ESV 2001 edition) may 
	 * have multiple representations in the system if, for example, it was imported at 
	 * different times or with different import strategies.
	 */
	@Transient public UUID getUUID() { return this.uuid; }
	
	/** Returns the UUID as a string. Intended to be used by the persistence framework. */
	@Basic String getUUIDString() { return this.uuid.toString(); }
	/** Sets the UUID as a string. Intended to be used by the persistence framework. */
	void setUUIDString(String id) { this.uuid = UUID.fromString(id); }
		
	/** The <tt>WorkId</tt> of this work. */
	@Embedded 
	@AttributeOverrides( {
        @AttributeOverride(name="type", column = @Column(name="workId_type") ),
        @AttributeOverride(name="lgCode", column = @Column(name="workId_lg") ),
        @AttributeOverride(name="publisher", column = @Column(name="workId_pub") ),
        @AttributeOverride(name="name", column = @Column(name="workId_name") ),
        @AttributeOverride(name="publicationDate", column = @Column(name="workId_date") )
	} )
	public WorkId getWorkId() { return this.osisID; }
	/** Used by persistence layer to set the work id for this work. */
	void setWorkId(WorkId osisId) { this.osisID = osisId; }
	// TODO there is a lot of redundant information here.
	
	/** The title of this work. */
	@Basic public String getTitle() { return title; }
	 /** Sets the title. */
    public void setTitle(String title) { this.title = title; }

	/** An abbreviation of this work's title. */
	@Basic public String getAbbreviation() { return this.abbreviation; }
	/** Sets the abbreviated form of the title. */
    public void setAbbreviation(String abbr) { this.abbreviation = abbr; }
	
	/** A description of this work. */
	@Lob public String getDescription() { return this.description; }
	/** Sets the description of this work. */
    public void setDescription(String desc) {  this.description = desc; }

	/** The URL where this resource was originally obtained. */
	@Basic public String getSourceUrl() {  return this.sourceUrl; }
	 /** Sets the URL where this resource was originally obtained. */ 
    public void setSourceUrl(String url) {  this.sourceUrl = url; }
    
	/** The individual or organization responsible for creating this work. */
	@Basic public String getCreator() { return this.creator; }
	/** Sets the creator of this work. */
    public void setCreator(String creator) {  this.creator = creator; }
    
    /** Return the publisher of this work. */
    @Basic public String getPublisher() { return this.publisher; }
    /** Sets the publisher of this work. */
    public void setPublisher(String value) { this.publisher = value; }
    
    /** Returns the date this work was published. */
    @Temporal(TemporalType.DATE) 
    public Date getPublicationDate() { return this.publicationDate; }
    /** Sets the date this work was published. */
    public void setPublicationDate(Date date) { this.publicationDate = date; }

	/** Returns a description of the copyright holder. */
	@Basic public String getCopyright() { return copyright; }
	 /** Sets the copyright holder of this work. */
    public void setCopyright(String copyright) {  this.copyright = copyright; }

	/** Returns a license under which this work is made available.  */
	@Transient public License getLicense() { return license; }
	/** Sets the license under which this work is made available. */
    public void setLicense(License license) { this.license = license; }
    
	/** Return the date that this version of the work was imported. */
	@Temporal(TemporalType.DATE) 
	public Date getImportDate() { return importDate; }
	 /** Sets the date that this version of the work was imported. */
    public void setImportDate(Date importDate) { this.importDate = importDate; }
    
    /** Returns the type of this work (e.g., Bible, Quran, etc.) */
    @Basic public String getType() { return this.type; }
    /** Sets the type of work (e.g., Bible, Quran, Commentary) of this work. */
    public void setType(String value) { this.type = value; } 
    
    /** Returns the primary language used in this work. */
    @Transient
    public Language getLanguage() { 
        ApplicationContext ac = ApplicationContext.getApplicationContext();
        return ac.getLanguage(this.language); 
    }
    /** Sets the primary language used in this work. */
    public void setLanguage(Language lg) { this.language = lg.getCode(); }
    
    /** Returns the language code. */
    @Basic public String getLgCode() { return this.language; }
    /** Sets the language code. Primarily intended for use by the persistence layer. */
    void setLgCode(String lgCode) { this.language = lgCode; }

    /** Returns the scope (e.g., scripture range) of this work. */
    @Basic public String getScope() { return this.scope; }
    /** Sets the scope (e.g. range of scripture) covered by this work. */
    public void setScope(String value) { this.scope = value; }

    /** Returns the reference system used by this work. */
    @Basic public String getRefSystem() { return this.refSystem; }
    /** Sets the reference system used by this work. */
    public void setRefSystem(String value) { this.refSystem = value; }
    
//============================================================================================
// CONTRIBUTOR METHODS
//============================================================================================
    
    /** Returns an unmodifiable <tt>Set</tt> of the contributors to this work. */
    @Transient
    public Set<Contributor> getContributors() { 
        return Collections.unmodifiableSet(this.contributors); 
    }
    
    /**
     * Adds the indicated contributor. 
     * 
     * @param contributor The contributor to add.
     * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
     *      contributor was not already in the set of contributors).
     */
    public boolean addContribotr(Contributor contributor) {
        return this.contributors.add(contributor);
    }
    
    /** 
     * Removes the indicated contributor. 
     * 
     * @param contributor The contributor to remove.
     * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
     *      contributor was in the set of contributors).
     */
    public boolean removeContributor(Contributor contributor) {
        return this.contributors.remove(contributor);
    }
    
    /** Sets the contributors to this work. */
    protected void setContributors(Set<Contributor> contributors) {
        this.contributors = contributors;
    }
    
//============================================================================================
// TOKEN SEQUENCE METHODS
//============================================================================================


    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#getWork()
     */
    @Transient 
    public Work getWork() {
        return this;
    }

    
    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#getText()
     */
    @Transient
    public String getText() {
        StringBuilder sb = new StringBuilder();
        
        Iterator<Token> i = this.iterator(); 
        while (i.hasNext()) {
            sb.append(i.next().getText());
        }
        
        return sb.toString();
    }

    /** 
     * Return the start position (inclusive) in the underlying <tt>Work</tt>'s token 
     * stream. This will always be <tt>0</tt>. 
     */
    @Transient
    public int getStart() {
        return 0;
    }
     
    /** 
     * Return the end position (exclusive) in the underlying <tt>Work</tt>'s token stream.  
     */
    @Transient
    public int getEnd() { 
        return this.tokenRepo.getMaxPosition(this) + 1;
    }
     
    /** Returns the token at the specified index. */
    public Token get(int index) {
        return this.tokenRepo.find(this, index);
    }

	
}
