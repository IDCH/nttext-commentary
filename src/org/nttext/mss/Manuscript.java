/**
 * 
 */
package org.nttext.mss;

import java.util.HashSet;
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
	
	/** MS designation (GA number, ver Soden, shelf number) */
	private Set<Designation> designations = new HashSet<Designation>();
	
	/** Provenance & ownership history, & current location */
	private Provenance provenance = null;
	
	private Set<DigitizationInfo> digiInfo;
	
	/** Quire collation if available */
	private String quireCollation;

}
