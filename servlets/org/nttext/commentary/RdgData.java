/**
 * 
 */
package org.nttext.commentary;

/**
 * @author Neal Audenaert
 */
public class RdgData {
    
    VUData vu;
    VariantReading rdg;
    char index;
    
    RdgData(VUData vu, VariantReading rdg, char index) {
        this.vu = vu;
        this.rdg = rdg;
        this.index = index;
    }

    
    public char getIndex() {
        return index;
    }
    
    public String getClassname() {
        return vu.getClassname() + Character.toLowerCase(index);
    }
    
    public String getEnglish() {
        String str = rdg.getEnglishReading();
        return (str != null) ? str : "";
    }
    
    public String getGreek() {
        String str = rdg.getGreekReading();
        return (str != null) ? str : "";
    }
    
    public String getWitnesses() {
        return rdg.getWitnessDescription();
    }
}
