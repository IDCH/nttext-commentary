/**
 * 
 */
package openscriptures.text.impl.mem;

import openscriptures.text.Work;
import openscriptures.text.impl.BasicToken;

/**
 * @author Neal Audenaert
 */
public class MemToken extends BasicToken {
    
    protected MemToken(Work work, int position, String text) {
        super(work, position, text);
    }
}
