/**
 * 
 */
package openscriptures.utils;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A human language, either ancient or modern.
 *  
 * @author Neal Audenaert
 */
@Entity
@Table(name="languages")
public class Language implements Comparable<Language> {
	public enum Direction {
		// also needing vertical directions, see CSS writing-mode
		LTR, RTL;
	}
	
	public static Language lookup(String code) {
		// TODO IMPLEMENT - need to lookup from some catalog and/or database.
		return new Language("", code);
	}
	
	//==============================================================================
	// MEMBER VARIABLES
	//==============================================================================
	
	@Id @GeneratedValue private Long id = null;
	
	/** Display name of the language. 
	 * 
	 * TODO For now, this is assumed to be in English, but we need to support 
	 *      internationalization.
	 */
	@Basic private String name;
	
	
	/** ISO 639-3 language code */
	@Basic private String code;
	
	@Enumerated(EnumType.STRING) private Direction direction = Direction.LTR; 
	
	//==============================================================================
    // CONSTRUCTORS
    //==============================================================================
    
	/**
	 */
	Language() {
	    
	}
	
	/**
	 * 
	 * @param name
	 * @param code
	 */
	public Language(String name, String code) {
		this.name = name;
		this.code = code;
		
		// TODO validate the language code
	}
	
	public Language(String name, String code, Direction dir) {
		this.name = name;
		this.code = code;
		this.direction = dir;
	}
	
	//==============================================================================
    // ACCESSORS
    //==============================================================================
    
	/** Returns the unique identifier used by the persistence layer. */
	Long getId() {
	    return id;
	}
	
	/** Returns the display name of this language (in English). */
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	   
    //==============================================================================
    // UTILITY METHODS
    //==============================================================================
    
	/** Compares this language to another language or language code for equality. */
	public boolean equals(Object o) {
	    if (o instanceof String) {
	        return this.code.equalsIgnoreCase((String)o);
	    } 

	    // forces ClassCastException if not String or Language 
	    Language lg = (Language)o;
	    return this.code.equalsIgnoreCase(lg.code);
	}
	
	/**
	 * 
	 */
	public int hashCode() {
	    return this.code.toLowerCase().hashCode();
	}
	
	public int compareTo(Language lg) {
	    if (this.equals(lg)) return 0;
	    
	    String thisName = this.getName();
	    String thatName = lg.getName();
	    if (thisName != null && thatName != null) {
	        // sort by readable name (if present)
	        return thisName.compareTo(thatName);
	    } else {
	        // otherwise sort by language code
	        return this.code.compareTo(lg.code);
	    }
	}
}
