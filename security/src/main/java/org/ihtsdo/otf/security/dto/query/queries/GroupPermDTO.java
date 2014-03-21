package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

public class GroupPermDTO {

	public GroupPermDTO(final String nameIn) {
		super();
		name = nameIn;
	}

	public GroupPermDTO() {
		super();

	}

	private String name;
	private List<PermDTO> perms = new ArrayList<PermDTO>();;

	public final String getName() {
		return name;
	}

	public final void setName(final String nameIn) {
		name = nameIn;
	}

	public final List<PermDTO> getPerms() {
		return perms;
	}

	public final void setPerms(final List<PermDTO> permsIn) {
		perms = permsIn;
	}

}
