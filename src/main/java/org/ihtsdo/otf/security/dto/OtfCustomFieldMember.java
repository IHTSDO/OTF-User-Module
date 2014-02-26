package org.ihtsdo.otf.security.dto;

public class OtfCustomFieldMember {
	String val;
	String[] vals;

	public OtfCustomFieldMember(String[] valsIn) {
		super();
		setVals(valsIn);
	}

	public String[] getVals() {
		if (vals == null || vals.length == 0) {
			vals = new String[2];
			vals[0] = OtfCustomField.CustomType.MEMBER.name();
			vals[1] = getVal();
		}
		return vals;
	}

	public void setVals(String[] valsIn) {
		vals = valsIn;
		setVal(vals[1]);
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
