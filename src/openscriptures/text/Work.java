/**
 * 
 */
package openscriptures.text;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import openscriptures.utils.Contributor;
import openscriptures.utils.Language;
import openscriptures.utils.License;

/**
 * @author Neal Audenaert
 */
public interface Work extends TokenSequence {

	/**
	 * Local, globally unique identifier for this particular instantiation of a work. 
	 * Note that a logical work as represented by a <tt>WorkId</tt> (e.g., the ESV 2001 
	 * edition) may have multiple representations in the system if, for example, it was imported at 
	 * different times or with different import strategies.
	 */
	public UUID getUUID();
		
	/** 
	 * The <tt>WorkId</tt> that identifies the conceptual work represented by this 
	 * object. This follows the OSIS strategy for naming works: 
	 * <tt>Type.language.publisher.work.date</tt>
	 * 
	 *  TODO Update this documentation. Compare to FRBR.
	 */
	public WorkId getWorkId();
	
	/** The title of this work. */
	public String getTitle();
	
	/** An abbreviation of this work's title. */
	public String getAbbreviation();
	
	/** A description of this work. */
	public String getDescription();
	
	//=======================================================================================
	// HEADER PROPERTIES
    //=======================================================================================
	
	// TODO need to do a better job of handling creator/contributor roles
	//      (possibly) need to handle import format
	
	/** The individual or organization responsible for creating this work. */
	public String getCreator();
	
	/** Return the date this work was published. */ 
	public Date getPublicationDate();
	
	/** Return the publisher of this work. */
	public String getPublisher(); 
	
    /** Return the type of work. */
    public String getType();
    
    /** Return the language of this work's primary text. */ 
    public Language getLanguage();
    
	/** Returns a description of the copyright holder. */
	public String getCopyright();
	
	/** Returns the scope (e.g., scripture passages) covered by this work. */
    public String getScope();
    
    /** Returns the reference system used by this work. */
    public String getRefSystem();
    
	/** Returns an unmodifiable <tt>Set</tt> of the contributors to this work. */
	public Set<Contributor> getContributors();
	
	/** Returns a license under which this work is made available.  */
	public License getLicense();
	
	/** The URL where this resource was originally obtained. 
	 *  TODO need to be more explicit about what this means. Is this the original
	 *       document file (eg, the SBLGNT.osis.xml.zip download) or is the web-service
	 *       that my implementation connected to. */
	public String getSourceURL();
	
	/** Return the date that this version of the work was imported. */
	public Date getImportDate();
	
	
}
