package org.ihtsdo.otf.security.dto;

import java.util.Map;

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

	@Override
	public String toString() {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Account:\n").append(super.toString()).append("\n");
		sbuild.append(custData.toString());
		return sbuild.toString();

	}

	@Override
	public void processParams(Map<String, String> paramsIn) {
		// TODO Auto-generated method stub

	}

}
