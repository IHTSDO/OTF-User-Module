package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
import java.util.Map;

public class OtfApplication extends OtfBaseNameDesc {

	public OtfApplication() {
		super();

	}

	private final Map<String, OtfAccountStore> accountStores = new HashMap<String, OtfAccountStore>();

	public final Map<String, OtfAccountStore> getAccountStores() {
		return accountStores;
	}

}
