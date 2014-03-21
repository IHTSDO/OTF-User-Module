package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtfCustomData {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	// private static final Logger LOG = Logger.getLogger(OtfCustomData.class
	// .getName());

	public static final List<String> RESERVED_WORDS = new ArrayList<String>();
	private final HashMap<String, OtfCustomField> custFields = new HashMap<String, OtfCustomField>();

	public final HashMap<String, OtfCustomField> getCustFields() {
		return custFields;
	}

	public static List<String> getReservedWords() {
		if (RESERVED_WORDS.size() == 0) {
			RESERVED_WORDS.add("href");
			RESERVED_WORDS.add("createdAt");
			RESERVED_WORDS.add("modifiedAt");
			RESERVED_WORDS.add("meta");
			RESERVED_WORDS.add("spMeta");
			RESERVED_WORDS.add("spmeta");
			RESERVED_WORDS.add("ionmeta");
			RESERVED_WORDS.add("ionMeta");
		}
		return RESERVED_WORDS;
	}

	public final List<OtfCustomField> getFieldsByType(
			final OtfCustomField.CustomType type) {
		List<OtfCustomField> fields = new ArrayList<OtfCustomField>();
		for (OtfCustomField cf : custFields.values()) {
			if (cf.getType().equals(type)) {
				fields.add(cf);
			}
		}
		return fields;
	}

	public final List<OtfCustomField> getDefaults() {
		return getFieldsByType(OtfCustomField.CustomType.DEFAULT);
	}

	public final List<OtfCustomField> getMembers() {
		return getFieldsByType(OtfCustomField.CustomType.MEMBER);
	}

	public final List<OtfCustomField> getPerms() {
		return getFieldsByType(OtfCustomField.CustomType.PERM);
	}

	public final List<OtfCustomField> getApps() {
		return getFieldsByType(OtfCustomField.CustomType.APP);
	}

	public final List<OtfCustomField> getSettings() {
		return getFieldsByType(OtfCustomField.CustomType.SETTING);
	}

	public final List<OtfCustomField> getAppsByAppName(final String appName) {

		List<OtfCustomField> allApps = getApps();

		List<OtfCustomField> allAppsByName = new ArrayList<OtfCustomField>();

		for (OtfCustomField cf : allApps) {
			if (cf.getModel().getVals()[1].equals(appName)) {
				allAppsByName.add(cf);
			}
		}

		return allAppsByName;
	}

	public final boolean isaMemberOf(final String member) {

		for (OtfCustomField cf : getMembers()) {
			if (cf.getVals()[1].equalsIgnoreCase(member)) {
				return true;
			}
		}

		return false;
	}
}
