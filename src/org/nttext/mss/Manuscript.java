/**
 * 
 */
package org.nttext.mss;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Neal Audenaert
 */
@Entity
@Table(name="MANUSCRIPTS")
public class Manuscript {
	
	/**
		•	Date, material, codex / fragments / single leaf  / unbound, dimensions,
		•	Binding & cover, ornamentation, clasps, fly leaves, insects
		•	Columns & lines per page
		•	Leaf count – foliated / paginated
		•	Contents ( e , a , p , r )
		•	Palimpsest?  ID of uberschrift / unterschrift
		•	Additional material: Eusebian canons, hypothesis, kephlaia, icons, lectionary apparatus, commentary (intertextual or marginal), head pieces
		•	DONE Digitization: Date of image, photography institution, media, image format, link
		•	Bibliography (from Elliot & elsewhere)
		•	General condition & specific notes on condition (water damage, fragmentary leaves, missing leaves, tearing, trimmed edges, stains, insect / rodent damage, etc.)
		•	Conservation history; photography history
		•	Known relationships with other MSS (abschrift, palimpsest, family)
		•	Diglot / triglot – interlinear or separate columns / sectors
		•	Superscripts / Subscripts
		•	Color of ink for text / other features
		•	Addition / inclusion of non-biblical text (patriotic works etc.)

	 */
    //=======================================================================================
    // MEMBER VARIABLES 
    //=======================================================================================
    
    private Long id;
    
	/** MS designation (GA number, ver Soden, shelf number) */
	private Map<String, Designation> designations = 
	    new HashMap<String, Designation>();
	
	private String description = null;

	private HistoricalDate date = null;
	
	private String contents = null;
	
	/** Provenance & ownership history, & current location */
	private Provenance provenance = null;
	
	private Set<DigitizationInfo> digiInfo;
	
	/** Quire collation if available */
	private String quireCollation;

	//=======================================================================================
    // CONSCTRUCTORS 
    //=======================================================================================
	Manuscript() {
	    
	}
	
	public Manuscript(Designation d) {
	    this.designations.put(d.getScheme(), d);
	}
	
	public boolean hasDesignation(String scheme) {
	    return this.designations.containsKey(scheme);
	}
	
	public Designation getDesignation(String scheme) {
	    return this.designations.get(scheme);
	}
	
	public Designation addDesignation(String scheme, String id) {
	    // TODO figure out where the catalog came from
	    // TODO synchronize access to designations
	    
//	    CatalogImpl cat = new CatalogImpl();
//	    Designation d = cat.createMSDesignation(scheme, id);
//	    this.designations.put(scheme, d);
//	    return d;
	    return null;
	}
	
	public void addDesignation(Designation d) {
	    this.designations.put(d.getScheme(), d);
	}
	
	public void removeDesignation(String scheme) {
	    
	}
	
	//=======================================================================================
	// 
	//=======================================================================================
	@Id
    @Column(name="mss_id")
    @GeneratedValue
	Long getId() {
	    return id;
	}
	
	void setId(Long id) {
	    this.id = id;
	}
	
	/**
	 * Returns the set of designators associated with this manuscript.
	 * 
	 * @return
	 */
	@OneToMany(targetEntity=Designation.class)
	@JoinTable(
	        name="MS_DESIGNATIONS_JOINTABLE",
	        joinColumns=@JoinColumn(name="mss_id"),
	        inverseJoinColumns=@JoinColumn(name="designation_id")
	)
	Set<Designation> getDesignations() {
	    Set<Designation> result = new HashSet<Designation>();
	    result.addAll(designations.values());
	    
	    return result;
	}
	
	/**
	 * Assigns a set of designations to this manuscript.
	 * 
	 * @param designations
	 */
	void setDesignations(Set<Designation> designations) {
	    for (Designation d : designations) {
	        this.designations.put(d.getScheme(), d);
	    }
	}
	
	/** Returns a brief description of this manuscript. */
	@Basic
	public String getDescription() {
	    return this.description;
	}
	
	/** Sets a brief description of this manuscript. */
	public void setDescription(String value) {
	    this.description = value;
	}
	
	/** Returns the date this manuscript is believed to have been produced. */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="date_fk")
	public HistoricalDate getDate() {
	    return this.date;
	}
	
	/** Sets the date this manuscript is believed to have been produced. */
	public void setDate(HistoricalDate date) {
	    this.date = date;
	}
	
	/** Returns a description of the contents of this manuscript. */
	@Basic
	public String getContents() {
	    return this.contents;
	}
	
	/** Sets a description of the contents of this manuscript. */
	public void setContents(String value) {
	    this.contents = value;
	}
	
}
