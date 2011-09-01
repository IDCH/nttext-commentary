/**
 * 
 */
package openscriptures.text.importer;


public abstract class StructureHandler {
    protected Context ctx;
    
    public StructureHandler() {
    }
    
    public void setContext(Context context) {
        this.ctx = context;
    }
    
    public boolean matchesEnd(PathElement p) {
        return this.matchesStart(p);
    }
    
    public abstract boolean matchesStart(PathElement p);
    public abstract void start(PathElement p); 
    public abstract void end(PathElement p);
}