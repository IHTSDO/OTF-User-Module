package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public abstract class OtfCustomFieldModel {

	protected String[] vals;
	protected OtfCustomField.CustomType type;

	public OtfCustomFieldModel(final String[] valsIn) {
		super();
		setVals(valsIn);
		modelFromVals();
	}

	public abstract void model2Vals();

	public abstract void modelFromVals();

	public final String[] getVals() {
		if (vals == null || vals.length == 0) {
			model2Vals();
		}
		return vals;
	}

	public final void setVals(final String[] valsIn) {
		vals = valsIn;
	}

	public final OtfCustomField.CustomType getType() {
		return type;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= vals.length - 1; i++) {
			sb.append(i).append(" : ").append(vals[i]).append("\n");
		}
		return sb.toString();
	}

}
