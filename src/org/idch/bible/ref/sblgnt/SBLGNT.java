/**
 * 
 */
package org.idch.bible.ref.sblgnt;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

//import net.sf.saxon.s9api.Processor;
//import net.sf.saxon.s9api.SaxonApiException;
//import net.sf.saxon.s9api.Serializer;
//import net.sf.saxon.s9api.XdmNode;
//import net.sf.saxon.s9api.XsltCompiler;
//import net.sf.saxon.s9api.XsltExecutable;
//import net.sf.saxon.s9api.XsltTransformer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Neal Audenaert
 */
public class SBLGNT {

	private File sourceDirectory = new File("data/nt/SBLGNT/derived");
	private File xsltBaseDirectory = new File("data/nt/SBLGNT/xslt");
	
	private Map<String, Object> cache;		// TODO Use the appropriate caching tool.
	
	private Configuration tmplConfig;
	
	public SBLGNT() {
		
		try {
			tmplConfig = new Configuration();
			tmplConfig.setDirectoryForTemplateLoading(xsltBaseDirectory);
		} catch (IOException ioe) {
			
		}
	}
	
	private String getVsRangeTemplate(String start, String end) throws TemplateException, IOException {
		String GET_VS_RANGE_TEMPLATE = "get_vs_range.xsl";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("start", start);
		data.put("end", end);
		
		Writer writer = new StringWriter();
		Template vsRangeTemplate = tmplConfig.getTemplate(GET_VS_RANGE_TEMPLATE);
		vsRangeTemplate.process(data, writer);
		writer.flush();
		
		return writer.toString();
	}
	
	public String getVerseRange(String start, String end) 
	throws /* SaxonApiException, */ TemplateException, IOException {
		// generate stylesheet
		Writer output = new StringWriter();
		
//		// get the XSLT stream source
//		StreamSource xslt = new StreamSource(
//				new StringReader(
//						this.getVsRangeTemplate(start, end)));
//		
//		// get the XML source file 
//		StreamSource sblgnt = new StreamSource(
//				new File(sourceDirectory, "Luke.xml"));
//		
//		long starttime = System.currentTimeMillis();
//		Processor proc = new Processor(false);
//		XdmNode source = proc.newDocumentBuilder().build(sblgnt);
//		XsltExecutable exp = proc.newXsltCompiler().compile(xslt);
//		XsltTransformer trans = exp.load();
//		System.out.println("Time to compile: " + (System.currentTimeMillis() - starttime));
//		
//		starttime = System.currentTimeMillis();
//		Serializer out = proc.newSerializer(output);
//		trans.setInitialContextNode(source);
//		trans.setDestination(out);
//		trans.transform();
//		System.out.println("Time to process: " + (System.currentTimeMillis() - starttime));
//		
		return output.toString();
	}
}
