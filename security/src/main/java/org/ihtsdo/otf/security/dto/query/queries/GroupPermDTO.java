package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

public class GroupPermDTO {

	public GroupPermDTO(String nameIn) {
		super();
		name = nameIn;
	}

	public GroupPermDTO() {
		super();

	}

	private String name;
	private List<PermDTO> perms = new ArrayList<PermDTO>();;

	public String getName() {
		return name;
	}

	public void setName(String nameIn) {
		name = nameIn;
	}

	public List<PermDTO> getPerms() {
		return perms;
	}

	public void setPerms(List<PermDTO> permsIn) {
		perms = permsIn;
	}

}
