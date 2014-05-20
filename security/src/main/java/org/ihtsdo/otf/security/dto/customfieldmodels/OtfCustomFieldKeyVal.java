package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldKeyVal extends OtfCustomFieldModel {

	private String keyVal;
	private String val;

	public OtfCustomFieldKeyVal(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
	}

	public OtfCustomFieldKeyVal() {
		super();
	}

	@Override
	public final void model2Vals() {
		vals = new String[3];
		vals[0] = getType().name();
		vals[1] = getKey();
		vals[2] = getVal();

	}

	@Override
	public final void modelFromVals() {
		setKey(vals[1]);
		setVal(vals[2]);
	}

	public final String getKeyVal() {
		if (keyVal == null) {
			keyVal = "";
		}
		return keyVal;
	}

	public final void setKeyVal(final String keyIn) {
		keyVal = keyIn;
	}

	public final String getVal() {
		if (val == null) {
			val = "";
		}
		return val;
	}

	public final void setVal(final String valIn) {
		val = valIn;
	}

	@Override
	public String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Key Values";
	}

	@Override
	public String getNewTitle() {
		// TODO Auto-generated method stub
		return "New Key Value";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		retval.put("Key:", getObw().getHtmlInputText("KeyValKey", getKeyVal()));
		retval.put("Value:", getObw().getHtmlInputText("KeyValVal", getVal()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.KEY_VAL;
		return type;
	}

}
