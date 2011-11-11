/**
 * 
 */
package org.nttext.commentary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neal Audenaert
 */
public class VUData {

    private static char A = 'A';
    
    private int ix;
    private VariationUnit vu;
    
    /**
     * @param vu
     */
    public VUData(VariationUnit vu, int ix) {
        this.ix = ix;
        this.vu = vu;
    }
    
    public int getIndex() {
        return ix;
    }
    
    public String getClassname() {
        return "var-" + ((ix < 10) ? "0" + ix : ix);
    }
    
    public String getCommentary() { 
        return vu.getCommentary();
    }
    
    public int getNumberOfReadings() {
        return vu.getReadings().size();
    }
    
    public List<RdgData> getVariantReadings() {
        List<RdgData> readings = new ArrayList<RdgData>();
        
        int ix = 0;
        List<VariantReading> rdgs = vu.getReadings();
        for (VariantReading rdg : rdgs) {
            readings.add(new RdgData(this, rdg, (char)(A + ix++)));
        }
        
        return readings;
    }

}
