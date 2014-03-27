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

}
