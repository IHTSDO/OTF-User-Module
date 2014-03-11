package org.ihtsdo.otf.security.dto.query.queries;

public class AppPermDTO {

	public AppPermDTO() {
		super();

	}

	public AppPermDTO(String appIn, String roleIn, String memberIn) {
		super();
		app = appIn;
		role = roleIn;
		member = memberIn;
	}

	private String app;
	private String role;
	private String member;

	public String getApp() {
		return app;
	}

	public void setApp(String appIn) {
		app = appIn;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String roleIn) {
		role = roleIn;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String memberIn) {
		member = memberIn;
	}

}
