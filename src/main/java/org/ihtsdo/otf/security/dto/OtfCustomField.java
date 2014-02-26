package org.ihtsdo.otf.security.dto;

import java.util.UUID;
import java.util.logging.Logger;

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
	public static String SEP = ":";
	private CustomType type = CustomType.DEFAULT;
	private String[] vals;

	private OtfCustomFieldMember member;
	private OtfCustomFieldPerm perm;
	private OtfCustomFieldApplication app;

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
		// analyseKey();
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
		} else {
			setType(CustomType.DEFAULT);
		}

	}

	private void analyseValue() {

		// Split val
		vals = getValue().split(SEP);
		if (vals.length > 0) {
			setType();
		}
		switch (type) {
		case APP:
			setApp(new OtfCustomFieldApplication(vals));
		case MEMBER:
			setMember(new OtfCustomFieldMember(vals));
			break;
		case PERM:
			setPerm(new OtfCustomFieldPerm(vals));
			break;
		default:
			// do nothing
			break;
		}
	}

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
		APP
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
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CustomType getType() {
		return type;
	}

	public void setType(CustomType type) {
		// LOG.info("CUSTOM - Setting type to : " + type);
		this.type = type;
	}

	public String[] getVals() {
		return vals;
	}

	public void setVals(String[] valsIn) {
		vals = valsIn;
	}

	public OtfCustomFieldPerm getPerm() {
		return perm;
	}

	public void setPerm(OtfCustomFieldPerm permIn) {
		perm = permIn;
	}

	public OtfCustomFieldApplication getApp() {
		return app;
	}

	public void setApp(OtfCustomFieldApplication appIn) {
		app = appIn;
	}

	public OtfCustomFieldMember getMember() {
		return member;
	}

	public void setMember(OtfCustomFieldMember memberIn) {
		member = memberIn;
	}

}
