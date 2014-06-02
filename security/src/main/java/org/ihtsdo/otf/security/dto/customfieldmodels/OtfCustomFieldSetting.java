package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldSetting extends OtfCustomFieldKeyVal {

	public static final String DEFPW = "defaultpw";
	public static final String USERS = "users";
	public static final String MEMBERS = "members";

	public OtfCustomFieldSetting() {
		super();
	}

	public OtfCustomFieldSetting(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.CD_TYPE_SETTING;
		return type;
	}

}
