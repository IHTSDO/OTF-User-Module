package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldMember extends OtfCustomFieldCachedVals {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfCustomFieldApplication.class.getName());

	private String val;

	public OtfCustomFieldMember() {
		super();
	}

	public OtfCustomFieldMember(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
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
	public final void model2Vals() {
		vals = new String[2];
		vals[0] = getType().name();
		vals[1] = getVal();
	}

	@Override
	public final void modelFromVals() {
		setVal(vals[1]);
	}

	@Override
	public String getCollectionTitle() {
		
		return "Memberships";
	}

	@Override
	public String getNewTitle() {
		
		return "Add new Membership";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		String id = getId();
		String controlId = getUniqueControlName("Member", id);
		retval.put("Member:", getMemberOptionsSelect(getVal(), controlId));
		return retval;
	}

	@Override
	public void valFromParamDTO(OtfCustomFieldParamDTO ocfpIn) {
		setVal(ocfpIn.getVal());
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.CD_TYPE_MEMBER;
		return type;
	}

}
