/**
 * 
 */
package org.nttext.mss;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Neal Audenaert
 */
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
	
    private Long id;
    
	/** MS designation (GA number, ver Soden, shelf number) */
	private Map<String, Designation> designations = new HashMap<String, Designation>();
	
	/** Provenance & ownership history, & current location */
	private Provenance provenance = null;
	
	private Set<DigitizationInfo> digiInfo;
	
	/** Quire collation if available */
	private String quireCollation;

	
	public Manuscript() {
	    
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
	    
	    Catalog cat = new Catalog();
	    Designation d = cat.createMSDesignation(scheme, id);
	    this.designations.put(scheme, d);
	    return d;
	}
	
	public void addDesignation(Designation d) {
	    this.designations.put(d.getScheme(), d);
	}
	
	public void removeDesignation(String scheme) {
	    
	}
}
