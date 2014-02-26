package org.ihtsdo.otf.security.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.xml.base.XMLUtil;
import org.w3c.dom.Document;

public class XmlUserSecurity implements UserSecurityHandler {

	private String configFN;
	private String sortXsltFn = "";

	private UserSecurity userSecurity;

	private final Xml2Model xml2Mod = new Xml2Model();
	private final Model2Xml Mod2Xml = new Model2Xml();

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(XmlUserSecurity.class
			.getName());

	public XmlUserSecurity(String configFNIn) {
		super();
		configFN = configFNIn;

	}

	public XmlUserSecurity() {
		super();

	}

	public void initFromFile() throws Exception {
		init(configFN);
	}

	public void initFromDoc(final Document confDoc) throws Exception {
		userSecurity = xml2Mod.build(confDoc);
	}

	public void initFromXMLString(final String confXML) throws Exception {
		final Document confDoc = XMLUtil.getDocumentFromXMLString(confXML,
				false);
		userSecurity = xml2Mod.build(confDoc);
	}

	public final void init(final String configFN) throws Exception {
		final File confile = new File(configFN);
		if (confile.exists() && !confile.isDirectory() && confile.canRead()) {
			final Document confDoc = XMLUtil.getDocument(configFN);
			userSecurity = xml2Mod.build(confDoc);
		} else {
			LOG.severe("Something is wrong with the file you have specified file = "
					+ configFN);
			throw new Exception(
					"Config File specified but Not found config file = "
							+ configFN);
		}

	}

	public Document getXMLFromUserSecurity() {
		return Mod2Xml.getXML(userSecurity);
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
	public UserSecurity getUserSecurity() {
		return userSecurity;
	}

	@Override
	public void setUserSecurity(UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

	@Override
	public void saveUserSecurity() throws IOException {

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

}
