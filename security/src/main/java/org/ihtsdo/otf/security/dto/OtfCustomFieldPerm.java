package org.ihtsdo.otf.security.dto;

public class OtfCustomFieldPerm {

	String key;
	String val;
	String[] vals;

	public OtfCustomFieldPerm(String[] valsIn) {
		super();
		setVals(valsIn);
	}

	public String[] getVals() {
		if (vals == null || vals.length == 0) {
			vals = new String[3];
			vals[0] = OtfCustomField.CustomType.PERM.name();
			vals[1] = getKey();
			vals[2] = getVal();
		}
		return vals;
	}

	public void setVals(String[] valsIn) {
		vals = valsIn;
	}

	public String getKey() {
		if (key == null) {
			key = "";
		}
		return key;
	}

	public void setKey(String keyIn) {
		key = keyIn;
	}

	public String getVal() {
		if (val == null) {
			val = "";
		}
		return val;
	}

	public void setVal(String valIn) {
		val = valIn;
	}

}
