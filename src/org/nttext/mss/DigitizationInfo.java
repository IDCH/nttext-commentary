/**
 * 
 */
package org.nttext.mss;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.nttext.util.Institution;

/**
 * @author Neal Audenaert
 */
@SuppressWarnings("unused")
public class DigitizationInfo {
	// Digitization: Date of image, photography institution, media, image format, link
	// TODO there's a bunch more information that needs to go here.
	
    private Date dateOfDigitization;
	private Institution digitizingInstition;
	private String media;  						// e.g. Microfilm, Digital, etc.
	private String imageFormat;
	private List<URL> online; 
	
}
