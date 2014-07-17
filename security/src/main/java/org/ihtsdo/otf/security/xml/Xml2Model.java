package org.ihtsdo.otf.security.xml;

import java.util.List;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountStore;
import org.ihtsdo.otf.security.dto.OtfAccounts;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfApplications;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectories;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfGroups;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.xml.base.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Xml2Model {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(Xml2Model.class
			.getName());

	UserSecurity userSecurity;

	public final UserSecurity build(final Document confDoc) throws Exception {
		buildUserSecurity(confDoc.getDocumentElement());
		return userSecurity;
	}

	private void buildUserSecurity(final Element elem) {
		userSecurity = new UserSecurity();
		buildTopDirs(elem);
		buildTopApps(elem);

	}

	private void buildTopDirs(final Element elem) {
		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_DIRS, elem);
		for (Element el : namedChildren) {
			buildDirs(el);
		}
	}

	private void buildDirs(final Element elem) {

		OtfDirectories dirs = new OtfDirectories();

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_DIR, elem);
		for (Element el : namedChildren) {
			OtfDirectory dir = buildDir(el);
			dirs.getDirectories().put(dir.getName(), dir);

		}
		userSecurity.setDirs(dirs);
	}

	private OtfDirectory buildDir(final Element elem) {
		OtfDirectory dir = new OtfDirectory();
		dir.setIdref(elem.getAttribute(XmlStatics.XML_A_ID_REF));
		dir.setName(elem.getAttribute(XmlStatics.XML_A_NAME));
		dir.setDescription(elem.getAttribute(XmlStatics.XML_A_DESC));
		dir.setStatus(elem.getAttribute(XmlStatics.XML_A_STAT));
		// Groups
		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_GRPS, elem);
		for (Element el : namedChildren) {
			dir.setGroups(buildGroups(el));
		}
		// Accounts
		namedChildren = XMLUtil.getChildElemsListByName(XmlStatics.XML_E_ACCS,
				elem);
		for (Element el : namedChildren) {
			dir.setAccounts(buildAccounts(el));
		}

		return dir;
	}

	private OtfGroups buildGroups(final Element elem) {
		OtfGroups ogrps = new OtfGroups();

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_GRP, elem);
		for (Element el : namedChildren) {
			OtfGroup grp = buildGroup(el);
			ogrps.getGroups().put(grp.getName(), grp);
		}

		return ogrps;
	}

	private OtfGroup buildGroup(final Element elem) {
		OtfGroup grp = new OtfGroup();
		grp.setIdref(elem.getAttribute(XmlStatics.XML_A_ID_REF));
		grp.setName(elem.getAttribute(XmlStatics.XML_A_NAME));
		grp.setDescription(elem.getAttribute(XmlStatics.XML_A_DESC));
		grp.setStatus(elem.getAttribute(XmlStatics.XML_A_STAT));
		// CustomData
		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_CUSTD, elem);
		for (Element el : namedChildren) {
			OtfCustomData cust = buildCustData(el,
					OtfCustomData.CustomParentType.GROUP);
			grp.setCustData(cust);
		}

		// CustFields - allow people to edit fields as direct children of group.
		namedChildren = XMLUtil.getChildElemsListByName(XmlStatics.XML_E_CUST,
				elem);
		for (Element el : namedChildren) {
			OtfCustomField cust = buildCustField(el);
			grp.getCustData().getCustFields().put(cust.getKey(), cust);
		}

		// Accounts
		namedChildren = XMLUtil.getChildElemsListByName(XmlStatics.XML_E_ACCS,
				elem);
		for (Element el : namedChildren) {
			grp.setAccounts(buildAccounts(el));
		}
		return grp;

	}

	private OtfCustomData buildCustData(final Element elem,
			OtfCustomData.CustomParentType parentType) {
		OtfCustomData custData = new OtfCustomData(parentType);
		// CustomFields

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_CUST, elem);
		for (Element el : namedChildren) {
			OtfCustomField cust = buildCustField(el);
			custData.getCustFields().put(cust.getKey(), cust);
		}
		return custData;

	}

	private OtfAccounts buildAccounts(final Element elem) {
		OtfAccounts accs = new OtfAccounts();

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_ACC, elem);
		for (Element el : namedChildren) {
			OtfAccount acc = buildAccount(el);
			accs.getAccounts().put(acc.getName(), acc);
		}

		return accs;
	}

	private OtfAccount buildAccount(final Element elem) {
		OtfAccount acc = new OtfAccount();
		acc.setIdref(elem.getAttribute(XmlStatics.XML_A_ID_REF));
		acc.setName(elem.getAttribute(XmlStatics.XML_A_NAME));
		acc.setGivenName(elem.getAttribute(XmlStatics.XML_A_GNAME));
		acc.setMiddleName(elem.getAttribute(XmlStatics.XML_A_MNAME));
		acc.setSurname(elem.getAttribute(XmlStatics.XML_A_SNAME));
		acc.setEmail(elem.getAttribute(XmlStatics.XML_A_EMAIL));
		acc.setStatus(elem.getAttribute(XmlStatics.XML_A_STAT));

		// CustomData
		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_CUSTD, elem);
		for (Element el : namedChildren) {
			OtfCustomData cust = buildCustData(el,
					OtfCustomData.CustomParentType.ACCOUNT);
			acc.setCustData(cust);
		}

		// CustFields - allow people to edit fields as direct children of group.
		namedChildren = XMLUtil.getChildElemsListByName(XmlStatics.XML_E_CUST,
				elem);
		for (Element el : namedChildren) {
			OtfCustomField cust = buildCustField(el);
			acc.getCustData().getCustFields().put(cust.getKey(), cust);
		}
		return acc;
	}

	private OtfCustomField buildCustField(final Element elem) {
		OtfCustomField cust = new OtfCustomField();
		cust.setValue(elem.getAttribute(XmlStatics.XML_A_VAL));
		cust.setKey(elem.getAttribute(XmlStatics.XML_A_KEY));
		cust.init();
		return cust;

	}

	private void buildTopApps(final Element elem) {

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_APPS, elem);
		for (Element el : namedChildren) {
			buildApps(el);
		}
	}

	private void buildApps(final Element elem) {
		OtfApplications apps = new OtfApplications();
		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_APP, elem);
		for (Element el : namedChildren) {
			OtfApplication app = buildApp(el);
			apps.getApplications().put(app.getName(), app);

		}
		userSecurity.setApps(apps);

	}

	private OtfApplication buildApp(final Element elem) {
		OtfApplication app = new OtfApplication();
		app.setIdref(elem.getAttribute(XmlStatics.XML_A_ID_REF));
		app.setName(elem.getAttribute(XmlStatics.XML_A_NAME));
		app.setDescription(elem.getAttribute(XmlStatics.XML_A_DESC));
		app.setStatus(elem.getAttribute(XmlStatics.XML_A_STAT));

		List<Element> namedChildren = XMLUtil.getChildElemsListByName(
				XmlStatics.XML_E_ACS, elem);
		for (Element el : namedChildren) {
			OtfAccountStore oAs = buildAccountStore(el);
			app.getAccountStores().put(oAs.getName(), oAs);
		}
		return app;
	}

	private OtfAccountStore buildAccountStore(final Element elem) {
		OtfAccountStore oAs = new OtfAccountStore();
		oAs.setIdref(elem.getAttribute(XmlStatics.XML_A_ID_REF));
		oAs.setName(elem.getAttribute(XmlStatics.XML_A_NAME));
		oAs.setType(elem.getAttribute(XmlStatics.XML_A_ACCS_TYPE));
		oAs.setStatus(elem.getAttribute(XmlStatics.XML_A_STAT));
		return oAs;

	}

}
