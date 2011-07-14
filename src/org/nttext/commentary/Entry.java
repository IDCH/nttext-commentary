/**
 * 
 */
package org.nttext.commentary;

import java.util.List;

import org.idch.bibleref.Passage;

/**
 * @author Neal Audenaert
 */
public class Entry {
	
	private Passage ref;
	private List<VariationUnit> variants;
	private String overview;
	
	public Entry(Passage ref) {
		this.ref = ref;
	}
	
	public Passage getReference() {
		return this.ref;
	}

}
