package openscriptures.text;

/**
 * Defines an attribute of a structural component. This corresponds more or less directly
 * to a XML attribute in an OSIS (or other) document. 
 * @author Neal Audenaert
 */
public class StructureAttribute {
	// TODO should this model XML attributes so closely? Should we allow multi-valued 
	//		attributes? 
	// TODO add type validation (String, Boolean, Date/Time, language, osisRef, osisID
	
	String name;
	String value;
	
	public StructureAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}