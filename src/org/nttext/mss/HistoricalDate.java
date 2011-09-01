/**
 * 
 */
package org.nttext.mss;

import java.util.Date;

/**
 * Represents the date a manuscript was created.
 * @author Neal Audenaert
 */
public class HistoricalDate {

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
	
	private Date start;
	private Date end;
	
	private String text;
	
	private Precision precision;
	
	private Certainty startCertainty = Certainty.NORMAL;
	private Certainty endCertainty = Certainty.NORMAL;

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

	/**
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return this.start;
	}
	
	public void setStartDate(Date start) {
		this.start = start;
	}
	
	public void setStartDate(Date start, Certainty cert) {
		this.start = start;
		this.startCertainty = cert;
	}
	
	public Date getEndDate() {
		return this.end;
	}
	
	public void setEndDate(Date end) {
		this.end = end;
	}
	
	public void setEndDate(Date end, Certainty cert) {
		this.end = end;
		this.endCertainty = cert;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String value) {
		this.text = value;
	}
	
	public void setPrecision(Precision precision) {
		this.precision = precision;
	}
	
	public Precision getPrecision() {
		return this.precision;
	}
	
	public Certainty getStartCertainty() {
		return this.startCertainty;
	}
	
	public Certainty getEndCertainty() {
		return this.endCertainty;
	}
	
	public void setStartCertainty(Certainty cert) {
		this.startCertainty = cert;
	}
	
	public void setEndCertainty(Certainty cert) {
		this.endCertainty = cert;
	}
	
	public void setCertainty(Certainty cert) {
		this.startCertainty = cert;
		this.endCertainty = cert;
	}
	
	public void setCertainty(Certainty start, Certainty end) {
		this.startCertainty = start;
		this.endCertainty = end;
	}
	
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
