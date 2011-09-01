/**
 * 
 */
package openscriptures.text;

/**
 * @author Neal Audenaert
 */
public class InvalidTokenException extends RuntimeException {
    
    private final Token t;
    
    @SuppressWarnings("unused")
    private final String msg;
    
    /**
     * @param string
     */
    public InvalidTokenException(String msg) {
        super("InvalidToken: " + msg);
        
        this.t = null;
        this.msg = msg;
    }
    
    public InvalidTokenException(String msg, Token t) {
        super("InvalidToken: " + msg);
        
        this.t = t;
        this.msg = msg;
    }
    
    public Token getToken() {
        return this.t;
    }
}
