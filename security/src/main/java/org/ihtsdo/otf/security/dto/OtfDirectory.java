package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfDirectory extends OtfBaseAccountStore {

	HashMap<String, OtfAccount> AllAccounts;

	public OtfDirectory() {
		super();
	}

	private OtfGroups groups = new OtfGroups();

	public OtfGroups getGroups() {
		return groups;
	}

	public void setGroups(OtfGroups groupsIn) {
		groups = groupsIn;
	}

	public HashMap<String, OtfAccount> getDirAccounts() {
		return getAccounts().getAccounts();
	}

	public OtfAccount getAccountByName(String name) {
		return getAllAccounts().get(name);
	}

	public HashMap<String, OtfAccount> getAllAccounts() {
		if (AllAccounts == null) {
			AllAccounts = new HashMap<String, OtfAccount>();
		}
		if (getGroups().getGroups().size() == 0) {
			AllAccounts = getDirAccounts();
		}
		if (getGroups().getGroups().size() > 0) {
			for (String key : getDirAccounts().keySet()) {
				AllAccounts.put(key, getDirAccounts().get(key));
			}
			// Add Any Accounts in child groups.
			for (OtfGroup grp : getGroups().getGroups().values()) {

				for (String key : grp.getAccounts().getAccounts().keySet()) {
					AllAccounts.put(key,
							grp.getAccounts().getAccounts().get(key));
				}
			}
		}
		return AllAccounts;
	}

	public void setAllAccounts(HashMap<String, OtfAccount> allAccountsIn) {
		AllAccounts = allAccountsIn;
	}

}
