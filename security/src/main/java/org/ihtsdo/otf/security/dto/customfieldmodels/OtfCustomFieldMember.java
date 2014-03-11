package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldMember extends OtfCustomFieldModel {
	String val;

	public OtfCustomFieldMember(String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.MEMBER;
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

	@Override
	public void model2Vals() {
		vals = new String[2];
		vals[0] = getType().name();
		vals[1] = getVal();
	}

	@Override
	public void modelFromVals() {
		setVal(vals[1]);
	}

}
