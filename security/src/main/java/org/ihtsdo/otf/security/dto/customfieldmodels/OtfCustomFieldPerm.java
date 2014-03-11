package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldPerm extends OtfCustomFieldKeyVal {

	public OtfCustomFieldPerm(String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.PERM;
	}

}
