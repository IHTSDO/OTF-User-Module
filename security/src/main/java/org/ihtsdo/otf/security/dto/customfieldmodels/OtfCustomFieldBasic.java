package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldBasic extends OtfCustomFieldCachedVals {

	@Override
	public void model2Vals() {
	}

	@Override
	public void modelFromVals() {
	}

	@Override
	public String getCollectionTitle() {
		return "Basic Fields:";
	}

	@Override
	public String getNewTitle() {
		return "New Basic Field";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		retval.put("ID:", getObw().getHtmlInputText("ID", getKey()));
		retval.put("Value:",
				getObw().getHtmlInputText("Value", getValFromVals()));
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
		type = OtfCustomField.CustomType.CD_TYPE;
		return type;
	}

	@Override
	public void valFromParamDTO(OtfCustomFieldParamDTO ocfpIn) {

	}

}
