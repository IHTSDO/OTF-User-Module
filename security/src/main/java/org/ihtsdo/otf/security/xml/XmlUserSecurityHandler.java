package org.ihtsdo.otf.security.xml;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.UserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurityModelCached;
import org.ihtsdo.otf.security.xml.base.XMLUtil;
import org.w3c.dom.Document;

public class XmlUserSecurityHandler extends AbstractUserSecurityHandler {

	private String configFN;
	private String sortXsltFn = "";
	public static final String CONF_PROPS_FN = "configFile";

	private final Xml2Model xml2Mod = new Xml2Model();
	private final Model2Xml mod2Xml = new Model2Xml();

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(XmlUserSecurityHandler.class.getName());

	public XmlUserSecurityHandler(final Properties propsIn) {
		super();
		try {
			init(propsIn);
		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	public XmlUserSecurityHandler() {
		super();

	}

	@Override
	public final void init(final Properties propsIn) throws Exception {
		setConfigFN(propsIn.getProperty(CONF_PROPS_FN));
		buildUserSecurity();
	}

	@Override
	public final void buildUserSecurity() throws Exception {
		initFromFile();
		postbuildUserSecurity();
		getUserSecurityModel().init();
	}

	public final void initFromFile() throws Exception {
		init(configFN);
	}

	public final void initFromDoc(final Document confDoc) throws Exception {
		getUserSecurityModel().setModel(xml2Mod.build(confDoc));
	}

	public final void initFromXMLString(final String confXML) throws Exception {
		final Document confDoc = XMLUtil.getDocumentFromXMLString(confXML,
				false);
		getUserSecurityModel().setModel(xml2Mod.build(confDoc));
	}

	public final void init(final String configFnIn) throws Exception {
		final File confile = new File(configFnIn);
		if (confile.exists() && !confile.isDirectory() && confile.canRead()) {
			final Document confDoc = XMLUtil.getDocument(configFnIn);
			getUserSecurityModel().setModel(xml2Mod.build(confDoc));
		} else {
			LOG.severe("Something is wrong with the file you have specified file = "
					+ configFnIn);
			throw new Exception(
					"Config File specified but Not found config file = "
							+ configFnIn);
		}

	}

	public final Document getXMLFromUserSecurity() {
		return mod2Xml.getXML(getUserSecurityModel().getModel());
	}

	public final String getXMLFromUserSecurityAsString() {
		try {
			return XMLUtil.writeXMLToString(getXMLFromUserSecurity(),
					getSortXsltFn());
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Problem getting XML as String", e);
		}
		return "";
	}

	private String saveUSToXML() {
		try {
			saveUserSecurity();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Error Writing XML Model", e);
			return REMOTE_COMMIT_NOT_OK + " " + e.getMessage();
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public final void saveUserSecurity() throws Exception {
		LOG.info("About to write to " + getConfigFN());
		XMLUtil.writeXMLToFile(getXMLFromUserSecurity(), getConfigFN(),
				getSortXsltFn());
	}

	public final String getConfigFN() {
		return configFN;
	}

	public final void setConfigFN(final String configFNIn) {
		configFN = configFNIn;
	}

	public final String getSortXsltFn() {
		if (sortXsltFn == null || sortXsltFn.length() == 0) {
			URL fn = getClass().getResource("/xslt/SortName.xslt");
			sortXsltFn = fn.toString();
		}
		return sortXsltFn;
	}

	public final void setSortXsltFn(final String sortXsltFnIn) {
		sortXsltFn = sortXsltFnIn;
	}

	@Override
	public final OtfAccount authAccountLocal(final String acNameIn,
			final String pwIn, final String tokenIn) {
		// check acc exists
		OtfAccount acc = getUserSecurityModel().getUserAccountByName(acNameIn);
		if (acc == null) {
			return null;
		}
		// check if uuid/token
		if (stringOK(tokenIn)) {
			if (acc.checkAuthToken(tokenIn)) {
				return acc;
			}
		}

		// Really for use in testing - simply check if password is defaultPw and
		// that user account exists.
		if (stringOK(pwIn)) {
			if (pwIn.equals(getUserSecurityModel().getSettings().getDefPw())) {
				return acc;
			}
		}
		return null;
	}

	@Override
	public final String addUpdateMemberLocal(final OtfGroup grpIn,
			final OtfDirectory mDirectoryIn, final boolean isNewIn) {
		// Nothing to do as the entire model is written out.
		return saveUSToXML();
	}

	@Override
	public final String addUpdateAppLocal(final OtfApplication appIn,
			final boolean isNewIn) {
		// Nothing to do as the entire model is written out.
		return saveUSToXML();
	}

	@Override
	public final String addUpdateAccountLocal(final OtfAccount accIn,
			final String parentDirNameIn, final boolean isNewIn) {
		if (isNewIn && parentDirNameIn != null) {
			OtfDirectory parentIn = getUserSecurityModel().getDirByName(
					parentDirNameIn);
			parentIn.getAccounts().getAccounts().put(accIn.getName(), accIn);
		}

		return saveUSToXML();
	}

	@Override
	public void localReload() {
	}

	@Override
	public final String addUpdateGroupLocal(final OtfGroup grpIn,
			final OtfDirectory mDirectoryIn, final boolean isNewIn) {
		return saveUSToXML();
	}

	@Override
	public final String addUpdateDirLocal(final OtfDirectory parentIn,
			final boolean isNewIn) {
		return saveUSToXML();
	}

	@Override
	public final String requestUpdateUserPassword(final String userNameIn,
			final String emailAddrIn) {

		return "XML Implementation. requestUpdateUserPassword :- Currently no "
				+ "user password excepting the default one";
	}

	@Override
	public final int updateUserPassword(final String usernameIn,
			final String passwordIn, final String token) {

		if (stringOK(passwordIn)) {
			return 1;
		}
		if (stringOK(usernameIn)) {
			return 0;
		}
		return -1;
	}

	@Override
	public UserSecurityModel getLocalUserSecurityModel() {
		return new UserSecurityModelCached();
	}

	@Override
	public String addExistDirToAppLocal(final OtfDirectory dir,
			final OtfApplication app, boolean defAcStIn, boolean defGrpStIn,
			int orderIn) {

		// Nothing to do

		return "";
	}

}
