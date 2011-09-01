/**
 * 
 */
package org.nttext.commentary;

import java.util.Collections;
import java.util.List;

/**
 * @author Neal Audenaert
 */
public class VariationUnit {
	
	private Long id;
	private String commentary;
	private List<VariantReading> readings;

	
	public List<VariantReading> getReadings() {
		return Collections.unmodifiableList(this.readings);
	}
	
	public void addReading(VariantReading rdg) {
		
	}
}
