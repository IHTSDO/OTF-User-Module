package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.HashMap;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldPerm extends OtfCustomFieldKeyVal {

	public OtfCustomFieldPerm() {
		super();
	}

	public OtfCustomFieldPerm(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
	}

	@Override
	public final String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Permissions";
	}

	@Override
	public final String getNewTitle() {
		// TODO Auto-generated method stub
		return "Add New Permissions";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = new HashMap<String, String>();
		retval.put("Name:", getHtmlTextInput("PermissionName", getKey()));
		retval.put("Value:", getHtmlTextInput("PermissionValue", getVal()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.PERM;
		return type;
	}
}
