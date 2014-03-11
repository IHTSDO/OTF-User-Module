package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldSetting extends OtfCustomFieldKeyVal {

	public static String DEFPW = "defaultpw";
	public static String USERS = "users";
	public static String MEMBERS = "members";

	public OtfCustomFieldSetting(String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.SETTING;
	}
}
