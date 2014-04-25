package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UsersListQueryDTO extends AbstractSecurityQuery {

	private List<OftAccountMin> users;

	public UsersListQueryDTO() {
		super();
	}

	public UsersListQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public final List<OftAccountMin> getUsers() {
		if (users == null || users.size() == 0) {
			if (ush != null) {
				users = (List<OftAccountMin>) ush.getUserSecurity()
						.getMinUsers("*");
			} else {
				users = new ArrayList<OftAccountMin>();
			}
		}
		return users;
	}

	public final void setUsers(final List<OftAccountMin> usersIn) {
		users = usersIn;
	}

}
