package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UsersListQueryDTO extends AbstractSecurityQuery {

	private List<OtfAccountMin> users;

	public UsersListQueryDTO() {
		super();
	}

	public UsersListQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public final List<OtfAccountMin> getUsers() {
		if (users == null) {
			users = new ArrayList<OtfAccountMin>();
		}
		// if (users == null || users.isEmpty()) {
		// if (ush != null) {

		users = (List<OtfAccountMin>) ush.getUserSecurityModel().getUsersMin(
				"*");
		// } else {

		// }
		// }
		Collections.sort(users);
		return users;
	}

	public final void setUsers(final List<OtfAccountMin> usersIn) {
		users = usersIn;
	}

}
