/**
 * 
 */
package org.nttext.mss;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents the date a manuscript was created.
 * @author Neal Audenaert
 */
@Entity
@Table(name="MS_HISTORICALDATE")
public class HistoricalDate {
    // TODO this should be embedded and objects that have a historical date should define 
    //      the appropriate persistence mapping. Dates are ValueObjects, not entities.
    
	public static enum Precision {
		CENTURY, 
		DECADE, 
		YEAR, 
		MONTH, 
		DAY, 
		HOUR, 
		MINUTE, 
		SECOND;
	}
	
	/**
	 * Historical dates all have a degree of uncertainty about them. This enumeration 
	 * allows for this uncertainty to be indicated.
	 * 
	 * @author Neal Audenaert
	 */
	public static enum Certainty {
		/** The date has a normal, or unmarked degree of certainty. */
		NORMAL,
		EXACT, 
		UNCERTAIN,
		APPROXIMATE,
		POSSIBLE,
		DOUBTFUL,
		PROBABLE;
	}
	
	//======================================================================================
    // MEMBER VARIABLES
    //======================================================================================

	// TODO REMOVE IDENTIFIER. This should be a value object.
	@Id @GeneratedValue private Long id;

	@Temporal(TemporalType.DATE) @Column(name="startDate") private Date start;
	@Temporal(TemporalType.DATE) @Column(name="endDate") private Date end;
	
	@Basic private String text;
	
	@Enumerated(EnumType.STRING) private Precision precision;
	
	@Enumerated(EnumType.STRING) private Certainty startCertainty = Certainty.NORMAL;
	@Enumerated(EnumType.STRING) private Certainty endCertainty = Certainty.NORMAL;

	//======================================================================================
	// CONSTRUCTORS
	//======================================================================================

	/**
	 * 
	 */
	public HistoricalDate() {
		this.text = "unknown";
	}
	
	public HistoricalDate(String text) {
		this.text = text;
	}
	
	public HistoricalDate(String text, Date d) {
		this.text = text;
		this.start = d;
		this.end = d;
	}
	
	public HistoricalDate(String text, Date start, Date end) {
		this.start = start;
		this.end = end;
	}

//====================================================================================
// ACCESSORS AND MUTATORS
//====================================================================================

	/** Returns the unique identifier for this date. */
	
	public Long getId() {
	    return this.id;
	}
	
	void setId(Long id) {
	    this.id = id;
	}
	
	/** Returns the starting date for this historical period. */
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return this.start;
	}
	
	/**
	 * Sets the starting date for this historical period.
	 * @param start The start date.
	 */
	public void setStartDate(Date start) {
		this.start = start;
	}
	
	/**
	 * Sets the starting date and the certainty of that date.
	 * @param start The start date.
	 * @param cert The level of certainty. 
	 */
	public void setStartDate(Date start, Certainty cert) {
		this.start = start;
		this.startCertainty = cert;
	}
	
	/** Returns the ending date for this historical period. */
	public Date getEndDate() {
		return this.end;
	}
	
	/**
	 * Sets the ending date for this historical period.
	 * @param end The end date.
	 */
	public void setEndDate(Date end) {
		this.end = end;
	}
	
	/**
     * Sets the ending date and the certainty of that date.
     * @param end The end date.
     * @param cert The level of certainty. 
     */
	public void setEndDate(Date end, Certainty cert) {
		this.end = end;
		this.endCertainty = cert;
	}
	
	/** 
	 * Returns a textual description of this date. For historical dates, the human 
	 * readable description of the data should be considered canonical. The machine 
	 * readable interpretation of the date is intended to aid searching and visualization
	 * but should not be mistaken as the authoritative representation of the date. That is,
	 * 'the third century' may be approximated as Jan 1st, 200- Dec 31st, 299 AD, but it 
	 * is more accurate to say 'the third century' as this conveys an implicit level of 
	 * ambiguity and precision that is best understood within the historical and scholarly 
	 * context of its use. Rather than a technically precise machine readable representation.
	 * 
	 * @return a textual description of this date
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Sets the textual description of this date.
	 * @param value the description
	 * @see #getText()
	 */
	public void setText(String value) {
		this.text = value;
	}
	
	/** Returns the level of precision this date is represented with. */
	public Precision getPrecision() {
	    return this.precision;
	}
	
	/**
	 * Sets the level of precision that this date is represented with.
	 * @param precision The level of precision.
	 */
	public void setPrecision(Precision precision) {
		this.precision = precision;
	}
	
	/** Returns the level of certainty associated with the start of this date. */
	public Certainty getStartCertainty() {
		return this.startCertainty;
	}
	
	/** Sets the level of certainty associated with the start of this date. */
	public void setStartCertainty(Certainty cert) {
	    this.startCertainty = cert;
	}
	
	/** Returns the level of certainty associated with the end of this date. */
	public Certainty getEndCertainty() {
		return this.endCertainty;
	}
	
	/** Sets the level of certainty associated with the end of this date. */
	public void setEndCertainty(Certainty cert) {
		this.endCertainty = cert;
	}
	
	/** Sets the certainty associated with the start and end date. */
	public void setCertainty(Certainty cert) {
		this.startCertainty = cert;
		this.endCertainty = cert;
	}
	
	/** Sets the certainty associated with the start and end date. */
	public void setCertainty(Certainty start, Certainty end) {
		this.startCertainty = start;
		this.endCertainty = end;
	}
	
	//====================================================================================
	// COMPARISON
	//====================================================================================

	
	// TODO add comparison methods
	
	public String toString() {
		return this.text;
	}
	
	//  <hdate precision="century">
	//    <text></text>
	//    <start certainty=""></start>
	//    <end certainty=""></end>
	//  </hdate>
}
