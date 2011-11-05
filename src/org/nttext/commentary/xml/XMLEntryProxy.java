/**
 * 
 */
package org.nttext.commentary.xml;

import java.io.File;
import java.text.Collator;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRange;
import org.idch.persist.RepositoryAccessException;
import org.idch.texts.Structure;
import org.idch.texts.StructureRepository;
import org.idch.texts.TextModule;
import org.idch.texts.Work;
import org.idch.texts.WorkRepository;
import org.idch.texts.structures.Verse;
import org.idch.util.xml.XMLUtil;
import org.nttext.commentary.CommentaryModule;
import org.nttext.commentary.EntryInstance;
import org.nttext.commentary.InstanceRepository;
import org.nttext.commentary.VUReference;
import org.nttext.commentary.VURepository;
import org.nttext.commentary.VariantReading;
import org.nttext.commentary.VariantReadingRepository;
import org.nttext.commentary.VariationUnit;
import org.nttext.commentary.persist.mysql.MySQLCommentaryModule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Neal Audenaert
 */
public class XMLEntryProxy {

    /** 
     * Returns the contents of a CDATA element. This is used to extract the contents of 
     * the commentary element of VUs and the overview of entries.
     *  
     * @param parentElement
     * @param cdataElementName
     * @return
     */
    private static String getCDataBlock(Element parentElement, String cdataElementName) {
        NodeList nodeList = parentElement.getElementsByTagName(cdataElementName);
        if (nodeList.getLength() != 1) 
            return null;
        Element overview = (Element)nodeList.item(0);
        
        StringBuilder sb = new StringBuilder();
        NodeList children = overview.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node cdata = children.item(i);
            if (cdata.getNodeType() != Node.CDATA_SECTION_NODE)
                continue;
            
            sb.append(cdata.getTextContent().trim());
        }
        
        return sb.toString();
    }
    
    
    //===================================================================================
    // MEMBER VARIABLES
    //===================================================================================
    
    
    CommentaryModule module;
    
    //===================================================================================
    // CONSTRUCTORS
    //===================================================================================
    /**
     * 
     * @param module
     */
    XMLEntryProxy(CommentaryModule module) {
        this.module = module;
    }
    
    //===================================================================================
    // IMPORT METHOD
    //===================================================================================
    
    EntryInstance importEntry(Element e) {
        InstanceRepository repo = module.getInstanceRepository();
        EntryInstance instance = null;
        
        String passage = e.getAttribute("passage");
        VerseRange vsRange = new VerseRange(passage);
        instance = repo.find(vsRange);
        if (instance != null) {
            //  TODO duplicate entry - throw an exception
            return null; 
        }
        
        String overview = getCDataBlock(e, "overview");
        instance = repo.create(vsRange);
        instance.setOverview(overview);
        
        // create variation units
        NodeList nodes = e.getElementsByTagName("variationUnits");
        if (nodes.getLength() != 1) {
            // TODO we have a problem - throw an exception 
            return null;
        }
        
        Element variationUnits = (Element)nodes.item(0);
        NodeList vus = variationUnits.getElementsByTagName("vu");
        for (int i = 0; i < vus.getLength(); i++) {
            Element vuElement = (Element)vus.item(i);
            VariationUnit vu = createVariationUnit(vuElement);
            repo.associate(instance, vu);
        }
        
        repo.save(instance);
        return instance;
    }
    
    private VariationUnit createVariationUnit(Element vuElement) {
        VURepository vuRepo = module.getVURepository();
        VariantReadingRepository rdgRepo = module.getRdgRepository();
        
        String passage = vuElement.getAttribute("passage");
        String commentary = getCDataBlock(vuElement, "commentary");
        
        VariationUnit vu = vuRepo.create(new VerseRange(passage));
        vu.setCommentary(commentary);
        
        // TODO process references
        NodeList vsRefs = vuElement.getElementsByTagName("verseReference");
        for (int i = 0; i < vsRefs.getLength(); i++) {
            createVUReference(vu, (Element)vsRefs.item(i));
        }
        
        // process readings
        NodeList readings = vuElement.getElementsByTagName("readings");
        if (readings.getLength() == 1) {
            NodeList rdgs = ((Element)readings.item(0)).getElementsByTagName("rdg");
            for (int i = 0; i < rdgs.getLength(); i++) {
                Element rdgElement = (Element)rdgs.item(i);
                
                VariantReading rdg = rdgRepo.create(vu);
                rdg.setEnglishReading(rdgElement.getAttribute("eng"));
                rdg.setGreekReading(rdgElement.getAttribute("grc"));
                rdg.setWitnessDescription(getWitnesses(rdgElement));
            
                rdgRepo.save(rdg);
            }
        }
        
        vuRepo.save(vu);
        return vu;
    }
    
    private VUReference createVUReference(VariationUnit vu, Element refElement) {
        TextModule textModule = module.getTextRepository();
        WorkRepository workRepo = module.getWorkRepository();
        StructureRepository structRepo = module.getStructureRepository();
        
        Passage passage = vu.getPassage();
        String workAbbr = refElement.getAttribute("work");
        Work work = workRepo.findByAbbr(workAbbr).get(0);
        if (work == null) {
            // FIXME better error handling, for now, we know it works.
            System.err.println("Could not find work: " + workAbbr);
            return null;
        }
        
        // TODO need to create a better structure 
        Verse vs = Verse.getVerse(textModule, work, passage.getFirst().toString());
        String text = refElement.getAttribute("text");

        // contextual search options
        String prefix = refElement.getAttribute("prefix");
        String ct = refElement.getAttribute("ct");

        Structure refStructure = null;
        if (prefix != null) {
            refStructure = textModule.createStructure(vs, "VURef", text, prefix);
        } else if (ct != null && StringUtils.isNumeric(ct)) {
            refStructure = textModule.createStructure(vs, "VURef", text, Integer.parseInt(ct));
        } else {
            refStructure = textModule.createStructure(vs, "VURef", text);
        }
        
        VUReference ref = VUReference.init(refStructure, vu);
        structRepo.save(ref);
        
        return ref;
    }
    
    private String getWitnesses(Element e) {
        StringBuilder sb = new StringBuilder();
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                sb.append(node.getNodeValue());
            }
        }
        
        return sb.toString();
    }
    
    
    //===================================================================================
    // MAIN METHOD
    //===================================================================================
    
    public static void main(String[] args) {
        File input = new File("data/commentary/1Pet_1.xml");

        String str1 = "ἀλλ εἰ ἀγαθοποιοῦντες";
        String str2 = "αλλ εἰ ἀγαθοποιοῦντες";
        
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.FULL_DECOMPOSITION);
        System.out.println(collator.compare(str1, str2));
        
//        for (int i = 0; i < str1.length() && i < str2.length(); i++) {
//            char a = str1.charAt(i), b = str2.charAt(i);
//            System.out.println((int)a + " = " + (int)b + " is " + (a == b));
//        }
        XMLEntryProxy proxy;
        try {
            MySQLCommentaryModule repo = MySQLCommentaryModule.get();
//            Structure s = repo.getStructureRepository().find(19667L);
//            System.out.println(repo.getTextRepository().toString(s));
//            
//            s = repo.getStructureRepository().find(19668L);
//            System.out.println(repo.getTextRepository().toString(s));
//            
//            s = repo.getStructureRepository().find(19669L);
//            System.out.println(repo.getTextRepository().toString(s));
//            
//            s = repo.getStructureRepository().find(19670L);
//            System.out.println(repo.getTextRepository().toString(s));
            
//            proxy = new XMLEntryProxy(MySQLCommentaryModule.get());
//            Document doc = XMLUtil.getXmlDocument(input);
//            Element el = doc.getDocumentElement();
//            NodeList entries = el.getChildNodes();
//            for (int i = 0; i < entries.getLength(); i++) {
//                Node entry = entries.item(i);
//                
//                if (entry.getNodeType() != Node.ELEMENT_NODE) {
//                    // skip text and comment nodes
//                    continue;
//                }
//                
//                proxy.importEntry((Element)entry);
//            }
              
            System.out.println("done.");
        } catch (RepositoryAccessException e) {
            System.err.println("Error accessing repository: " + e);
        }
    }
}
