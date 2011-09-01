/**
 * 
 */
package org.idch.bibleref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.nttext.utils.Filenames;

/**
 * @author Neal Audenaert
 */
public class XSLPractice {
	
	private static final File XSLT_DIR = new File("data/nt/HCSB/xslt");
	private static final File OUTPUT_DIR = new File("data/nt/HCSB/derived");
	private static final File INPUT = new File("data/nt/HCSB/source/01-Gen_HCSB.textonly.xml");
	
	public static String extractBasename(File file) throws IOException {
		String basename = Filenames.getBasename(
				Filenames.getCanonicalPOSIXPath(file));
		Matcher mat = Pattern.compile("\\d+-(\\S+)_HCSB.textonly").matcher(basename);
		System.out.println(basename);
		if (!mat.matches()) {
			throw new IOException("Unexpected file. This doesn't look like an HCSB source file.");
		}
		
		return mat.group(1);
	}
	
	public static void simplify(File input) throws IOException, TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		String name = extractBasename(input);
		File xslt = new File(XSLT_DIR, "simplify.xslt");
		File output = new File(OUTPUT_DIR, name + ".simplified.xml");
		
		assert input.exists() : "Input file does not exist";
		assert xslt.exists() : "XSLT file does not exist";
		if (output.exists()) 
			output.delete();
		
		Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));
		transformer.transform(
				new StreamSource(input), 
				new StreamResult(new FileOutputStream(output)));
	}
	
	public static void main(String[] args) {
		try {  
			simplify(INPUT);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
