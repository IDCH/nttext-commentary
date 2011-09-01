/**
 * 
 */
package openscriptures.text.importer;

import java.util.Stack;

import org.xml.sax.Attributes;

public class ProcessingPath {
    private Stack<PathElement> path = new Stack<PathElement>();
    private final StringBuilder sb = new StringBuilder();
    
    public boolean matches(String test) {
        if (path.size() == 0)
            return false;
        
        int ix = test.indexOf('/');
        if (ix < 0) {
            return this.path.peek().name.equals(test);
        } else if (ix == 0) {
            return this.toString().startsWith(test);
        } else if (ix > 0) {
            return this.toString().endsWith(test);
        } else {
            assert false : "unreachable code";
            return false;
        }
    }
    
    public PathElement push(
            String nsURI, String name, String qName, Attributes attrs) {
        PathElement parent = (this.path.size() > 0) ? this.path.peek() : null;
        PathElement e = new PathElement(parent, sb, 
                nsURI, name, qName, attrs);
        
        this.path.push(e);
        
        return e;
    }
    
    public PathElement peek() {
        return this.path.peek();
    }
    
    public PathElement pop() {
        PathElement e = this.path.pop();
        e.end = sb.length();
        
        return e;
    }
    
    public void characters(char[] ch, int start, int length) {
        sb.append(ch, start, length);
    }
    
    
    public int size() {
        return this.path.size();
    }
    
    public String toString() {
        if (this.path.size() > 0)
            return this.path.peek().toPath();
        else 
            return "";
    }
}