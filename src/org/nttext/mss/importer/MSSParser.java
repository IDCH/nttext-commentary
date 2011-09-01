package org.nttext.mss.importer;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.lang.StringUtils;

/**
 * 
 */

/**
 * @author Neal Audenaert
 */
public class MSSParser {

	private static String FNAME_LECT = "data/mss/Lectionaries.csv";
	private static String FNAME_MAJ  = "data/mss/Majuscules.csv";
	private static String FNAME_MIN  = "data/mss/Minuscules.csv";
	private static String FNAME_PAP  = "data/mss/Papyri.csv";
	
	
	private static String ORDINAL_RE = "(\\d+)(?:st|nd|rd|th)?";  // regex to capture the numeric part of an ordinal number
	private static String DATE_PREFIX = "possibly |ca. ";
	private static String DATE_SEPARATOR = "(/|-| or possibly | and )";
	// Searches for things in the form of (something)(ordinal number)/(ordinal number)(something)
	private static Pattern datePattern = 
		Pattern.compile(
				"(" + DATE_PREFIX + ")?" + 
				ORDINAL_RE + 
				"(?:" + DATE_SEPARATOR + ORDINAL_RE + ")?" + 
				"(\\D*)");
	
	public static void parseDescription(String description) {
		
	}
	
	public static boolean isDate(String dateString) {
		return dateString.equalsIgnoreCase("unknown date") || 
		       datePattern.matcher(dateString).matches();
	}
	
	public static void parseDate(String dateString) {
		
		if (dateString.equalsIgnoreCase("unknown date")) {
			// mark this date as unknown
			return;
		}
		
		Matcher matcher = datePattern.matcher(dateString);
		if (!matcher.matches())
			return; 		// error
		
		String prefix = matcher.group(1);
		String firstDate = matcher.group(2);
		String separator = matcher.group(3);
		String lastDate = matcher.group(4);
		String suffix = StringUtils.trimToNull(matcher.group(5));
		
		if (suffix != null)
			System.out.println(suffix);
		
//		String dateRegEx = ;
//		Matcher matcher = Pattern.compile(dateRegEx).matcher(date);
//		if (matcher.matches()) {
////			System.out.println(date + ": matched " + matcher.groupCount());
//			if (StringUtils.isNotEmpty(matcher.group(1)))
//				System.out.println(matcher.group(1)); //  + "  " + matcher.group(2) + " : " + date);
//		} else {
////			System.out.println(date + ":  not matched");
//		}
	}
	
	public static void main(String[] args) {
		try {
			CSVParser parser = 
//				new CSVParser(new FileReader(FNAME_PAP), CSVStrategy.EXCEL_STRATEGY);
//				new CSVParser(new FileReader(FNAME_MAJ), CSVStrategy.EXCEL_STRATEGY);
//				new CSVParser(new FileReader(FNAME_MIN), CSVStrategy.EXCEL_STRATEGY);
				new CSVParser(new FileReader(FNAME_LECT), CSVStrategy.EXCEL_STRATEGY);
			
			parser.getLine();		// discard the header
			String[] record = parser.getLine();
			while (record != null) {
				if (record.length < 3) {
					System.err.println("Bad record format: Expected three fields" +
							" (line = " + parser.getLineNumber() + ").");
					record = parser.getLine();
					continue;
				}
				
				String identifier = record[0];
				String contents = record[1]; 
				String description = record[2];
				
				// parse the description 
				String[] parts = description.split(",");
				
				
				
				if (parts.length >= 3) {
					String date = StringUtils.trimToEmpty(parts[0]);
					
					parseDate(date);
					
						
//					System.out.print(identifier);
//					System.out.print(" :: ");
//					System.out.print(description);
//					System.out.println();
				}				
				record = parser.getLine();
			}

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
