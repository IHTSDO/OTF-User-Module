package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldKeyVal extends OtfCustomFieldModel {

	String key;
	String val;

	public OtfCustomFieldKeyVal(String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.DEFAULT;
	}

	@Override
	public void model2Vals() {
		vals = new String[3];
		vals[0] = getType().name();
		vals[1] = getKey();
		vals[2] = getVal();

	}

	@Override
	public void modelFromVals() {
		setKey(vals[1]);
		setVal(vals[2]);
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
