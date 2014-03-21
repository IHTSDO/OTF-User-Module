package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldKeyVal extends OtfCustomFieldModel {

	private String key;
	private String val;

	public OtfCustomFieldKeyVal(final String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.DEFAULT;
	}

	@Override
	public final void model2Vals() {
		vals = new String[3];
		vals[0] = getType().name();
		vals[1] = getKey();
		vals[2] = getVal();

	}

	@Override
	public final void modelFromVals() {
		setKey(vals[1]);
		setVal(vals[2]);
	}

	public final String getKey() {
		if (key == null) {
			key = "";
		}
		return key;
	}

	public final void setKey(final String keyIn) {
		key = keyIn;
	}

	public final String getVal() {
		if (val == null) {
			val = "";
		}
		return val;
	}

	public final void setVal(final String valIn) {
		val = valIn;
	}

}
