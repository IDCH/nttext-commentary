/**
 * 
 */
package openscriptures.text.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import openscriptures.text.Structure;
import openscriptures.text.StructureAttribute;
import openscriptures.text.Token;
import openscriptures.text.TokenSequence;
import openscriptures.text.Work;

/**
 * @author Neal Audenaert
 */
public class BasicStructure implements Structure {
	
	/** Maps input tag and/or structure names to output structures. This may be used to 
	 * transform input structures from one schema to another as needed. */
	public static Map<String, String> ELEMENT_CHOICES = new HashMap<String, String>();
	
	/* Static initialization block. */
	static {
		// TODO these should be made more robust and drawn from an XML file or other 
		//		extensible representation.
		
		// Element list
        ELEMENT_CHOICES.put("a", "a");
        ELEMENT_CHOICES.put("abbr", "abbr");
        ELEMENT_CHOICES.put("actor", "actor");
        ELEMENT_CHOICES.put("caption", "caption");
        ELEMENT_CHOICES.put("castGroup", "castGroup");
        ELEMENT_CHOICES.put("castItem", "castItem");
        ELEMENT_CHOICES.put("castList", "castList");
        ELEMENT_CHOICES.put("catchWord", "catchWord");
        ELEMENT_CHOICES.put("cell", "cell");
        ELEMENT_CHOICES.put("chapter", "chapter");
        ELEMENT_CHOICES.put("closer", "closer");
        ELEMENT_CHOICES.put("contributor", "contributor");
        ELEMENT_CHOICES.put("coverage", "coverage");
        ELEMENT_CHOICES.put("creator", "creator");
        ELEMENT_CHOICES.put("date", "date");
        ELEMENT_CHOICES.put("description", "description");
        //"div", (no need for this since promoting div[type] to full element named `type`)
        ELEMENT_CHOICES.put("divineName", "divineName");
        ELEMENT_CHOICES.put("figure", "figure");
        ELEMENT_CHOICES.put("foreign", "foreign");
        ELEMENT_CHOICES.put("format", "format");
        ELEMENT_CHOICES.put("head", "head");
        ELEMENT_CHOICES.put("header", "header");
        ELEMENT_CHOICES.put("hi", "hi");
        ELEMENT_CHOICES.put("identifier", "identifier");
        ELEMENT_CHOICES.put("index", "index");
        ELEMENT_CHOICES.put("inscription", "inscription");
        ELEMENT_CHOICES.put("item", "item");
        ELEMENT_CHOICES.put("l", "l");
        ELEMENT_CHOICES.put("label", "label");
        ELEMENT_CHOICES.put("language", "language");
        ELEMENT_CHOICES.put("lb", "lb");
        ELEMENT_CHOICES.put("lg", "lg");
        ELEMENT_CHOICES.put("list", "list");
        ELEMENT_CHOICES.put("mentioned", "mentioned");
        //"milestone",      (n/a since all strucutres milestoned)
        //"milestoneEnd",   (n/a since all strucutres milestoned)
        //"milestoneStart", (n/a since all strucutres milestoned)
        ELEMENT_CHOICES.put("name", "name");
        ELEMENT_CHOICES.put("note", "note");
        ELEMENT_CHOICES.put("osis", "osis");
        ELEMENT_CHOICES.put("osisCorpus", "osisCorpus");
        ELEMENT_CHOICES.put("osisText", "osisText");
        ELEMENT_CHOICES.put("p", "p");
        ELEMENT_CHOICES.put("publisher", "publisher");
        ELEMENT_CHOICES.put("q", "q");
        ELEMENT_CHOICES.put("rdg", "rdg");
        ELEMENT_CHOICES.put("rdgGrp", "rdgGrp");
        ELEMENT_CHOICES.put("refSystem", "refSystem");
        ELEMENT_CHOICES.put("reference", "reference");
        ELEMENT_CHOICES.put("relation", "relation");
        ELEMENT_CHOICES.put("revisionDesc", "revisionDesc");
        ELEMENT_CHOICES.put("rights", "rights");
        ELEMENT_CHOICES.put("role", "role");
        ELEMENT_CHOICES.put("roleDesc", "roleDesc");
        ELEMENT_CHOICES.put("row", "row");
        ELEMENT_CHOICES.put("salute", "salute");
        ELEMENT_CHOICES.put("scope", "scope");
        ELEMENT_CHOICES.put("seg", "seg");
        ELEMENT_CHOICES.put("seq", "seq");
        ELEMENT_CHOICES.put("signed", "signed");
        ELEMENT_CHOICES.put("source", "source");
        ELEMENT_CHOICES.put("speaker", "speaker");
        ELEMENT_CHOICES.put("speech", "speech");
        ELEMENT_CHOICES.put("subject", "subject");
        ELEMENT_CHOICES.put("table", "table");
        ELEMENT_CHOICES.put("teiHeader", "teiHeader");
        ELEMENT_CHOICES.put("title", "title");
        ELEMENT_CHOICES.put("titlePage", "titlePage");
        ELEMENT_CHOICES.put("transChange", "transChange");
        ELEMENT_CHOICES.put("type", "type");
        ELEMENT_CHOICES.put("verse", "verse");
        ELEMENT_CHOICES.put("w", "w");
        ELEMENT_CHOICES.put("work", "work");

        // Promoting div[type] elements to elements of the name [type]
        ELEMENT_CHOICES.put("acknowledgement", "acknowledgement");
        ELEMENT_CHOICES.put("afterword", "afterword");
        ELEMENT_CHOICES.put("annotant", "annotant");
        ELEMENT_CHOICES.put("appendix", "appendix");
        ELEMENT_CHOICES.put("article", "article");
        ELEMENT_CHOICES.put("back", "back");
        ELEMENT_CHOICES.put("body", "body");
        ELEMENT_CHOICES.put("book", "book");
        ELEMENT_CHOICES.put("bookGroup", "bookGroup");
        //"chapter", (use existing element above)
        ELEMENT_CHOICES.put("colophon", "colophon");
        ELEMENT_CHOICES.put("commentary", "commentary");
        ELEMENT_CHOICES.put("concordance", "concordance");
        ELEMENT_CHOICES.put("coverPage", "coverPage");
        ELEMENT_CHOICES.put("dedication", "dedication");
        ELEMENT_CHOICES.put("devotional", "devotional");
        ELEMENT_CHOICES.put("entry", "entry");
        ELEMENT_CHOICES.put("front", "front");
        ELEMENT_CHOICES.put("gazetteer", "gazetteer");
        ELEMENT_CHOICES.put("glossary", "glossary");
        ELEMENT_CHOICES.put("imprimatur", "imprimatur");
        //"index", (use existing element above)
        ELEMENT_CHOICES.put("introduction", "introduction");
        ELEMENT_CHOICES.put("majorSection", "majorSection");
        ELEMENT_CHOICES.put("map", "map");
        //"paragraph", (use existing element above)
        ELEMENT_CHOICES.put("part", "part");
        ELEMENT_CHOICES.put("preface", "preface");
        ELEMENT_CHOICES.put("section", "section");
        ELEMENT_CHOICES.put("subSection", "subSection");
        ELEMENT_CHOICES.put("summary", "summary");
        //"titlePage", (use existing element above)
        
        // New elements
        ELEMENT_CHOICES.put("page", "page"); // used to preserve page boundaries; TEI?
        
        // Proposed
        //ELEMENT_CHOICES.put("doubted", "doubted"); //level1 and level2? rdg
	}

//========================================================================================
// MEMBER VARIABLES
//========================================================================================
	// TODO we should allow for relationships between structures, including hierarchical.  
	
	protected UUID uuid;
	
	/**
	 * The name of the OSIS.
	 * XXX This seems like it ought to be the name of the structure. Since this is a 
	 * 	   standoff model, it seems like it ought to be something that can be extended 
	 *     beyond the confines of OSIS.
	 */
	protected String element;
	
	/**
	 * The identifier for this strcuture. 
	 * XXX Does it have to be an OSIS id? Do all structures have to have an OSIS id?
	 */
	protected String osisId;
	
	/** 
	 * Must be same as start/end_*_token.work. Must not be a variant work; use the 
	 * variant_bits to select for it
	 */
	protected String work;
	
	/** URL for where this structure came from; used for base to Token.relative_source_url */
	protected String sourceUrl;
	
	/** The order where this appears in the work. Note that this does not need to be unique
	 *  since two structures could start at the same position. */
	protected String position;
	
	// FIXME we need some sort of payload. 
	
	/** The token that starts the structure's content; this may or may not include the
	 *  start_marker, like quotation marks. <del>If null, then tokens should be discovered 
	 *  via StructureToken.</del>
	 */
	protected Token startToken;
	
	/** Same as start_token, but for the end. */
	protected Token endToken;
	
	/**
	 * The optional token that marks the start of the structure. This marker may be included
	 * (inside) in the start_token/end_token range as in the example of quotation marks, or 
	 * it may excluded (outside) as in the case of paragraph markers which are double 
	 * linebreaks. Outside markers may overlap (be shared) among multiple paragraphs' 
	 * start/end_markers, whereas inside markers may not.
	 * 
	 * TODO this is really unclear to me. I think the quatation marks make sense, but the
	 *      paragraph seems like that should be a display issue. Assuming that we are 
	 *      normalizing spaces on import, we shouldn't end up with multiple line breaks in the 
	 *      token sequence. 
	 */
	protected Token startMarker;
	
	/** Same as start_marker, but for the end. */
	protected Token endMarker;

	/** A number that may be associated with this structure, such as a chapter or verse number;
	 *  corresponds to OSIS @n attribute.
	 *  
	 *  @deprecated This should use the attributes instead
	 */
	protected String numericalStart;
	
	/**
	 * If the structure spans multiple numerical designations, this is used
	 * @deprecated This should use the attributes instead
	 */
	protected String numericalEnd;
	
	protected Map<String, StructureAttribute> attributes = 
		new HashMap<String, StructureAttribute>();
	
//========================================================================================
// CONSTRUCTORS
//========================================================================================
	
	/**
	 * 
	 */
	public BasicStructure() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenSequence getTokens() {
		return null;
	}
	
	public StructureAttribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	public String setAttribute(String name, String value) {
		StructureAttribute previous = 
			attributes.put(name, new StructureAttribute(name, value));
		
		return (previous != null) ? previous.getValue() : null;
	}

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#getText()
     */
    @Override
    public String getText() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#contains(openscriptures.text.Token)
     */
    @Override
    public boolean contains(Token o) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#get(int)
     */
    @Override
    public Token get(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#size()
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#iterator()
     */
    @Override
    public Iterator<Token> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#iterator(int)
     */
    @Override
    public Iterator<Token> iterator(int startAt) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#listIterator()
     */
    @Override
    public ListIterator<Token> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#listIterator(int)
     */
    @Override
    public ListIterator<Token> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#indexOf(openscriptures.text.Token)
     */
    @Override
    public int indexOf(Token token) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#subSequence(int, int)
     */
    @Override
    public TokenSequence subSequence(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#toArray()
     */
    @Override
    public Token[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.TokenSequence#toArray(openscriptures.text.Token[])
     */
    @Override
    public Token[] toArray(Token[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getUUID()
     */
    @Override
    public UUID getUUID() {
        return uuid;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getOsisId()
     */
    @Override
    public String getOsisId() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getStructureName()
     */
    @Override
    public String getStructureName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getWork()
     */
    @Override
    public Work getWork() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getStartToken()
     */
    @Override
    public Token getStartToken() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#getEndToken()
     */
    @Override
    public Token getEndToken() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see openscriptures.text.Structure#listAttributes()
     */
    @Override
    public List<String> listAttributes() {
        // TODO Auto-generated method stub
        return null;
    }
}
