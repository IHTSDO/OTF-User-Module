package org.ihtsdo.otf.security.dto;

import java.util.UUID;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldMember;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldModel;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

public class OtfCustomField {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfCustomField.class
			.getName());

	private String key;
	private String value;
	public static final String SEP = ":";
	private CustomType type = CustomType.DEFAULT;
	private String[] vals;

	private OtfCustomFieldModel model;

	/**
	 * Used to indicate the sort of custom field Start of the key string
	 */
	public static enum CustomType {
		/** NO VALUE. */
		DEFAULT,
		/** A Member */
		MEMBER,
		/** A permission. */
		PERM,
		/** An Application. */
		APP,
		/** An Setting. */
		SETTING,
		/** An Setting. */
		KEY_VAL
	}

	public OtfCustomField() {
		super();
	}

	public OtfCustomField(String value) {
		this.value = value;
		init();
	}

	public OtfCustomField(String key, String value) {
		super();
		this.key = key;
		this.value = value;
		init();
	}

	public final void init() {
		analyseValue();
	}

	private void setType() {
		String tval = vals[0];
		if (tval.equalsIgnoreCase(CustomType.MEMBER.name())) {
			setType(CustomType.MEMBER);
			return;
		}
		if (tval.equalsIgnoreCase(CustomType.PERM.name())) {
			setType(CustomType.PERM);
			return;
		}
		if (tval.equalsIgnoreCase(CustomType.APP.name())) {
			setType(CustomType.APP);
			return;

		}
		if (tval.equalsIgnoreCase(CustomType.SETTING.name())) {
			setType(CustomType.SETTING);
			return;
		} else {
			setType(CustomType.DEFAULT);
		}

	}

	public void analyseValue() {
		// LOG.info("analyseValue key = " + getKey());
		// Split val
		vals = getValue().split(SEP);
		if (vals.length > 0) {
			setType();
		}
		switch (type) {
		case APP:
			setModel(new OtfCustomFieldApplication(getKey(), vals));
			break;
		case MEMBER:
			setModel(new OtfCustomFieldMember(getKey(), vals));
			break;
		case PERM:
			setModel(new OtfCustomFieldPerm(getKey(), vals));
			break;
		case SETTING:
			setModel(new OtfCustomFieldSetting(getKey(), vals));
			break;
		default:
			// do nothing
			break;
		}
	}

	public String getKey() {
		if (key == null || key.length() == 0) {
			key = UUID.randomUUID().toString();
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		if (value == null || value.length() == 0) {
			value = getValFromVals();
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValFromVals() {
		StringBuilder sb = new StringBuilder();
		int vl = vals.length - 1;
		for (int i = 0; i <= vl; i++) {
			sb.append(vals[i]);
			if (i < vl) {
				sb.append(SEP);
			}
		}
		return sb.toString();
	}

	public CustomType getType() {
		return type;
	}

	public void setType(CustomType type) {
		this.type = type;
	}

	public String[] getVals() {
		return vals;
	}

	public void setVals(String[] valsIn) {
		vals = valsIn;
	}

	public OtfCustomFieldModel getModel() {
		return model;
	}

	public void setModel(OtfCustomFieldModel modelIn) {
		model = modelIn;
		// LOG.info("\n Model Type = " + model.getType() + " key = "
		// + model.getKey() + " id = " + model.getId());
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("CustomField: ").append("key:").append(key)
				.append(", value:").append(value);
		return sbuild.toString();

	}
}
