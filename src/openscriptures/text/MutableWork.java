/**
 * 
 */
package openscriptures.text;

import java.util.Date;
import java.util.List;

import openscriptures.utils.Contributor;
import openscriptures.utils.Language;
import openscriptures.utils.License;

/**
 * Represents a Work instance that can be updated. 
 * 
 * @author Neal Audenaert
 */
public interface MutableWork extends Work {
    
    /** Sets the title of this work. */
    public void setTitle(String value);
    
    /** Sets and abbreviated form of this work's title. */
    public void setAbbreviation(String value);
    
    /** Set the description of this work. */
    public void setDescription(String value);
    
    /** The URL where this resource was originally obtained. 
     *  TODO need to be more explicit about what this means. Is this the original
     *       document file (eg, the SBLGNT.osis.xml.zip download) or is the web-service
     *       that my implementation connected to. */
    public void setSourceURL(String value);
    
    //=======================================================================================
    // HEADER PROPERTIES
    //=======================================================================================   
    
    /** Sets the creator of this work. */
    public void setCreator(String value);
    
    /** Set the date this work was published. */
    public void setPublicationDate(Date date);
    
    /** Set the publisher of this work. */
    public void setPublisher(String value);
    
    /** Sets the type of work. */
    public void setType(String value);
    
    /** Set the language of this work's primary text. */ 
    public void setLanguage(Language value);
    
    /** Sets a description of the copyright holder. */
    public void setCopyright(String value);
    
    /** Sets the scope (e.g., scripture passages) covered by this work. */
    public void setScope(String value);
    
    /** Sets the reference system used by this work. */
    public void setRefSystem(String value);
    
    /** Sets the license under which this work is made available.  */
    public void setLicense(License value);
    
    /** Set the date this work was imported. */
    public void setImportDate(Date date);
    
    /**
     * Adds the indicated contributor. 
     * 
     * @param contributor The contributor to add.
     * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
     *      contributor was not already in the set of contributors).
     */
    public boolean addContribotr(Contributor contributor);

    /** 
     * Removes the indicated contributor. 
     * 
     * @param contributor The contributor to remove.
     * @return <tt>true</tt> if the set of contributors was modified (that is, if the 
     *      contributor was in the set of contributors).
     */
    public boolean removeContributor(Contributor contributor);
    
//  public void setFormat(String value);
//  public void setSource(String value);

//=======================================================================================
// TOKEN AND STRUCTURE FACTORIES
//=======================================================================================   
  
    /**
     * 
     * @param token
     * @return
     */
    public Token addToken(String token);
    
    /**
     * 
     * @param text
     * @return
     */
    public List<Token> tokenize(String text);
    
    /**
     * 
     * @param start
     * @param end
     * @return
     */
    public Structure createStructure(Token start, Token end);
    
}
