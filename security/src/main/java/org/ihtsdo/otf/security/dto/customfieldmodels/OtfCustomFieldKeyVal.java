package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldKeyVal extends OtfCustomFieldCachedVals {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfCustomFieldPerm.class
			.getName());

	private String keyVal;
	private String val;

	public static final String KEY_NAME = "KeyValKey";
	public static final String KEY_VAL = "KeyValVal";

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
		vals[1] = getKeyVal();
		vals[2] = getVal();

	}

	@Override
	public final void modelFromVals() {
		setKeyVal(vals[1]);
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

	public final void updateVal(final String valIn) {
		setVal(valIn);
		model2Vals();

	}

	@Override
	public String getCollectionTitle() {
		
		return "Key Values";
	}

	@Override
	public String getNewTitle() {
		
		return "New Key Value";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		retval.put("Key:", getObw().getHtmlInputText(KEY_NAME, getKeyVal()));
		retval.put("Value:", getObw().getHtmlInputText(KEY_VAL, getVal()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.CD_TYPE_KEY_VAL;
		return type;
	}

	@Override
	public void valFromParamDTO(OtfCustomFieldParamDTO ocfpIn) {
		switch (ocfpIn.getLocalKey()) {
		case KEY_NAME:
			setKeyVal(ocfpIn.getVal());
			break;
		case KEY_VAL:
			setVal(ocfpIn.getVal());
			break;
		default:
			LOG.info("valFromParamDTO localKey not found and = "
					+ ocfpIn.getLocalKey());
		}
	}

}
