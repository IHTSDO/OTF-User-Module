package org.ihtsdo.otf.security.xml;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.xml.base.XMLUtil;
import org.w3c.dom.Document;

public class XmlUserSecurity extends AbstractUserSecurityHandler {

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
	private static final Logger LOG = Logger.getLogger(XmlUserSecurity.class
			.getName());

	// public XmlUserSecurity(String configFNIn) {
	// super();
	// configFN = configFNIn;
	//
	// }

	public XmlUserSecurity(final Properties propsIn) {
		super();
		try {
			init(propsIn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public XmlUserSecurity() {
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
	}

	public final void initFromFile() throws Exception {
		init(configFN);
	}

	public final void initFromDoc(final Document confDoc) throws Exception {
		setUserSecurity(xml2Mod.build(confDoc));
	}

	public final void initFromXMLString(final String confXML) throws Exception {
		final Document confDoc = XMLUtil.getDocumentFromXMLString(confXML,
				false);
		setUserSecurity(xml2Mod.build(confDoc));
	}

	public final void init(final String configFnIn) throws Exception {
		final File confile = new File(configFnIn);
		if (confile.exists() && !confile.isDirectory() && confile.canRead()) {
			final Document confDoc = XMLUtil.getDocument(configFnIn);
			setUserSecurity(xml2Mod.build(confDoc));
		} else {
			LOG.severe("Something is wrong with the file you have specified file = "
					+ configFnIn);
			throw new Exception(
					"Config File specified but Not found config file = "
							+ configFnIn);
		}

	}

	public final Document getXMLFromUserSecurity() {
		return mod2Xml.getXML(getUserSecurity());
	}

	public final String getXMLFromUserSecurityAsString() {
		return XMLUtil.writeXMLToString(getXMLFromUserSecurity());
	}

	public final String getXMLFromUserSecurityAsStringSortByName() {

		getSortXsltFn();
		String xml = getXMLFromUserSecurityAsString();
		LOG.info("Orig XML : \n" + xml);
		if (sortXsltFn == null || sortXsltFn.length() == 0) {
			return xml;
		} else {
			return XMLUtil.processXslt(xml, sortXsltFn);
		}

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
	public void saveUserSecurity() throws Exception {
		LOG.info("About to write to " + getConfigFN());
		XMLUtil.writeXMLToFile(getXMLFromUserSecurity(), getConfigFN());
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
	public final OtfAccount authAccount(final String acNameIn, final String pwIn) {
		// Really for use in testing - simply check if password is defaultPw and
		// that user account exists.

		if (pwIn.equals(getUserSecurity().getDefaultpw())) {
			return getUserSecurity().getUserAccountByName(acNameIn, "*");
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
			final OtfDirectory parentIn, final boolean isNewIn) {
		// Nothing to do as the entire model is written out.
		return saveUSToXML();
	}

	@Override
	public void localReload() {
		try {
			buildUserSecurity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String addUpdateGroupLocal(OtfGroup grpIn,
			OtfDirectory mDirectoryIn, boolean isNewIn) {
		return saveUSToXML();
	}

}
