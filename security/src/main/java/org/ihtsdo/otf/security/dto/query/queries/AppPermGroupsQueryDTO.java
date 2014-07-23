package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccountStore;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class AppPermGroupsQueryDTO extends AbstractSecurityQuery {

	private String appName;
	private String groupName;
	private List<GroupPermDTO> perms = new ArrayList<GroupPermDTO>();

	public AppPermGroupsQueryDTO() {
		super();
	}

	public AppPermGroupsQueryDTO(final UserSecurityHandler ushIn,
			final String appNameIn) {
		super(ushIn);
		appName = appNameIn;
	}

	public AppPermGroupsQueryDTO(final UserSecurityHandler ushIn,
			final String appNameIn, final String groupNameIn) {
		super(ushIn);
		groupName = groupNameIn;
		appName = appNameIn;
	}

	public final List<GroupPermDTO> getPerms() {
		if (perms == null) {
			perms = new ArrayList<GroupPermDTO>();
		}

		if (!perms.isEmpty()) {
			if (ush != null) {
				boolean checkGrpName = groupName != null
						&& groupName.length() > 0;
				for (OtfApplication oApp : ush.getUserSecurity().getApps()
						.getApplications().values()) {

					if (oApp.getName().equals(appName)) {
						for (OtfAccountStore oAst : oApp.getAccountStores()
								.values()) {
							String name = oAst.getName();

							OtfDirectory dir = ush.getUserSecurity().getDirs()
									.getDirByName(name);
							if (dir != null) {
								getGroupsForDir(dir, checkGrpName);
							}
						}
					}
				}
			}
		}
		return perms;
	}

	public final void setPerms(final List<GroupPermDTO> permsIn) {
		perms = permsIn;
	}

	private void getGroupsForDir(final OtfDirectory dir, final boolean checkName) {
		for (OtfGroup grp : dir.getGroups().getGroups().values()) {
			boolean add = true;
			if (checkName) {
				add = grp.getName().equals(groupName);
			}
			if (add) {
				GroupPermDTO gpd = getgpd(grp);
				if (gpd != null) {
					perms.add(gpd);
				}
			}
		}

	}

	private GroupPermDTO getgpd(final OtfGroup grp) {
		List<OtfCustomField> permsL = grp.getCustData().getPerms();
		if (!permsL.isEmpty()) {
			GroupPermDTO gpd = new GroupPermDTO(grp.getName());
			for (OtfCustomField cf : permsL) {
				OtfCustomFieldPerm cfapp = (OtfCustomFieldPerm) cf.getModel();
				PermDTO pd = new PermDTO(cfapp.getKeyVal(), cfapp.getVal());
				gpd.getPerms().add(pd);
			}
			return gpd;
		}
		return null;
	}

}
