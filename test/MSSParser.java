import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.lang.StringUtils;
import org.nttext.mss.HistoricalDate;
import org.nttext.mss.HistoricalDate.Certainty;
import org.nttext.mss.HistoricalDate.Precision;
import org.nttext.mss.Manuscript;

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
	private static String DATE_PREFIX = "possibly |ca. ?";
	private static String DATE_SEPARATOR = "(/| ?- ?| or possibly | and )";
	private static String UNKNOWN_DATE_RE = "no date given|unknown date";
	// Searches for things in the form of (something)(ordinal number)/(ordinal number)(something)
	
	private static Pattern datePattern = 
		Pattern.compile(
				"(" +
				   "(" + DATE_PREFIX + ")?" + 
				   ORDINAL_RE + 
				   "(?:" + DATE_SEPARATOR + ORDINAL_RE + ")?" + 
				   "(?:\\s*\\.|\\s*(c\\.?))?" + 
				   "|" + UNKNOWN_DATE_RE +  
				"),? ?");

	Pattern codexPattern = 
		Pattern.compile("Codex (.*)");
	
	Pattern msContentsPattern = 
		Pattern.compile("an?\\W*(.*?)(?: of (.*))?");
	
	Pattern locationPattern = 
		Pattern.compile(
				"located " +
				"(formerly|possibly)? ?" +
				"(?:at|on|in(?: the)?|with(?: the)?) " +
				"(.*)?");
	
	Pattern oldLocationPattern = 
		Pattern.compile(
				"formerly " +
				"(?:at|in(?: the)?|with(?: the)?) " +
				"(.*)?");
	
	Pattern classificationPattern = 
		Pattern.compile(
				"(?:classified as )?" +
				"(possibly)? ?" + 
				"(?:an )?" + 
				"Alanda? category ([IV]+)(?: text)?.*");
	
	private static String booksRegEx =  
		"Matthew|Mark|Luke|John|" +
		"Acts|" +
		"Romans|1?-?2?Corinthians|"+
		"Galatians|Ephesians|Philippians|Colossians|" +
		"1?-?2?Thessalonians|1?-?2?Timothy|Titus|Philemon|Hebrews|" + 
		"James|" + 
		"1?-?2?Peter|" +
		"1?-?2?-?3?John|" +
		"Jude|" +
		"Revelation|" + 
		"commentary|" + 
		"General(?: and Pauline)? (?:Ep\\w+)|" +
		"Gospel|" +
		"NT";
	
	//=======================================================================================
	// MEMBER VARIABLES
	//=======================================================================================
	
	// a Gospel ms.
	private String identifier;
	private String contents;
	private String description;
	
	private Manuscript ms;
	
	private static int unparsedDateCt = 0;
	private boolean debug = false; 
	
	
	//=======================================================================================
	// CONSTRUCTORS
	//=======================================================================================
	
	private MSSParser(String[] csvRecord) {
		this.identifier  = csvRecord[0];
		this.contents    = csvRecord[1]; 
		this.description = csvRecord[2];
	}
	
	//=======================================================================================
	// TOP LEVEL PARSING METHODS
	//=======================================================================================
	
	/**
	 * 
	 * @return
	 */
	public Manuscript parse() {
		ms = new Manuscript();
		
		parseDesignation();
		parseDescription();
		
		return ms;
	}
	
	private void parseDesignation() {
		System.out.println(this.identifier);
		
	}
	
	
	private void parseDescription() {
		String desc = this.description;
		
		desc = desc.replaceFirst("-+see German footnotes?;?-*\\s*", "");  
		if (StringUtils.isBlank(desc)) {
			return;
		} else if (desc.equals("paraphrase")) {
			return;
		}
		
		if (debug) System.out.println("Parsing: " + desc);
		
		desc = stripSeeAlso(desc);
		desc = extractDate(desc);
		desc = extractCodexName(desc);
		desc = extractContents(desc);
		desc = extractLocation(desc);
		desc = extractRuinedMs(desc);
		desc = extractClassification(desc);
		
		if (!debug && !desc.matches("\\W*")) {
			if (desc.matches("\\W*(from( the)?)? ?same.*")) {
				// TODO parse related MS info.
			} else {
//				System.out.println("      Result: " + desc + "\t:: " + this.description );
				unparsedDateCt++;
			}
		}
		
		if (debug) System.out.println("         Result: " + desc);
		if (debug) System.out.println();
		
	}
	
	//=======================================================================================
	// FIELD EXTRACTION METHODS
	//=======================================================================================
	
	private String stripSeeAlso(String desc) {
		Pattern p = Pattern.compile(
				" ?\\(see.*\\),? ?");
		
		String result = desc;
		Matcher mat = p.matcher(desc);
		if (mat.find()) {
			result = desc.substring(0, mat.start());
		}

		return result;
	}
	
	private String extractContents(String desc) {
		Pattern p = Pattern.compile(
				"(an? (.*?)(?: of (.*))?)(?:,|\\s*located)");
		Pattern p2 = Pattern.compile(
			"((incomplete .*?)(?: of (.*))?)(?:,|\\s*located)");
		
		String result = desc;
		Matcher mat = p.matcher(desc);
		Matcher mat2 = p2.matcher(desc);
		mat = mat.find() ? mat 
				: mat2.find() ? mat2 : null;
		
		if (mat != null) {
			String matchedText = mat.group(1);
			int start = mat.start();
			int end = (matchedText.indexOf("located") >= 0)
						? end = mat.start(1) + matchedText.indexOf("located")
						: mat.end(1);
			
			String contents = desc.substring(mat.start(1), end);			
			result = desc.substring(0, start) + desc.substring(end);
			
			if (debug) System.out.println("       Contents: " + contents);
			
		} else { 
//			System.out.println(description);
		}
		
		return result;
	}
	
	private String extractLocation(String desc) {
		Pattern locationPattern = 
			Pattern.compile(
					"(?:, located, )?" +
					"lo?cate?d " +
					"(formerly|possibly)? ?" +
					"(?:at|on|in(?: the)?|with(?: the)?)? ?" +
					"((?:[^,]|(?:, [A-Z]{2}))*),? ?");
		
		String result = desc;
		Matcher mat = locationPattern.matcher(desc);
		boolean found = false;
		int locct = 0;
		while (mat.find()) {
			found = true;
			String location = mat.group(2);
			result = desc.substring(0, mat.start()) + desc.substring(mat.end());
			if (debug) System.out.println("       Location: " + location);
			locct++;
		}
		
		Pattern oldLocationPattern = 
			Pattern.compile(
					"formerly " +
					"(?:at|in(?: the)?|with(?: the)?) " +
					"(.*)?");
		
		mat = oldLocationPattern.matcher(result);
		while (mat.find()) {
			found = true;
			String location = mat.group(1);
			result = result.substring(0, mat.start()) + result.substring(mat.end());
			if (debug) System.out.println("Former Location: " + location);
		}
			
		if (!found) {
//			System.out.println(desc);
		}
		
		
		return result;
	}
	
	
	
	private String extractClassification(String desc) {
		String result = desc;
		Matcher mat = classificationPattern.matcher(desc);
		
		boolean matched = false;
		if (mat.find()) {
			matched = true;
			String category = mat.group(2);
			result = desc.substring(0, mat.start()) + desc.substring(mat.end());
			if (debug) System.out.println("       Category: " + category);
		} 
		
		mat = Pattern.compile(",? ?(classified .*)").matcher(desc);
		if (mat.find()) {
			// strip out the excess text regardless
			// XXX overwriting previous result.
			result = desc.substring(0, mat.start()) + desc.substring(mat.end());
			
			if (!matched) {
				// if we didn't previously match a sub-set of the clause, record the category
				String category = mat.group(1);
				if (debug) System.out.println("       Category: " + category);
			}
		}
		
		mat = Pattern.compile("text too brief to classify,? ?").matcher(result);
		if (mat.find()) {
			result = result.substring(0, mat.start()) + result.substring(mat.end());
			if (debug) System.out.println("       Category: text too brief to classify");
		}
		
		return result;
	}
	
	private String extractCodexName(String desc) {
		Matcher mat = Pattern.compile("Codex ([^,]*), ").matcher(desc);
		String result = desc;
		if (mat.find()) {
			String name = mat.group(1);
			result = result.substring(0, mat.start()) + result.substring(mat.end());
			if (debug) System.out.println("          Codex: " + name);
		}
		return result;
	}
	private String extractRuinedMs(String desc) {
		Pattern p = Pattern.compile(" ?ruined ms\\.?");
		
		String result = desc;
		Matcher mat = p.matcher(desc);
		if (mat.find()) {
			// TODO mark as ruined
			result = desc.substring(0, mat.start()) + desc.substring(mat.end());
		}
		
		return result;
	}

	//=======================================================================================
	// DATE PARSING METHODS
	//=======================================================================================
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public String extractDate(String desc) {
		String result = desc;
		Matcher mat = datePattern.matcher(desc);

		if (mat.find() && (mat.start() == 0)) {
			HistoricalDate date = new HistoricalDate(mat.group(1));
			
			result = desc.substring(mat.end());
			if (date.getText().matches(UNKNOWN_DATE_RE)) {
				return result;
			}
			
			String prefix = StringUtils.trimToNull(mat.group(2));
			String firstDate = StringUtils.trimToNull(mat.group(3));
			String separator = StringUtils.trimToNull(mat.group(4));
			String lastDate = StringUtils.trimToNull(mat.group(5));
			String suffix = StringUtils.trimToNull(mat.group(6));
			
			if (suffix != null) {
				if (suffix.matches("c\\.?")) {
					date.setPrecision(Precision.CENTURY);
				} else {
					// Doesn't seem to happen
				}
			}
			
			if (prefix != null) {
				if (prefix.equalsIgnoreCase("ca.")) {
					// mark date as approximate			
					date.setCertainty(Certainty.APPROXIMATE);
				} else if (prefix.equalsIgnoreCase("possibly")) {
					// mark date as uncertain
					date.setCertainty(Certainty.POSSIBLE);
				} else {
					// doesen't occur
				}
			}
			
			setStartDate(firstDate, date);
			setEndDate(firstDate, lastDate, separator, date);
			if (debug) System.out.println("           Date: " + date.getText());
		} else {
//			System.out.println(desc);
		}
		
		return result;
	}
	
	/**
	 * Helper method to set the start date while parsing a date.
	 * 
	 * @param firstDate The string based representation of a date (number only).
	 * @param date The <tt>HistoricalDate</tt> object to be updated.
	 */
	private void setStartDate(String firstDate, HistoricalDate date) {
		// create a date instance
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
			if (firstDate.length() < 3) {
				firstDate = firstDate + "00";
			} else if (date.getPrecision() == null) {
				date.setPrecision(Precision.YEAR);
			}
			
			Date d = formatter.parse(firstDate);
			date.setStartDate(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Helper method to set the end date while parsing a date.
	 * 
	 * @param firstDate The start date. Used as a basis for interpreting relative 
	 * 		end dates such as 1934/35.
	 * @param endDate The string based representation of a date (number only).
	 * @param separator The separator used to split date parts. This may indicate
	 * 		the certainty of the trailing date as distinct from the entire date, as
	 * 		in 1935 or possibly 36. 
	 * @param date The <tt>HistoricalDate</tt> object to be updated.
	 */
	private void setEndDate( 
			String firstDate, String endDate, String separator,
			HistoricalDate date) {
		if (endDate == null) {
			date.setEndDate(date.getStartDate());
			return;
		}
		
		// for dates like 1325/26, adjust the second to be a full four-year date
		if (firstDate.length() > endDate.length()) {
			int offset = firstDate.length() - endDate.length();
			endDate = firstDate.substring(0, offset) + endDate;
		}
		
		date.setEndDate(date.getStartDate());
		if (separator != null && (separator.indexOf("possibly") > 0)) {
			date.setEndCertainty(Certainty.POSSIBLE);
		}
	}
	
	public static void main(String[] args) {
		try {
			CSVParser parser = 
//				new CSVParser(new FileReader(FNAME_PAP), CSVStrategy.EXCEL_STRATEGY);
				new CSVParser(new FileReader(FNAME_MAJ), CSVStrategy.EXCEL_STRATEGY);
//				new CSVParser(new FileReader(FNAME_MIN), CSVStrategy.EXCEL_STRATEGY);
//				new CSVParser(new FileReader(FNAME_LECT), CSVStrategy.EXCEL_STRATEGY);

			int rCt = 0;
			parser.getLine();		// discard the header
			String[] record = parser.getLine();
			while (record != null) {
				rCt++;
				if (record.length < 3) {
					System.err.println("Bad record format: Expected three fields" +
							" (line = " + parser.getLineNumber() + ").");
					record = parser.getLine();
					continue;
				}
				
				MSSParser ms = new MSSParser(record);
				ms.parse();
				
				record = parser.getLine();
			}
			
			System.out.println("Records: " + rCt);
			System.out.printf("Records with unparsed data: %d (%4.2f%%)\n", 
					unparsedDateCt, ((float)unparsedDateCt / rCt ) * 100);

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
