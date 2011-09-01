/**
 * 
 */
package org.idch.bibleref;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.idch.bible.ref.sblgnt.SBLGNT;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

//import net.sf.saxon.s9api.Processor;
//import net.sf.saxon.s9api.SaxonApiException;
//import net.sf.saxon.s9api.Serializer;
//import net.sf.saxon.s9api.XdmNode;
//import net.sf.saxon.s9api.XsltCompiler;
//import net.sf.saxon.s9api.XsltExecutable;
//import net.sf.saxon.s9api.XsltTransformer;

/**
 * @author Neal Audenaert
 */
public class SBLGNTTransform {
	
	// TODO externalize these to resource bundle
	private static final String SPLIT_BOOKS_XSLT = "/split_by_book.xslt";
	
	private static final File XSLT_DIR = new File("data/nt/SBLGNT/xslt");
	private static final File DERIVED_DIR = new File("data/nt/SBLGNT/derived");
	private static final File SOURCE_FILE = new File("data/nt/SBLGNT/source/SBLGNT.osis.xml");
	
	public static List<String> loadBookList(File bookListFile) throws IOException {
		List<String> books = new ArrayList<String>(66);
		
		BufferedReader reader = new BufferedReader(new FileReader(bookListFile)); 
		while (reader.ready()) {
			String book = reader.readLine();
			
			if (!StringUtils.isEmpty(book))
				books.add(book);
		}
		
		return books;
	}

	private static final void processXsl(File input, StreamSource xsl, OutputStream output) {
//	throws SaxonApiException {
//
//		Processor proc = new Processor(false);
//		XsltCompiler comp = proc.newXsltCompiler();
//		XsltExecutable exp = comp.compile(xsl);
//		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
//
//		XsltTransformer trans = exp.load();
//		trans.setInitialContextNode(source);
//		Serializer out = proc.newSerializer(output);
//		trans.setDestination(out);
//		trans.transform();
	}
	
	public static void splitBooks(List<String> books, File xsltDir, File outDir, File input) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		try {  
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(xsltDir);
			Template template = cfg.getTemplate(SPLIT_BOOKS_XSLT);
			
			Map<String, Object> dataModel = new HashMap<String, Object>();
			for (String book : books) {
				System.out.println("Processing book: " + book);
				
				// generate XSLT from template
				dataModel.put("osisID", book);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 8);
				OutputStreamWriter outputStream = new OutputStreamWriter(baos);
				template.process(dataModel, outputStream);

				// process XSLT
				String xslt = baos.toString("UTF-8");
				FileOutputStream fos = new FileOutputStream(
						new File(outDir,  book + ".xml"));
				
				Transformer transformer = tFactory.newTransformer(new StreamSource(new StringReader(xslt)));
				transformer.transform(
						new StreamSource(input), 
						new StreamResult(fos));
				
				// clean up
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static String getVerseRange(String start, String end) throws /* SaxonApiException, */ TemplateException, IOException {
//		File output = new File(DERIVED_DIR, "query_result.xml");
		
		
		// generate stylesheet
		Writer writer = new StringWriter();
		Writer output = new StringWriter();
		
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("start", start);
//		data.put("end", end);
//		
//		Configuration cfg = new Configuration();
//		cfg.setDirectoryForTemplateLoading(new File("data/nt/SBLGNT/xslt"));
//		Template vsRangeTemplate = cfg.getTemplate("get_vs_range.xsl");
//		vsRangeTemplate.process(data, writer);
//		writer.flush();
//		
//		Processor proc = new Processor(false);
//		XsltCompiler comp = proc.newXsltCompiler();
//		XsltExecutable exp = comp.compile(new StreamSource(
//				new StringReader(writer.toString())));
//		
//		File input = new File(DERIVED_DIR, "1Pet.xml");
//		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
//		
//		XsltTransformer trans = exp.load();
//		Serializer out = proc.newSerializer(output);
//		trans.setInitialContextNode(source);
//		trans.setDestination(out);
//		trans.transform();
		
		return output.toString();
	}
	
	public static void test(File input) throws FileNotFoundException, TransformerException /*, SaxonApiException */ {
//		File xslt = new File(XSLT_DIR, "get_vs_ranage.xsl");
//		File output = new File(DERIVED_DIR, "query_result.xml");
//		
//		assert input.exists() : "Input file does not exist";
//		assert xslt.exists() : "XSLT file does not exist";
//		if (output.exists()) 
//			output.delete();
//		
//		// generate stylesheet
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		OutputStreamWriter writer = new OutputStreamWriter(baos);
//		Configuration cfg = new Configuration();
//		cfg.setDirectoryForTemplateLoading(XSLT_DIR);
//		
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("start", "1Pet.1.5");
//		data.put("end", "1Pet.1.8");
//		
//		Template vsRangeTemplate = cfg.getTemplate("/get_vs_ranage.xsl");
//		vsRangeTemplate.process(data, writer);
//		writer.flush();
//		
//		String xslt = new String(baos.toByteArray(), "UTF-8");
//		
//		 Processor proc = new Processor(false);
//         XsltCompiler comp = proc.newXsltCompiler();
//         XsltExecutable exp = comp.compile(new StreamSource(xslt));
//         XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
//         Serializer out = proc.newSerializer(output);
//         XsltTransformer trans = exp.load();
//         trans.setInitialContextNode(source);
//         trans.setDestination(out);
//         trans.transform();
	}
	
	public static void main(String[] args) {
		List<String> books;
		try {
			SBLGNT book = new SBLGNT();
			
			System.out.println(book.getVerseRange("Luke.1.5", "Luke.1.7"));
			
			test(new File(DERIVED_DIR, "1Pet.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
