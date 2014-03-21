package org.ihtsdo.otf.security.dto.query.queries;

public class AppPermDTO {

	public AppPermDTO() {
		super();

	}

	public AppPermDTO(final String appIn, final String roleIn,
			final String memberIn) {
		super();
		app = appIn;
		role = roleIn;
		member = memberIn;
	}

	private String app;
	private String role;
	private String member;

	public final String getApp() {
		return app;
	}

	public final void setApp(final String appIn) {
		app = appIn;
	}

	public final String getRole() {
		return role;
	}

	public final void setRole(final String roleIn) {
		role = roleIn;
	}

	public final String getMember() {
		return member;
	}

	public final void setMember(final String memberIn) {
		member = memberIn;
	}

}
