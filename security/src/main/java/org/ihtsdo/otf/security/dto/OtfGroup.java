package org.ihtsdo.otf.security.dto;

public class OtfGroup extends OtfBaseAccountStore {

	private OtfCustomData custData = new OtfCustomData();

	public OtfGroup() {
		super();
	}

	public final OtfCustomData getCustData() {
		return custData;
	}

	public final void setCustData(final OtfCustomData custDataIn) {
		custData = custDataIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Group:\n");
		sbuild.append(super.toString());
		sbuild.append(custData.toString());
		return sbuild.toString();

	}

}
