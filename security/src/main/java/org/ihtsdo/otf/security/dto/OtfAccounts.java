package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfAccounts {

	private final HashMap<String, OtfAccount> accounts = new HashMap<String, OtfAccount>();

	public OtfAccounts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, OtfAccount> getAccounts() {
		return accounts;
	}

}
