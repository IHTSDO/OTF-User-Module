package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldMember extends OtfCustomFieldModel {

	private String val;
	private List<String> members;

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

	public final List<String> getMembers() {
		if (members == null) {
			members = new ArrayList<String>();
		}
		return members;
	}

	public final void setMembers(List<String> membersIn) {
		members = membersIn;
	}

	@Override
	public String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Memberships";
	}

	@Override
	public String getNewTitle() {
		// TODO Auto-generated method stub
		return "Add new Membership";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = new HashMap<String, String>();
		retval.put("Member:", getHtmlTextInput("Member", getVal()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.MEMBER;
		return type;
	}

}
