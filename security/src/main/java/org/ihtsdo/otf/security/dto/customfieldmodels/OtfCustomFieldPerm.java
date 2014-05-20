package org.ihtsdo.otf.security.dto.customfieldmodels;

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
		return "Add New Permission";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		retval.put("Name:",
				getObw().getHtmlInputText("PermissionName", getKey()));
		retval.put("Value:",
				getObw().getHtmlInputText("PermissionValue", getVal()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.PERM;
		return type;
	}
}
