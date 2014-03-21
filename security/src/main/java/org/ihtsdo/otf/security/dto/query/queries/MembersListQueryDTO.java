package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class MembersListQueryDTO extends AbstractSecurityQuery {

	private List<String> members;

	public MembersListQueryDTO() {
		super();
	}

	public MembersListQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public final List<String> getMembers() {

		if (members == null || members.size() == 0) {
			if (ush != null) {
				members = ush.getUserSecurity().getMembers();
			} else {
				members = new ArrayList<String>();
			}
		}
		return members;
	}

	public final void setMembers(final List<String> membersIn) {
		members = membersIn;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Members Num found = ").append(getMembers().size() + "\n");
		for (String member : getMembers()) {
			sb.append(member).append("\n");
		}
		return sb.toString();
	}

}
