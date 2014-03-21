package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldSetting extends OtfCustomFieldKeyVal {

	public static final String DEFPW = "defaultpw";
	public static final String USERS = "users";
	public static final String MEMBERS = "members";

	public OtfCustomFieldSetting(final String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.SETTING;
	}
}
