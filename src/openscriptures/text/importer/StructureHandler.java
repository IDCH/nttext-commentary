/**
 * 
 */
package openscriptures.text.importer;

/**
 * Helper class to facilitate structure creation during SAX based importing of XML documents. 
 * This class abstracts the process of creating individual structures in response to the 
 * start and end tags encountered while parsing an XML file. When the <tt>Importer</tt> 
 * encounters an XML start tag, it invokes the <tt>{@link #matchesStart(PathElement)}</tt> 
 * method to check if the start handler should be called. If this method returns <tt>true<tt>,
 * it invokes the <tt>@{@link #start(PathElement)}</tt> method and returns control to the 
 * main SAX processor without invoking any handlers later on in the handler chain (although, 
 * see the note below for possible changes). This process is repeated for the XML end tags
 * invoking the <tt>{@link #matchesEnd(PathElement)}</tt> and 
 * <tt>@{@link #end(PathElement)}</tt> methods respectively.
 * 
 * <p>A <tt>StructureHandler</tt> may register itself with the import <tt>Context</tt> so that
 * other handlers have access to its state. For example, a <tt>BookHandler</tt> might register
 * itself between its <tt>{@link #start(PathElement)}</tt> and 
 * <tt>{@link #end(PathElement)}</tt> methods so that any chapter handlers encountered can 
 * retrieve add the current book structure as a parent.</tt></p>
 * 
 * <p>
 * TODO Add some good example/tutorial documentation on a Wiki. Probably use book/chapter/verse
 *      hierarchy along with notes and book titles to demonstrate setting the <tt>inText</tt> 
 *      flag.
 * <p> 
 * NOTE The <tt>Importer</tt> is still in alpha stage and the API and overall workflow are 
 *      subject to change. Notably, this may be updated so that all structure handlers are 
 *      tested for all start/end elements. This will allow multiple handlers to service a
 *      single tag. If this option is implemented, we may also implement as 'short circut' 
 *      mechanism that will allow a handler to prevent subsequent handlers from being invoked.
 *      For example, the handler for the header tag might indicate that subsequent handlers 
 *      should not be invoked. This would lead to performance improvements and simplify 
 *      handler implementation since, for example, the handler for body-level paragraphs 
 *      wouldn't need to check if the current path is in the header. 
 * 
 * @author Neal Audenaert
 * @see {@link Context}
 */
public abstract class StructureHandler {
    
    protected Context ctx;
    protected String name;
    
    @Deprecated
    public StructureHandler() {
    }
    
    /**
     * Instantiates a new <tt>StructureHandler</tt> with the specified name.
     * @param name The name of this handler.
     */
    public StructureHandler(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name of this handler. This name should be unique for all structure 
     * handlers loaded into an importer.
     *  
     * @return The name of this handler
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the context object to be used by this <tt>StructureHandler</tt>. The context 
     * object is used to manage contextual information that needs to be shared across
     * multiple <tt>StructureHandler</tt> implementations. 
     *  
     * @param context The import context.
     */
    void setContext(Context context) {
        this.ctx = context;
    }
    
    /** 
     * Indicates whether this <tt>StructureHandler</tt> should be invoked for the start tag
     * tag associated with the supplied <tt>PathElement</tt>. 
     *  
     * @param p The <tt>PathElement</tt> to test.
     * @return <tt>true</tt> if this <tt>StructureHandler</tt> should be applied, 
     *      <tt>false</tt> otherwise.
     * @see #start(PathElement)
     * @see #matchesEnd(PathElement) 
     */
    public abstract boolean matchesStart(PathElement p);
    
    /** 
     * Indicates whether this <tt>StructureHandler</tt> should be invoked for the end tag
     * tag associated with the supplied <tt>PathElement</tt>. By default, this returns 
     * the same value as <tt>matchesStart</tt>.
     *  
     * @param p The <tt>PathElement</tt> to test.
     * @return <tt>true</tt> if this <tt>StructureHandler</tt> should be applied, 
     *      <tt>false</tt> otherwise.
     * @see #end(PathElement)
     * @see #matchesStart(PathElement) 
     */
    boolean matchesEnd(PathElement p) {
        return this.matchesStart(p);
    }

    /**
     * Called to initiate in response to the start tag of a matching path element. This will 
     * typically perform some combination of the following steps.
     * 
     * <ul>
     *   <li>Start the process of creating a structure that will be closed during the 
     *       {@link #end(PathElement)} method. For example, in response to a 
     *       <tt>&lt;div type="book"&gt;</tt> tag, the start method might set-up a new
     *       book structure that will be closed when the corresponding <tt>&lt;/div&gt;</tt>
     *       is encountered. </li>
     *   <li>Fully create a new structure. For structures that mark a location but do not 
     *       span multiple tokens, it may be possible to handle the full process of creating
     *       the structure in the start method.</li>
     *   <li>Handle milestone elements by starting or completing the process of creating a 
     *       structure as appropriate. For example, on encountering a <tt>&lt;verse&gt;</tt>
     *       milestone element, this method might check for the presence of either the 
     *       <tt>sId</tt> or <tt>eId</tt> marker in order to start or complete the creation
     *       of a <tt>verse</tt> structure as appropriate.</li>
     *   <li>Do nothing. Some structures can be handled entirely at their end tag.</li>
     * </ul> 
     *       
     * @param p The path element whose start tag is being processed. 
     * @see #matchesStart(PathElement) 
     * @see #end(PathElement)
     */
    public abstract void start(PathElement p);
    
    /**
     * Called to initiate in response to the end tag of a matching path element. This will 
     * typically perform some combination of the following steps.
     * 
     * TODO update these
     * <ul>
     *   <li>Complete the process of creating a structure that was started by the 
     *       {@link #start(PathElement)} method. For example, in response to a the end tag
     *       corresponding to a book, the end method might be used to update the start and end
     *       tokens of the structure (since these were not known when the start tag was 
     *       encountered) and close any open sub-structures like chapters and verses.</li>
     *   <li>Fully create a new structure. For some structures, there may be no need to take 
     *       action based on the start tag and it may be possible to handle the full process 
     *       of creating the structure in the end method. This scenario is expected to be 
     *       rare.</li>
     *   <li>Do nothing. Some structures (especially milestones) can be handled entirely at 
     *       their start tag.</li>
     * </ul> 
     * 
     * @param p The path element whose end tag is being processed. 
     * @see #matchesEnd(PathElement)
     * @see #start(PathElement)
     */
    public abstract void end(PathElement p);

}