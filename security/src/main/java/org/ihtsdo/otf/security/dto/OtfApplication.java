package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtfApplication extends OtfBaseNameDesc {

	public OtfApplication() {
		super();

	}

	private final Map<String, OtfAccountStore> accountStores = new HashMap<String, OtfAccountStore>();

	public final Map<String, OtfAccountStore> getAccountStores() {
		return accountStores;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		List<String> keys = new ArrayList<String>(accountStores.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(accountStores.get(key).toString());
		}
		return sbuild.toString();

	}

}
