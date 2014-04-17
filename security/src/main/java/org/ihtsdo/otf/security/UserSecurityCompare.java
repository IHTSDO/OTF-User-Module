package org.ihtsdo.otf.security;

import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.UserSecurity;

public class UserSecurityCompare {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(UserSecurity.class
			.getName());

	private UserSecurity leftUS;
	private UserSecurity rightUS;
	private boolean different;

	public UserSecurityCompare(final UserSecurity leftUSIn,
			final UserSecurity rightUSIn) {
		super();
		leftUS = leftUSIn;
		rightUS = rightUSIn;
	}

	public final UserSecurity getLeftUS() {
		return leftUS;
	}

	public final void setLeftUS(final UserSecurity leftUSIn) {
		leftUS = leftUSIn;
	}

	public final UserSecurity getRightUS() {
		return rightUS;
	}

	public final void setRightUS(final UserSecurity rightUSIn) {
		rightUS = rightUSIn;
	}

	public void compare() {

	}

	public final boolean isDifferent() {
		return different;
	}

	public final void setDifferent(boolean differentIn) {
		different = differentIn;
	}

	public static final String remSpaceLineEnds(final String strIn) {
		return strIn.replaceAll("\\s", "");
	}

}
