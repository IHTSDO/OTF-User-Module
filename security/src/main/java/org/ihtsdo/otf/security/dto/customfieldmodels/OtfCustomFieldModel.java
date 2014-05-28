package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.ihtsdo.otf.security.dto.OtfBasicWeb;
import org.ihtsdo.otf.security.dto.OtfCustomField;

public abstract class OtfCustomFieldModel {

	protected String[] vals;
	protected String key;
	protected OtfCustomField.CustomType type;
	private OtfBasicWeb obw;
	public static final String SEP = "-xxx-";
	public static final String NEW_FORM_URL = "newForm/";

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

	public abstract String getNewFormURLWithContext();

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

	// public final String getHtmlTextInput(String name, String value) {
	// StringBuilder sbuild = new StringBuilder();
	// sbuild.append("<input type=\"text\" class=\"base_web_text_input\" name=\"");
	// sbuild.append(name);
	// sbuild.append("\" value=\"");
	// sbuild.append(value);
	// sbuild.append("\">");
	// return sbuild.toString();
	// }

	public final OtfBasicWeb getObw() {
		if (obw == null) {
			obw = new OtfBasicWeb();
		}
		return obw;
	}

	public final String getKey() {
		if (key == null) {
			key = "";
		}
		return key;
	}

	public final void setKey(String keyIn) {
		key = keyIn;
	}

	public Map<String, String> getStdLabelValuesMap() {
		return new LinkedHashMap<String, String>();
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

	public String getNewFormId() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getClass().getName()).append(".newForm");
		return sbuild.toString();
	}

	public String getNewFormURL() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(NEW_FORM_URL).append(getType().name());
		return sbuild.toString();
	}

	public String getUniqueControlName(String controlName, String id) {
		if (id == null) {
			id = getId();
		}
		return new StringBuilder().append(controlName).append(SEP).append(id)
				.toString();
	}

	public static final String getId() {
		return UUID.randomUUID().toString();
	}

}
