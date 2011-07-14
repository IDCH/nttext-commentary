/**
 * 
 */
package org.idch.bibleref;

/**
 * @author Neal Audenaert
 */
public class InvalidReferenceException extends RuntimeException {
	private String msg;
	private String ref;
	
	InvalidReferenceException(String msg, String ref) {
		super(msg);
	}
}
