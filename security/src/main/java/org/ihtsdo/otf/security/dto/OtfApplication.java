package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfApplication extends OtfBaseNameDesc {

	public OtfApplication() {
		super();

	}

	private final HashMap<String, OtfAccountStore> accountStores = new HashMap<String, OtfAccountStore>();

	public HashMap<String, OtfAccountStore> getAccountStores() {
		return accountStores;
	}

}
