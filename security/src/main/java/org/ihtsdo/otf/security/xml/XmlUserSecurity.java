package org.ihtsdo.otf.security.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
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
	private final Model2Xml Mod2Xml = new Model2Xml();

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

	public XmlUserSecurity(Properties propsIn) {
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
	public void init(Properties propsIn) throws Exception {
		setConfigFN(propsIn.getProperty(CONF_PROPS_FN));
		buildUserSecurity();
	}

	@Override
	public void buildUserSecurity() throws Exception {
		initFromFile();
	}

	public void initFromFile() throws Exception {
		init(configFN);
	}

	public void initFromDoc(final Document confDoc) throws Exception {
		setUserSecurity(xml2Mod.build(confDoc));
	}

	public void initFromXMLString(final String confXML) throws Exception {
		final Document confDoc = XMLUtil.getDocumentFromXMLString(confXML,
				false);
		setUserSecurity(xml2Mod.build(confDoc));
	}

	public final void init(final String configFN) throws Exception {
		final File confile = new File(configFN);
		if (confile.exists() && !confile.isDirectory() && confile.canRead()) {
			final Document confDoc = XMLUtil.getDocument(configFN);
			setUserSecurity(xml2Mod.build(confDoc));
		} else {
			LOG.severe("Something is wrong with the file you have specified file = "
					+ configFN);
			throw new Exception(
					"Config File specified but Not found config file = "
							+ configFN);
		}

	}

	public Document getXMLFromUserSecurity() {
		return Mod2Xml.getXML(getUserSecurity());
	}

	public String getXMLFromUserSecurityAsString() {
		return XMLUtil.writeXMLToString(getXMLFromUserSecurity());
	}

	public String getXMLFromUserSecurityAsStringSortByName() {

		getSortXsltFn();
		String xml = getXMLFromUserSecurityAsString();
		LOG.info("Orig XML : \n" + xml);
		if (sortXsltFn == null || sortXsltFn.length() == 0) {
			return xml;
		} else {
			return XMLUtil.processXslt(xml, sortXsltFn);
		}

	}

	@Override
	public void saveUserSecurity() throws IOException {
		// TODO: Write out to file.
	}

	public String getConfigFN() {
		return configFN;
	}

	public void setConfigFN(String configFNIn) {
		configFN = configFNIn;
	}

	public String getSortXsltFn() {
		if (sortXsltFn == null || sortXsltFn.length() == 0) {
			URL fn = getClass().getResource("/xslt/SortName.xslt");
			sortXsltFn = fn.toString();
		}
		return sortXsltFn;
	}

	public void setSortXsltFn(String sortXsltFnIn) {
		sortXsltFn = sortXsltFnIn;
	}

	@Override
	public OtfAccount authAccount(String acNameIn, String pwIn) {
		// Really for use in testing - simply check if password is defaultPw and
		// that user account exists.

		if (pwIn.equals(getUserSecurity().getDefaultpw())) {
			return getUserSecurity().getUserAccountByName(acNameIn);
		}

		return null;
	}

	@Override
	public boolean addUpdateAccount(OtfAccount accIn, OtfDirectory parentIn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUpdateMember(OtfGroup grpIn) {
		// get members parent dir
		OtfDirectory mDirectory = getUserSecurity().getMembersDir();
		if (stringOK(grpIn.getName())) {
			mDirectory.getGroups().getGroups().put(grpIn.getName(), grpIn);
		} else {

		}

		return false;
	}

	@Override
	public boolean addUpdateApp(OtfApplication appIn) {
		// TODO Auto-generated method stub
		return false;
	}

}
