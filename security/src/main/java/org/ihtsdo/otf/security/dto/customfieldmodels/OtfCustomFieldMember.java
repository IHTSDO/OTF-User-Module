package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldMember extends OtfCustomFieldModel {
	private String val;

	public OtfCustomFieldMember(final String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.MEMBER;
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

	@Override
	public final void model2Vals() {
		vals = new String[2];
		vals[0] = getType().name();
		vals[1] = getVal();
	}

	@Override
	public final void modelFromVals() {
		setVal(vals[1]);
	}

}
