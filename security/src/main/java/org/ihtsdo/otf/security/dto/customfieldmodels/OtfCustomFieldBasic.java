package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.HashMap;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldBasic extends OtfCustomFieldModel {

	@Override
	public void model2Vals() {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelFromVals() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Basic Fields:";
	}

	@Override
	public String getNewTitle() {
		// TODO Auto-generated method stub
		return "New Basic Field";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = new HashMap<String, String>();
		retval.put("ID:", getHtmlTextInput("ID", getKey()));
		retval.put("Value:", getHtmlTextInput("Value", getValFromVals()));
		return retval;
	}

	public String getValFromVals() {
		StringBuilder sb = new StringBuilder();
		int vl = vals.length - 1;
		for (int i = 0; i <= vl; i++) {
			sb.append(vals[i]);
			if (i < vl) {
				sb.append(OtfCustomField.SEP);
			}
		}
		return sb.toString();
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.DEFAULT;
		return type;
	}

}
