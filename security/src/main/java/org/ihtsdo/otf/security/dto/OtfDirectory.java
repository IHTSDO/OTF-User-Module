package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
import java.util.Map;

public class OtfDirectory extends OtfBaseAccountStore {

	private Map<String, OtfAccount> allAccounts;

	public OtfDirectory() {
		super();
	}

	private OtfGroups groups = new OtfGroups();

	public final OtfGroups getGroups() {
		return groups;
	}

	public final void setGroups(final OtfGroups groupsIn) {
		groups = groupsIn;
	}

	public final Map<String, OtfAccount> getDirAccounts() {
		return getAccounts().getAccounts();
	}

	public final OtfAccount getAccountByName(final String name) {
		return getAllAccounts().get(name);
	}

	public final Map<String, OtfAccount> getAllAccounts() {
		if (allAccounts == null) {
			allAccounts = new HashMap<String, OtfAccount>();
		}
		if (getGroups().getGroups().size() == 0) {
			allAccounts = getDirAccounts();
		}
		if (getGroups().getGroups().size() > 0) {
			for (String key : getDirAccounts().keySet()) {
				allAccounts.put(key, getDirAccounts().get(key));
			}
			// Add Any Accounts in child groups.
			for (OtfGroup grp : getGroups().getGroups().values()) {

				for (String key : grp.getAccounts().getAccounts().keySet()) {
					allAccounts.put(key,
							grp.getAccounts().getAccounts().get(key));
				}
			}
		}
		return allAccounts;
	}

	public final void setAllAccounts(final Map<String, OtfAccount> allAccountsIn) {
		allAccounts = allAccountsIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Directory:\n");
		sbuild.append(super.toString());
		sbuild.append(groups.toString());
		return sbuild.toString();

	}
}
