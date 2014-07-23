package org.ihtsdo.otf.security.xml;

import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountStore;
import org.ihtsdo.otf.security.dto.OtfAccounts;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfGroups;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.xml.base.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Model2Xml {

	private Document doc;
	private UserSecurity userSecurity;
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(Model2Xml.class
			.getName());

	public Model2Xml() {
		super();
	}

	public final Document getXML(UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
		buildFromModel();
		return doc;
	}

	private void buildFromModel() {
		doc = null;
		final Element elem = getDoc().createElement(XmlStatics.XML_E_ROOT);
		buildTopDirs(elem);
		buildTopApps(elem);
		doc.appendChild(elem);
	}

	private void buildTopDirs(final Element elem) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_DIRS);
		for (OtfDirectory dir : userSecurity.getDirs().getDirectories()
				.values()) {
			buildDir(elemNew, dir);
		}

		elem.appendChild(elemNew);
	}

	private void buildDir(final Element elem, final OtfDirectory dir) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_DIR);
		elemNew.setAttribute(XmlStatics.XML_A_ID_REF, dir.getIdref());
		elemNew.setAttribute(XmlStatics.XML_A_NAME, dir.getName());
		addNotEmptyAtt(dir.getDescription(), XmlStatics.XML_A_DESC, elemNew);
		elemNew.setAttribute(XmlStatics.XML_A_STAT, dir.getStatus().toString());
		// Groups
		if (dir.getGroups() != null) {
			buildGroups(elemNew, dir.getGroups());
		}
		// Accounts
		if (dir.getAccounts() != null) {
			buildAccounts(elemNew, dir.getAccounts());
		}

		elem.appendChild(elemNew);
	}

	private void buildGroups(final Element elem, final OtfGroups grps) {
		if (!grps.getGroups().isEmpty()) {
			final Element elemNew = doc.createElement(XmlStatics.XML_E_GRPS);
			for (OtfGroup grp : grps.getGroups().values()) {
				buildGroup(elemNew, grp);
			}
			elem.appendChild(elemNew);
		}
	}

	private void buildGroup(final Element elem, final OtfGroup grp) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_GRP);
		elemNew.setAttribute(XmlStatics.XML_A_ID_REF, grp.getIdref());
		elemNew.setAttribute(XmlStatics.XML_A_NAME, grp.getName());
		addNotEmptyAtt(grp.getDescription(), XmlStatics.XML_A_DESC, elemNew);
		elemNew.setAttribute(XmlStatics.XML_A_STAT, grp.getStatus().toString());
		if (grp.getAccounts() != null) {
			buildAccounts(elemNew, grp.getAccounts());
		}
		if (!grp.getCustData().getCustFields().isEmpty()) {
			buildCustData(elemNew, grp.getCustData());
		}
		elem.appendChild(elemNew);
	}

	private void buildCustField(final Element elem, final OtfCustomField cField) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_CUST);
		elemNew.setAttribute(XmlStatics.XML_A_KEY, cField.getKey());
		elemNew.setAttribute(XmlStatics.XML_A_VAL, cField.getValue());
		elem.appendChild(elemNew);
	}

	private void buildAccounts(final Element elem, final OtfAccounts accs) {

		if (!accs.getAccounts().isEmpty()) {
			final Element elemNew = doc.createElement(XmlStatics.XML_E_ACCS);
			for (OtfAccount acc : accs.getAccounts().values()) {
				buildAccount(elemNew, acc);
			}
			elem.appendChild(elemNew);
		}

	}

	private void buildCustData(final Element elem, final OtfCustomData cdata) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_CUSTD);
		for (OtfCustomField cf : cdata.getCustFields().values()) {
			buildCustField(elemNew, cf);
		}
		elem.appendChild(elemNew);
	}

	private void buildAccount(final Element elem, final OtfAccount acc) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_ACC);
		elemNew.setAttribute(XmlStatics.XML_A_ID_REF, acc.getIdref());
		elemNew.setAttribute(XmlStatics.XML_A_NAME, acc.getName());
		elemNew.setAttribute(XmlStatics.XML_A_GNAME, acc.getGivenName());
		elemNew.setAttribute(XmlStatics.XML_A_MNAME, acc.getMiddleName());
		elemNew.setAttribute(XmlStatics.XML_A_SNAME, acc.getSurname());
		elemNew.setAttribute(XmlStatics.XML_A_EMAIL, acc.getEmail());
		elemNew.setAttribute(XmlStatics.XML_A_STAT, acc.getStatus().toString());
		if (!acc.getCustData().getCustFields().isEmpty()) {
			buildCustData(elemNew, acc.getCustData());
		}
		elem.appendChild(elemNew);
	}

	private void buildTopApps(final Element elem) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_APPS);
		for (OtfApplication app : userSecurity.getApps().getApplications()
				.values()) {
			buildApp(elemNew, app);
		}

		elem.appendChild(elemNew);
	}

	private void buildApp(final Element elem, final OtfApplication app) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_APP);
		elemNew.setAttribute(XmlStatics.XML_A_ID_REF, app.getIdref());
		elemNew.setAttribute(XmlStatics.XML_A_NAME, app.getName());
		addNotEmptyAtt(app.getDescription(), XmlStatics.XML_A_DESC, elemNew);
		elemNew.setAttribute(XmlStatics.XML_A_STAT, app.getStatus().toString());
		for (OtfAccountStore st : app.getAccountStores().values()) {
			buildAccStore(elemNew, st);
		}
		elem.appendChild(elemNew);
	}

	private void buildAccStore(final Element elem, final OtfAccountStore st) {
		final Element elemNew = doc.createElement(XmlStatics.XML_E_ACS);
		elemNew.setAttribute(XmlStatics.XML_A_ID_REF, st.getIdref());
		elemNew.setAttribute(XmlStatics.XML_A_NAME, st.getName());
		elemNew.setAttribute(XmlStatics.XML_A_ACCS_TYPE, st.getType());
		elemNew.setAttribute(XmlStatics.XML_A_STAT, st.getStatus().toString());
		elem.appendChild(elemNew);
	}

	public Document getDoc() {
		if (doc == null) {
			doc = XMLUtil.getEmptyDocument();
		}
		return doc;
	}

	public void setDoc(Document docIn) {
		doc = docIn;
	}

	private void addNotEmptyAtt(String val, String aName, Element el) {
		if (StringOk(val)) {
			el.setAttribute(aName, val);
		}
	}

	private boolean StringOk(String check) {
		if (check == null) {
			return false;
		}
		if (check.length() == 0) {
			return false;
		}
		return true;
	}

}
