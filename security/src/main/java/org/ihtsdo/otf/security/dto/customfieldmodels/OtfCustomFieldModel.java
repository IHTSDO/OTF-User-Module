package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfBasicWeb;
import org.ihtsdo.otf.security.dto.OtfCustomField;

public abstract class OtfCustomFieldModel {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfCustomFieldModel.class.getName());

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
		if (vals != null) {
			modelFromVals();
		}
		if (vals == null) {
			model2Vals();
		}

	}

	public abstract void model2Vals();

	public abstract void modelFromVals();

	public abstract void valFromParamDTO(OtfCustomFieldParamDTO ocfp);

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
		sb.append("Cust FieldModel Key = ").append(getKey()).append("\n");
		for (int i = 0; i <= getVals().length - 1; i++) {
			sb.append(i).append(" : ").append(vals[i]).append("\n");
		}
		return sb.toString();
	}

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

	public final String getUniqueControlName(final String controlName,
			final String idIn) {
		String id = idIn;
		if (!stringOK(id)) {
			id = getId();
		}
		return new StringBuilder().append(getType()).append(SEP)
				.append(controlName).append(SEP).append(id).toString();
	}

	public final String getId() {
		String id = getKey();
		if (!stringOK(id)) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

}
