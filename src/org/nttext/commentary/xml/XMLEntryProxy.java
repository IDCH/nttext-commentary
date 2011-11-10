/**
 * 
 */
package org.nttext.commentary.xml;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.idch.bible.ref.Passage;
import org.idch.bible.ref.VerseRange;
import org.idch.bible.ref.VerseRef;
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
        // TODOD should move this to XML Utils
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
    InstanceRepository repo;
    VURepository vuRepo;
    VariantReadingRepository rdgRepo;
    
    Set<String> missingWorks = new HashSet<String>();
    
    //===================================================================================
    // CONSTRUCTORS
    //===================================================================================
    /**
     * 
     * @param module
     */
    XMLEntryProxy(CommentaryModule module) {
        this.module = module;
        this.repo = module.getInstanceRepository();
        this.vuRepo = module.getVURepository();
        this.rdgRepo = module.getRdgRepository();
    }
    
    //===================================================================================
    // IMPORT METHOD
    //===================================================================================
    
    /**
     * Extracts the commentary for an entry. 
     * 
     * @param e
     * @param vsRange
     * @param instance
     */
    private EntryInstance getOverview(Element e, VerseRange vsRange) {
        String overview = XMLEntryProxy.getCDataBlock(e, "overview");
        EntryInstance instance = repo.create(vsRange);
        instance.setOverview(overview);
        
        return instance;
    }
    
    /** 
     * Extracts the variation units associated with an entry.
     * @param e
     * @param instance
     */
    private void getVariationUnits(Element e, EntryInstance instance) {
        NodeList nodes = e.getElementsByTagName("variationUnits");
        if (nodes.getLength() != 1) 
            return;
        
        Element variationUnits = (Element)nodes.item(0);
        NodeList vus = variationUnits.getElementsByTagName("vu");
        for (int i = 0; i < vus.getLength(); i++) {
            Element vuElement = (Element)vus.item(i);
            VariationUnit vu = this.createVariationUnit(vuElement);
            repo.associate(instance, vu);
        }
    }
    
    public EntryInstance importEntry(Element e) {
        EntryInstance instance = null;
        
        String passage = e.getAttribute("passage");
        VerseRange vsRange = new VerseRange(passage);
        instance = repo.find(vsRange);
        if (instance != null) {
            //  TODO duplicate entry - throw an exception
            System.err.println("Duplicate Entry: " + passage);
            return null; 
        }
        
        System.out.println("Importing entry for " + passage);
        instance = this.getOverview(e, vsRange);
        this.getVariationUnits(e, instance);
        
        repo.save(instance);
        return instance;
    }

    /**
     * Processes the references for a variation unit
     * @param vuElement
     * @param vu
     */
    private void processReferences(Element vuElement, VariationUnit vu) {
        NodeList vsRefs = vuElement.getElementsByTagName("verseReference");
        for (int i = 0; i < vsRefs.getLength(); i++) {
            createVUReference(vu, (Element)vsRefs.item(i));
        }
    }
    
    /**
     * Helper function that processes all readings for a particular variation unit.
     * 
     * @param vuElement
     * @param vu
     */
    private void processReadings(Element vuElement, VariationUnit vu) {
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
//                System.out.println("      Created variant reading: " + 
//                            rdg.getEnglishReading() + " (" + rdg.getGreekReading() + "): " + 
//                            rdg.getWitnessDescription());
            }
        }
    }
    
    private VariationUnit createVariationUnit(Element vuElement) {
        String passage = vuElement.getAttribute("passage");
        String commentary = getCDataBlock(vuElement, "commentary");
        
//        System.out.println("   Creating variation unit for " + passage);
        VariationUnit vu = vuRepo.create(new VerseRange(passage));
        vu.setCommentary(commentary);
        
        this.processReferences(vuElement, vu);
        this.processReadings(vuElement, vu);
        
        vuRepo.save(vu);
        return vu;
    }
    
    private Structure getPassageReference(TextModule module, Passage passage, String workAbbr) {
        if (this.missingWorks.contains(workAbbr))
            return null;
        
        // TODO move to TextModule
        WorkRepository workRepo = module.getWorkRepository();
        
        List<Work> works = workRepo.findByAbbr(workAbbr);
        if (works.size() == 0) {
            // just ignore references to works that we don't have
            System.out.println("Cannot find references for work. No such work: " + workAbbr);
            this.missingWorks.add(workAbbr);
            return null;
        } else if (works.size() > 1) {
            System.out.println("Multiple works found for: " + workAbbr + ". Using first.");
        }
        
        Work work = workRepo.findByAbbr(workAbbr).get(0);
        VerseRef firstRef = passage.getFirst();
        VerseRef lastRef = passage.getLast();
        
        Structure structure = null;
        if (!firstRef.equals(lastRef)) {
            Verse startVs = Verse.getVerse(module, work, firstRef.toString());
            Verse endVs = Verse.getVerse(module, work, lastRef.toString());
            structure = new Structure(work, "passage", 
                    startVs.getStartToken(), endVs.getEndToken());
        } else {
            structure = Verse.getVerse(module, work, firstRef.toString());
        }
        
        return structure;
    }
    
    /**
     * 
     * @param vu
     * @param refElement
     * @return
     */
    private VUReference createVUReference(VariationUnit vu, Element refElement) {
        TextModule textModule = module.getTextRepository();
        StructureRepository structRepo = module.getStructureRepository();
        
        String abbr = refElement.getAttribute("work");
        Structure passage = this.getPassageReference(textModule, vu.getPassage(), abbr);
        if (passage == null)
            return null;            // we can't find this work
        
        String text = refElement.getAttribute("text");

        // contextual search options
        String prefix = StringUtils.trimToNull(refElement.getAttribute("prefix"));
        String ct = StringUtils.trimToNull(refElement.getAttribute("ct"));

        Structure refStructure = null;
        if (text.equalsIgnoreCase("ALL")) {
            refStructure = textModule.createStructure(passage, "VURef");
        } else if (prefix != null) {
            refStructure = textModule.createStructure(passage, "VURef", text, prefix);
        } else if (ct != null && StringUtils.isNumeric(ct)) {
            refStructure = textModule.createStructure(passage, "VURef", text, Integer.parseInt(ct));
        } else {
            refStructure = textModule.createStructure(passage, "VURef", text);
        }
        
        VUReference ref = null;
        if (refStructure == null) {
            System.out.println("Could not find reference (" + abbr + "): " + vu.getPassage());
            System.out.println("        Passage: " + textModule.toString(passage));
            System.out.println("   Text to find: " + text);
        } else { 
//            System.out.println("Found reference (" + abbr + "): " + vu.getPassage());
//            System.out.println("        Passage: " + textModule.toString(passage));
//            System.out.println("   Matched Text: " + text);
//            System.out.println("   Matched Text: " + textModule.toString(refStructure));
            
            ref = VUReference.init(refStructure, vu);
            structRepo.save(ref);
        }
        
        
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
    public static Structure resolve(TextModule module, String workAbbr, VerseRef ref) {
        List<Work> works = module.getWorkRepository().findByAbbr(workAbbr);
        
        Work work = null;
        if (works.size() > 0) {
            work = works.get(0);
        } else {
            return null;
        }
        
        StructureRepository structureRepository = module.getStructureRepository();
        SortedSet<Structure> structures = structureRepository.find(work, "verse", "osisId", ref.toString());
        return (structures.size() > 0) ? structures.first() : null;
    }
    
    public static void main(String[] args) {
        File input = new File("data/commentary/Phil1.xml");
        XMLEntryProxy proxy;
        try {
            proxy = new XMLEntryProxy(MySQLCommentaryModule.get());
            Document doc = XMLUtil.getXmlDocument(input);
            Element el = doc.getDocumentElement();
            NodeList entries = el.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                
                if (entry.getNodeType() != Node.ELEMENT_NODE) {
                    // skip text and comment nodes
                    continue;
                }
                
                proxy.importEntry((Element)entry);
            }
              
            System.out.println("done.");
        } catch (RepositoryAccessException e) {
            System.err.println("Error accessing repository: " + e);
        }
    }
}
