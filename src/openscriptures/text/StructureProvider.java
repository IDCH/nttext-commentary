/**
 * 
 */
package openscriptures.text;

/**
 * Defines a controller interface that allows for the retrieval and (optionally) creation
 * of <tt>Structure</tt>s for a given work. Since a major goal of <tt>openscriptures.text</tt> 
 * is to enable multiple independent content providers to create annotations on a single base
 * text, often available over the Web via a RESTful API, it is necessary to provide a 
 * mechanism to persist and retrieve those structures that is independent of the work 
 * itself.
 *  
 * @author Neal Audenaert
 */
public interface StructureProvider {

}
