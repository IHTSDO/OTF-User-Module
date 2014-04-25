package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserAppPermsListQueryDTO extends AbstractSecurityQuery {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	// private static final Logger LOG = Logger
	// .getLogger(UserAppPermsListQueryDTO.class.getName());

	private List<AppPermDTO> perms;
	private String userName;
	private String appName;
	private String membership;

	public UserAppPermsListQueryDTO() {
		super();
	}

	public UserAppPermsListQueryDTO(final UserSecurityHandler ushIn,
			final String userNameIn, final String appNameIn) {
		super(ushIn);
		userName = userNameIn;
		appName = appNameIn;
	}

	public UserAppPermsListQueryDTO(final UserSecurityHandler ushIn,
			final String userNameIn, final String appNameIn,
			final String membershipIn) {
		super(ushIn);
		userName = userNameIn;
		appName = appNameIn;
		membership = membershipIn;
	}

	public final List<AppPermDTO> getPerms() {
		if (perms == null) {
			perms = new ArrayList<AppPermDTO>();
		}

		if (perms.size() == 0) {
			if (ush != null) {
				OtfAccount oacc = ush.getUserSecurity().getUserAccountByName(
						userName, "*");
				if (oacc != null) {
					List<OtfCustomField> operms = oacc.getCustData().getApps();
					for (OtfCustomField cf : operms) {
						OtfCustomFieldApplication cfapp = (OtfCustomFieldApplication) cf
								.getModel();
						if (cfapp.getApp().equals(appName)) {
							if (membership == null || membership.length() == 0) {
								perms.add(getAp(cfapp));
							} else {
								if (membership.equals(cfapp.getMember())) {
									perms.add(getAp(cfapp));
								}
							}

						}

					}
				}
			}
		}
		return perms;
	}

	private AppPermDTO getAp(final OtfCustomFieldApplication cfapp) {

		AppPermDTO ap = new AppPermDTO(cfapp.getApp(), cfapp.getRole(),
				cfapp.getMember());

		return ap;
	}

	public final void setPerms(final List<AppPermDTO> permsIn) {
		perms = permsIn;
	}

}
