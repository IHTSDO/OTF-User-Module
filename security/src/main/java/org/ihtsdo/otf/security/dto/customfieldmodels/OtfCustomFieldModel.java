package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public abstract class OtfCustomFieldModel {

	protected String[] vals;
	protected String key;
	protected OtfCustomField.CustomType type;

	public OtfCustomFieldModel() {
		super();
	}

	public OtfCustomFieldModel(final String keyIn, final String[] valsIn) {
		super();
		setKey(keyIn);
		setVals(valsIn);
		modelFromVals();
	}

	public abstract void model2Vals();

	public abstract void modelFromVals();

	public abstract String getCollectionTitle();

	public abstract String getNewTitle();

	public abstract Map<String, String> getLabelValuesMap();

	public abstract OtfCustomField.CustomType getType();

	public final String[] getVals() {
		if (vals == null || vals.length == 0) {
			model2Vals();
		}
		return vals;
	}

	public final void setVals(final String[] valsIn) {
		vals = valsIn;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= vals.length - 1; i++) {
			sb.append(i).append(" : ").append(vals[i]).append("\n");
		}
		return sb.toString();
	}

	public final String getHtmlTextInput(String name, String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<input type=\"text\" class=\"base_web_text_input\" name=\"");
		sbuild.append(name);
		sbuild.append("\" value=\"");
		sbuild.append(value);
		sbuild.append("\">");
		return sbuild.toString();
	}

	public final String getKey() {
		return key;
	}

	public final void setKey(String keyIn) {
		key = keyIn;
	}

}
