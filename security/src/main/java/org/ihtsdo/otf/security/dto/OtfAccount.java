package org.ihtsdo.otf.security.dto;

public class OtfAccount extends OftAccountMin {
	public OtfAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	private OtfCustomData custData = new OtfCustomData();

	public final OtfCustomData getCustData() {
		return custData;
	}

	public final void setCustData(final OtfCustomData custDataIn) {
		custData = custDataIn;
	}
}
