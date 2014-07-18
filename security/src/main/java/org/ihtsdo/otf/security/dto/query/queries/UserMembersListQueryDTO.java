package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserMembersListQueryDTO extends AbstractSecurityQuery {

	private List<String> members;
	private String userName;

	public UserMembersListQueryDTO() {
		super();
	}

	public UserMembersListQueryDTO(final UserSecurityHandler userSecurityIn,
			final String userNameIn) {
		super(userSecurityIn);
		userName = userNameIn;
	}

	public final List<String> getMembers() {
		if (members == null) {
			members = new ArrayList<String>();
		}

		if (members.isEmpty()) {
			if (ush != null) {
				OtfAccount oacc = ush.getUserSecurity().getUserAccountByName(
						userName, "*");
				if (oacc != null) {
					List<OtfCustomField> mems = oacc.getCustData().getMembers();
					for (OtfCustomField cf : mems) {
						members.add(cf.getVals()[1]);
					}
				}
			}
		}
		return members;
	}

	public final void setMembers(final List<String> membersIn) {
		members = membersIn;
	}

}
