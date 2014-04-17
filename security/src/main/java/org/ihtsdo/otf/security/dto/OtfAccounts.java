package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OtfAccounts {

	private final HashMap<String, OtfAccount> accounts = new HashMap<String, OtfAccount>();

	public OtfAccounts() {
		super();
	}

	public final HashMap<String, OtfAccount> getAccounts() {
		return accounts;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Accounts:\n");
		List<String> keys = new ArrayList<String>(accounts.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(accounts.get(key).toString()).append("\n");
		}
		return sbuild.toString();
	}

}
