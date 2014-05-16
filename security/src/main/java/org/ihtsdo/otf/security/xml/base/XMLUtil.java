/*
 * XMLUtil.java
 *
 * Created on 22 April 2005, 10:03
 */

package org.ihtsdo.otf.security.xml.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Adam Flinton
 */

public final class XMLUtil {

	private static DocumentBuilder mDB;
	// private static final Log log = LogFactory.getLog(XMLUtil.class);
	private static final Logger LOG = Logger.getLogger(XMLUtil.class.getName());
	private static SimpleErrorHandler mErrorhandler = new SimpleErrorHandler();
	public static final String XPATH_FACTORY = "net.sf.saxon.xpath.XPathFactoryImpl";

	// RESERVED CHARS

	public static final String SPACE = " ";
	public static final String EQUALS = "=\"";
	public static final String QUOTE = "\"";

	public static final String GT = ">";
	public static final String GT_A = "&gt;";
	public static final String LT = "<";
	public static final String LT_A = "&lt;";
	public static final String QUOTE_A = "&quot;";
	public static final String APOS = "'";
	public static final String APOS_A = "&apos;";
	public static final String AND = "&";
	public static final String AND_A = "&amp;";
	public static final String NBSP_A = "&nbsp;";

	public static String encodePlainText4XML(final String content) {

		String temp = "";
		String result = "";
		if (content != null) {
			try {
				// just the chars in normal plain text which will screw up the
				// XML
				result = content.replaceAll(LT, LT_A);
				temp = result.replaceAll(GT, GT_A);
				result = temp.replaceAll(QUOTE, QUOTE_A);
				temp = result.replaceAll(APOS, APOS_A);
				return temp.replaceAll(AND, AND_A);

			} catch (Exception excep) {
				System.out.println("ex in encodeString4XML content = "
						+ content + " result = " + result);
			}
		}

		return content;
	}

	public static String decodeXML(final String xML) {

		String result = xML.replaceAll(LT_A, LT);
		result = result.replaceAll(GT_A, GT);
		result = result.replaceAll(QUOTE_A, QUOTE);
		result = result.replaceAll(APOS_A, APOS);
		result = result.replaceAll(AND_A, AND);
		result = result.replaceAll(NBSP_A, SPACE);

		/*
		 * if(result.contains("amp;")){ log.severe("decodeXML Orig XML = "+XML);
		 * log.severe("decodeXML Final XML = "+result); }
		 */

		/*
		 * if(result.contains("&")){
		 * log.severe("decodeXML Final XML contains amp = "+result); }
		 */
		return result;
	}

	public static String encodeXMLReplaceAmp(final String content) {

		if (content != null) {
			try {
				// log.severe("encodeXMLReplaceAmp content = "+content);
				// change all &

				String c1 = content.replaceAll("&", AND_A);
				// then set back those which are OK
				String c2 = c1.replaceAll("&amp;quot", QUOTE_A);
				String c3 = c2.replaceAll("&amp;apos", APOS_A);
				String c4 = c3.replaceAll("&amp;lt", LT_A);
				String c5 = c4.replaceAll("&amp;gt", GT_A);
				return c5.replaceAll("&amp;amp;", AND_A);

			} catch (Exception exec) {
				// log.error("ex in encodeXMLReplaceAmp content = " + content,
				// E);
				LOG.log(Level.SEVERE, "ex in encodeXMLReplaceAmp content = "
						+ content, exec);
			}

		}

		/*
		 * if(content.contains("amp;")){
		 * log.severe("encodeXMLReplaceAmp content2 = "+content); }
		 */

		return "";
	}

	/**
	 * Builds and returns a document indicated by the filename/url
	 * 
	 * @param fileName
	 * @return The Document
	 */
	public static Document getDocument(final String fileName) throws Exception {
		Document doc = null;
		// try {
		if (mDB == null) {
			getDocumentBuilder();
		}
		doc = mDB.parse(new File(fileName));

		/*
		 * } catch (Exception e) { log.severe(
		 * "Error thrown n XMLUtil.getDocument(String fileName) fileName = " +
		 * fileName, e); }
		 */
		return doc;
	}

	public static DocumentBuilderFactory getDBF() {
		DocumentBuilderFactory fac = getDBF(true);
		return fac;

	}

	public static DocumentBuilderFactory getDBF(final boolean namespaceaware) {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setValidating(false);
		fac.setNamespaceAware(namespaceaware);
		fac.setXIncludeAware(true);

		return fac;
	}

	/**
	 * Builds and returns a document created from the supplied inputStream
	 * 
	 * @param is
	 * @return The document
	 */

	public static synchronized Document getDocument(final InputStream is,
			final String systemID) throws Exception {
		Document doc = null;
		try {
			if (mDB == null) {
				getDocumentBuilder();
			}
			doc = mDB.parse(is, systemID);

			// XMLUtil.logNode("XMLUtil getDocument 147 ", doc);

		} catch (SAXParseException sx) {
			LOG.severe("SAXParseException in getDocument(InputStream is) \n"
					+ " line number = " + sx.getLineNumber()
					+ "\n column number = " + sx.getColumnNumber());
			throw sx;
		}
		return doc;
	}

	public static synchronized Document getDocument(final InputSource is)
			throws Exception {
		Document doc = null;
		try {
			if (mDB == null) {
				getDocumentBuilder();
			}
			// doc = mDB.parse(is, SystemID);
			doc = mDB.parse(is);
			// XMLUtil.logNode("XMLUtil getDocument 169 ", doc);

		} catch (SAXParseException sx) {
			LOG.severe("SAXParseException in getDocument(InputSource is) \n"
					+ " line number = " + sx.getLineNumber()
					+ "\n column number = " + sx.getColumnNumber());
			throw sx;
		}
		return doc;
	}

	public static synchronized Document getDocument(final String fileName,
			final boolean namespaceaware) {
		// log.severe("getDocument called ");
		Document doc = null;
		try {
			DocumentBuilder lDBIgNS = getDBF(namespaceaware)
					.newDocumentBuilder();
			lDBIgNS.setErrorHandler(mErrorhandler);
			doc = lDBIgNS.parse(new File(fileName));

		} catch (Exception e) {
			// log.error("Error thrown in XMLUtil.getDocument(String fileName,boolean namespaceaware)) Exception = ",e);
			LOG.log(Level.SEVERE,
					"Error thrown in XMLUtil.getDocument(String fileName,boolean namespaceaware)) Exception = ",
					e);
			// Failed to build the document
		}

		return doc;
	}

	/**
	 * Builds & returns a document given a String containing XML
	 * 
	 * @param xmlString
	 * @param namespaceaware
	 * @return The Document
	 */

	public static synchronized Document getDocumentFromXMLString(
			final String xmlString, final boolean namespaceaware)
			throws Exception {
		Document doc = null;
		// Remove any Non XML Entities
		String newXmlString = removeNonXMLEntities(xmlString);

		try {
			DocumentBuilder db = getDBF(namespaceaware).newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(newXmlString)));

		} catch (Exception e) {
			// Failed to build the document
			/*
			 * log.error(
			 * "Error thrown in XMLUtil.getDocumentFromXMLString(String xmlString,boolean namespaceaware) xmlString = "
			 * + xmlString + " namespaceaware = " + namespaceaware, e);
			 */

			LOG.log(Level.SEVERE,
					"Error thrown in XMLUtil.getDocumentFromXMLString(String xmlString,boolean namespaceaware) xmlString = "
							+ xmlString + " namespaceaware = " + namespaceaware,
					e);

			throw new Exception(e);
		}
		return doc;
	}

	/**
	 * Writes XML supplied as an DOM Document to a place indicated by the
	 * supplied filename string
	 * 
	 * @param document
	 * @param fileName
	 * @throws Exception
	 */

	public static void writeXMLToFile(final Document document,
			final String fileName) throws Exception {
		writeXMLToStream(document, new FileOutputStream(new File(fileName)));
	}

	/**
	 * Writes XML supplied as an DOM Document to an OutputStream
	 * 
	 * @param document
	 * @param stream
	 * @throws Exception
	 */

	/*
	 * public static void writeXMLToStream(Document document, OutputStream
	 * stream) throws Exception { OutputFormat format = new
	 * OutputFormat(document); // Serialize DOM //
	 * log.severe("format encoding = "+format.getEncoding());
	 * format.setIndent(4); // format.setEncoding(CommonProps.DEFAULTENC);
	 * XMLSerializer serial = new XMLSerializer(stream, format);
	 * serial.serialize(document);
	 * 
	 * }
	 */

	public static void writeXMLToStream(final Document document,
			final OutputStream stream) throws Exception {
		if (document == null) {
			LOG.severe("You have passed me a null object");
		}
		if (document != null) {
			StreamResult result = new StreamResult(stream);
			DOMSource domSource = new DOMSource(document);
			Transformer transformer = ProcessXSLT.getEmptyTransformer();
			transformer.transform(domSource, result);
		}
	}

	/**
	 * Given a DOM this returns the XML as string formatted for pretty printing
	 * 
	 * @param document
	 * @return String
	 * @throws Exception
	 */

	public static String writeXMLToString(final Document document) {
		String output = "";
		if (document == null) {
			LOG.severe("You have passed me a null object");
		}
		if (document != null) {
			try {
				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult(sw);
				DOMSource domSource = new DOMSource(document);
				Transformer transformer = ProcessXSLT.getEmptyTransformer();
				transformer.transform(domSource, result);
				output = sw.toString();
			} catch (Exception excep) {
				// log.error("Error thrown in XMLUtil.writeXMLToString", E);
				LOG.log(Level.SEVERE,
						"Error thrown in XMLUtil.writeXMLToString", excep);
			}
		}
		return output;
	}

	/*
	 * public static final String writeXMLToString(Document document) { String
	 * output = ""; if (document == null) {
	 * log.severe("You have passed me a null object");
	 * 
	 * } if (document != null) { try { StringWriter sw = new StringWriter();
	 * OutputFormat format = new OutputFormat(document); // Serialize //
	 * log.severe("format encoding = "+format.getEncoding()); // DOM
	 * format.setIndent(4); XMLSerializer serial = new XMLSerializer(sw,
	 * format); serial.serialize(document); output = sw.toString(); } catch
	 * (Exception excep) { //
	 * log.error("Error thrown in XMLUtil.writeXMLToString", E);
	 * log.log(Level.SEVERE, "Error thrown in XMLUtil.writeXMLToString", E); } }
	 * return output; }
	 */

	/**
	 * Creates & returns an empty DOM document
	 * 
	 * @return The Document
	 * @throws Exception
	 */

	public static Document getEmptyDocument() {
		Document dom = null;
		try {
			if (mDB == null) {
				mDB = getDBF().newDocumentBuilder();
			}
			dom = mDB.newDocument();
		} catch (Exception e) {
			// log.error("Exception thrown in XMLUtil.getEmptyDocument", e);
			LOG.log(Level.SEVERE,
					"Exception thrown in XMLUtil.getEmptyDocument ", e);
		}
		return dom;
	}

	/**
	 * Writes out a File
	 * 
	 * @param document
	 * @param filePath
	 * @param tag
	 * @param folder
	 * @return The String indicating the path the file was written to
	 * @throws Exception
	 */

	public static String writeFile(final Document document,
			final String filePath, final String tag, final String folder)
			throws Exception {
		String upPath = null;

		File test = new File(filePath);
		if (folder != null) {
			upPath = test.getParent() + File.separator + folder;
			if (!new File(upPath).isDirectory()) {
				boolean b = new File(upPath).mkdir();
				if (!b) {
					LOG.severe("writeFile tried to create a required directory but the process failed path = "
							+ upPath);
				}
			}

			if (test.getName().lastIndexOf(".") != -1) {
				upPath = test.getParent()
						+ File.separator
						+ folder
						+ File.separator
						+ tag
						+ test.getName().substring(0,
								test.getName().lastIndexOf('.')) + ".xml";
			} else {
				upPath = test.getParent() + File.separator + folder
						+ File.separator + tag + test.getName() + ".xml";
			}
		} else {
			upPath = test.getParent()
					+ File.separator
					+ tag
					+ test.getName().substring(0,
							test.getName().lastIndexOf('.')) + ".xml";
		}

		// Stream the result to file.
		writeXMLToStream(document, new FileOutputStream(new File(upPath)));

		return upPath;
	}

	/**
	 * Gets a DOM DocumentBuilder
	 * 
	 * @return The DOM DocumentBuilder
	 */

	public static DocumentBuilder getDocumentBuilder() {
		try {
			if (mDB == null) {
				mDB = getDBF().newDocumentBuilder();
				mDB.setErrorHandler(mErrorhandler);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return mDB;
	}

	/**
	 * Converts a DOM node to String
	 * 
	 * @param node
	 * @return The node converted to string
	 * @throws TransformerException
	 */

	public static String convertToStringStripCDATA(final Node node)
			throws TransformerException {
		Transformer transformer = ProcessXSLT.getEmptyTransformer();
		// transformer.setOutputProperty(OutputKeys.ENCODING,
		// CommonProps.DEFAULTENC);

		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(node), new StreamResult(
				stringWriter));
		return removeCDATA(stringWriter.getBuffer().toString());

	}

	/**
	 * Converts a DOM node to String
	 * 
	 * @param node
	 * @return The node converted to string
	 * @throws TransformerException
	 */

	public static String convertToStringLeaveCDATA(final Node node)
			throws TransformerException {

		Transformer transformer = ProcessXSLT.getEmptyTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING,
				CommonXMLStatics.DEFAULTENC);
		StringWriter stringWriter = new StringWriter();
		DOMSource ds = null;
		try {
			ds = new DOMSource(node);
		} catch (Exception execep) {
			// log.error("Error thrown creating DOMSource from Node "+
			// node.getNodeName(), Ex);
			LOG.log(Level.SEVERE, "Error thrown creating DOMSource from Node "
					+ node.getNodeName(), execep);

		}

		transformer.transform(ds, new StreamResult(stringWriter));
		StringBuffer buffer = stringWriter.getBuffer();

		return buffer.toString();
	}

	/*
	 * public static final String CDATANamedElementString(String NodeAsString,
	 * String ElementName){
	 * 
	 * log.severe("CDATANamedElementString called ElementName = "+ElementName);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return NodeAsString; }
	 * 
	 * public static final String UNCDATANamedElementString(String NodeAsString,
	 * String ElementName){
	 * 
	 * 
	 * 
	 * log.severe("UNCDATANamedElementString called ElementName = "+ElementName);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return NodeAsString; }
	 */

	/**
	 * 
	 * @author rana
	 * 
	 *         A Simple Error Handling class
	 * 
	 */

	private static class SimpleErrorHandler implements ErrorHandler {
		SimpleErrorHandler() {
		}

		// The following methods are standard SAX ErrorHandler methods.
		// See SAX documentation for more info.

		@Override
		public void warning(final SAXParseException spe) {
		}

		@Override
		public void error(final SAXParseException spe) throws SAXParseException {
			throw spe;
		}

		@Override
		public void fatalError(final SAXParseException spe)
				throws SAXParseException {
			throw spe;
		}
	}

	/*
	 * public static Document addUuidAttrDoc(Document Doc, String Attname) {
	 * 
	 * try { // System.out.println("addUuidAttrDoc(Document Doc, String Attname
	 * ) // called"); Element root = Doc.getDocumentElement(); //
	 * System.out.println("addUuidAttrDoc Root = "+root.getNodeName()); root =
	 * addUuidAttrElems(root, Attname); //
	 * System.out.println("addUuidAttrDoc Root2 = "+root.getNodeName());
	 * 
	 * // System.out.println("Document post adding uuid = //
	 * "+writeXMLToString(Doc)); } catch (Exception excep) { System.out
	 * .println(
	 * "Exception in addUuidAttrDoc(Document Doc, String Attname ) Ex = " + E);
	 * } return Doc; }
	 */

	/**
	 * Will create a documentFragment of the replacingDocument, will import the
	 * replacingDocument as a node of the replacedDocument, and then will
	 * replace the replaceNode with the documentFragment of replacingDocument.
	 * 
	 * @param replacedDocument
	 *            The document which will have a node replace
	 * @param replacingDocument
	 *            The document that will replace a node
	 * @param replacedNode
	 *            The node in replacedDocument that will be replaced
	 * @return The new version of replacedDocument will replacedNode replaced
	 */
	public static Node replaceNode(final Document replacedDocument,
			final Document replacingDocument, final Node replacedNode) {

		// Create a documentFragment of the replacingDocument
		DocumentFragment docFrag = replacingDocument.createDocumentFragment();
		Element rootElement = replacingDocument.getDocumentElement();
		docFrag.appendChild(rootElement);

		// Import docFrag under the ownership of replacedDocument
		Node replacingNode = ((replacedDocument).importNode(docFrag, true));

		// In order to replace the node need to retrieve replacedNode's parent
		Node replaceNodeParent = replacedNode.getParentNode();
		replaceNodeParent.replaceChild(replacingNode, replacedNode);
		return replacedDocument;
	}

	public static Node replaceNode(final Document impD, final Node oldE,
			final Node newE) {
		Node parent = oldE.getParentNode();
		Node impNewNode = impD.importNode(newE, true);
		parent.replaceChild(impNewNode, oldE);

		return parent;
	}

	public static Node insertNewAfter(final Document impD, final Node targetE,
			final Node newE) {
		// System.out.println("targetE "+targetE.getLocalName());
		Node impNewNode = impD.importNode(newE, true);
		Node parent = targetE.getParentNode();
		if (targetE.getNextSibling() == null) {
			parent.appendChild(impNewNode);
		} else {
			parent.insertBefore(impNewNode, targetE.getNextSibling());
		}
		return impNewNode;
	}

	public static boolean replaceNodeWithNodeList(final Document impD,
			final Node oldE, final NodeList nl) {
		boolean isOK = true;

		if (nl.getLength() == 1) {
			Node newNode = nl.item(0);
			// System.out.println("replaceNodeWithNodeList  1 NewNode Found newNode = "+
			// newNode.getLocalName());
			if (newNode != null && newNode != oldE) {
				XMLUtil.replaceNode(impD, oldE, newNode);
			}
			if (newNode == oldE) {
				isOK = false;
				// System.out.println("replaceNodeWithNodeList newNode is the same as node supplied");
			}
		}

		if (nl.getLength() > 1) {
			// System.out.println("replaceNodeWithNodeList multiple nodes found  num = "+
			// nl.getLength());
			if (nl.item(0).getNodeType() != Node.ELEMENT_NODE) {
				LOG.severe("Sorry for multiple nodes I only handle Elements");
				System.out
						.println("Sorry for multiple nodes  I only handle Elements");
			}

			else {
				Node anchor = null;
				for (int y = 0; y < nl.getLength(); y++) {
					Node node = nl.item(y);
					Element elementE = (Element) node;

					// System.out.println("Inserting Elem y = "+y);

					if (y == 0 && oldE != elementE) {
						anchor = insertNewAfter(impD, oldE, elementE);
					}
					if (y > 0 && anchor != elementE) {
						anchor = insertNewAfter(impD, anchor, elementE);
					}
				}

				oldE.getParentNode().removeChild(oldE);
				// impD.removeChild(oldE);
			}
		}

		return isOK;
	}

	/*
	 * public static Element addUuidAttrElems(Element elem, String Attname) { //
	 * System.out.println("addUuidAttrElems(Element elem, String Attname //
	 * called "); elem = addUuidAttrElem(elem, Attname); //
	 * System.out.println("elem = addUuidAttrElem(elem, Attname );"); NodeList
	 * pl = elem.getChildNodes(); int index = pl.getLength(); //
	 * System.out.println("addUuidAttrElems index = "+index); int i = 0; while
	 * (i < index) { // System.out.println("addUuidAttrElems i = "+i); Node
	 * DomNode = pl.item(i); if ((DomNode.getNodeType()) ==
	 * org.w3c.dom.Node.ELEMENT_NODE) { Element el = (Element) DomNode; el =
	 * addUuidAttrElems(el, Attname); } i++; } return elem; }
	 */

	/*
	 * public static Element addUuidAttrElem(Element elem, String Attname) { //
	 * System.out.println("addUuidAttrElem(Element elem, String Attname ) //
	 * called"); // If att exists if (elem.getAttribute(Attname).equals("")) {
	 * // Get a UUID & add it to the element as an id attrib String uuid =
	 * HistoryItemHandler.getHistoryItemHandler().getUUID();
	 * elem.setAttribute(Attname, uuid); } // if the attrib doesn't exist if
	 * (elem.getAttribute(Attname) == null) { // Get a UUID & add it to the
	 * element as an id attrib String uuid =
	 * HistoryItemHandler.getHistoryItemHandler().getUUID();
	 * elem.setAttribute(Attname, uuid); } return elem; }
	 */

	public static String removeCDATA(final String xml) {

		String cStart = "<![CDATA[";
		String cEnd = "]]>";

		int loc = xml.indexOf(cStart);
		// System.out.println("loc = "+loc);
		if (loc > -1) {
			String frontchop = remove(xml, cStart, loc);
			loc = frontchop.indexOf(cEnd);
			return remove(frontchop, cEnd, loc);
		}
		return xml;
	}

	public static String addCDATA(final String xML) {

		// log.severe("addCDATA called XML = "+XML);

		String cStart = "<![CDATA[";
		String cEnd = "]]>";

		return new StringBuilder().append(cStart).append(xML).append(cEnd)
				.toString();

	}

	private static String remove(final String str, final String toRemove,
			final int location) {

		StringBuffer buffer = new StringBuffer(str);
		return buffer.replace(location, location + toRemove.length(), "")
				.toString();

	}

	/**
	 * Removes all children of the specified node.
	 * 
	 * @param node
	 *            the node.
	 */
	public static void removeAllChildren(final Node node) {
		Node child;
		while ((child = node.getFirstChild()) != null) {
			node.removeChild(child);
		}
	}

	public static boolean uniqueXpath(final String xPath, final Document doc) {
		boolean isUnique = false;

		try {
			// NodeList nl = getNodeListXpath(XPath, Doc);
			NodeList nl = getNodesListXpathNode(xPath, doc);
			int nlLen = nl.getLength();
			// System.out.println("UniqueXpath nl length = "+nlLen +" XPath =
			// "+XPath);
			if (nlLen == 1) {
				isUnique = true;
			}

		} catch (Exception exec) {
			System.out.println("Exception in XMLUtil.UniqueXpath Ex = " + exec);
		}
		return isUnique;
	}

	public static NodeList getNodesListXpathNode(final String xPath,
			final Node node) throws Exception {
		return (NodeList) getNodesListXpath(xPath, node, "", "",
				XPathConstants.NODESET);
	}

	public static ArrayList<String> getNodeListAttValAsStringCol(
			final String xPath, final Node node, final String attrName)
			throws Exception {
		ArrayList<String> retV = new ArrayList<String>();

		NodeList nl = getNodesListXpathNode(xPath, node);
		int l = nl.getLength();
		Element e = null;
		String val = "";

		for (int i = 0; i < l; i++) {
			e = (Element) nl.item(i);
			if (e.getNodeType() == Node.ELEMENT_NODE) {
				val = e.getAttribute(attrName);
				if (val != null && val.length() > 0) {
					// log.info("getNodeListAttValAsStringCol val = "+val
					// +" attname = "+attrName);
					/*
					 * try { log.info(convertToStringLeaveCDATA(e));
					 * }catch(Exception excep) { E.printStackTrace(); }
					 */
					retV.add(val);
				}
			}
		}
		return retV;
	}

	public static ArrayList<String> getNodeListAttValAsStringCols(
			final String xPath, final Node node, final String[] attrNames,
			final String sep) throws Exception {
		ArrayList<String> retV = new ArrayList<String>();

		String locSep = " ";

		if (sep != null) {
			locSep = sep;
		}
		int aNamesL = attrNames.length;
		if (aNamesL > 0) {

			NodeList nl = getNodesListXpathNode(xPath, node);
			int l = nl.getLength();
			Element e = null;
			String val = "";

			for (int i = 0; i < l; i++) {
				e = (Element) nl.item(i);
				if (e.getNodeType() == Node.ELEMENT_NODE) {
					StringBuilder sb = new StringBuilder();
					for (int y = 0; y < aNamesL; y++) {
						sb.append(e.getAttribute(attrNames[y]));
						if (y < aNamesL - 1) {
							sb.append(locSep);
						}
					}
					val = sb.toString();
					if (val != null && val.length() > 0) {
						// log.info("getNodeListAttValAsStringCol val = "+val
						// +" attrNames = "+attrNames);
						/*
						 * try { log.info(convertToStringLeaveCDATA(e));
						 * }catch(Exception excep) { E.printStackTrace(); }
						 */
						retV.add(val);
					}
				}
			}
		}
		return retV;
	}

	// Return type is one of XPathConstants .BOOLEAN, .NODE, .NODESET, .NUMBER,
	// .STRING
	public static Object getNodesListXpath(final String xPathS,
			final Node node, final String nsuri, final String pre,
			final QName returnType) throws Exception {
		Object matches = null;
		// TODO move this to a generic start up method
		System.setProperty("javax.xml.xpath.XPathFactory:"
				+ XPathConstants.DOM_OBJECT_MODEL, XPATH_FACTORY);

		XPathFactory xpathFactory = XPathFactory
				.newInstance(XPathConstants.DOM_OBJECT_MODEL);
		XPath xpath = xpathFactory.newXPath();
		XPathExpression xpe = xpath.compile(xPathS);
		matches = xpe.evaluate(node, returnType);

		return matches;
	}

	/*
	 * public static String evaluateXPathDoc(Document dom, String xpath) {
	 * return evaluateXPathNode(dom,xpath); }
	 */

	public static String evaluateXPathNode(final Node inNode, final String xpath)
			throws Exception {

		Node node = selectSingleNode(xpath, inNode);
		if (node != null) {
			return (convertToStringLeaveCDATA(node));
		}

		return "";
	}

	public static boolean evaluateXPathBool(final Node inNode,
			final String xpath) throws Exception {
		Boolean xPathBool = (Boolean) getNodesListXpath(xpath, inNode, "", "",
				XPathConstants.BOOLEAN);
		return xPathBool.booleanValue();
	}

	/*
	 * public static Node selectSingleNode(String xPath, Document Doc, String
	 * nsuri, String pre) { return (Node)getNodesListXpath(Xpath, Doc, nsuri,
	 * pre,XPathConstants.NODE); }
	 */

	public static Node selectSingleNode(final String xPath, final Node inNode,
			final String nsuri, final String pre) throws Exception {
		return (Node) getNodesListXpath(xPath, inNode, nsuri, pre,
				XPathConstants.NODE);
	}

	/*
	 * public static Node selectSingleNodeDoc(String xPath, Document Doc) { //
	 * TODO put a node type checker so it can be used for documents or nodes
	 * return selectSingleNode(Xpath, Doc, "", ""); }
	 */

	public static Node selectSingleNode(final String xPath, final Node inNode)
			throws Exception {
		// TODO put a node type checker so it can be used for documents or nodes
		return selectSingleNode(xPath, inNode, "", "");
	}

	public static String selectXPathString(final String xPath,
			final Node inNode, final String nsuri, final String pre)
			throws Exception {
		return (String) getNodesListXpath(xPath, inNode, nsuri, pre,
				XPathConstants.STRING);
	}

	public static String selectXPathString(final String xPath, final Node inNode)
			throws Exception {
		return selectXPathString(xPath, inNode, "", "");
	}

	public static String removeNonXMLEntities(final String xML) {

		String result = xML.replaceAll("&nbsp;", " ");
		// result = result.replaceAll("&nbsp;", "&#160;");
		// escape any remaining & elements
		result = encodeXMLReplaceAmp(result);
		return result;

	}

	public static String getNamespaceURI(final Document doc, final String ns) {
		NamedNodeMap attr = doc.getDocumentElement().getAttributes();
		for (int i = 0; i < attr.getLength(); i++) {
			Node attrNode = attr.item(i);
			if (attrNode.getNodeName().indexOf(ns) != -1) {
				return (attrNode.getNodeValue());
			}
		}
		return null;
	}

	public static String getPrefix(final Node node) {
		if (node.getNodeName().indexOf(":") != -1) {
			return node.getNodeName().substring(0,
					node.getNodeName().indexOf(":"));
		}
		return "";
	}

	/** Decide if the node is text, and so must be handled specially */
	public static boolean isTextNode(final Node n) {
		if (n == null) {
			return false;
		}
		short nodeType = n.getNodeType();
		return nodeType == Node.CDATA_SECTION_NODE
				|| nodeType == Node.TEXT_NODE;
	}

	public static Element getFirstElementChild(final Node node) {

		Element elem = null;
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode
				.getNextSibling()) {
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				elem = (Element) childNode;
				return elem;
			}
		}
		return elem;

	}

	public static Vector<Element> getChildElemsByName(final String name,
			final Node parent) {
		Vector<Element> v = new Vector<Element>();
		Element elem = null;
		for (Node childNode = parent.getFirstChild(); childNode != null; childNode = childNode
				.getNextSibling()) {
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if (childNode.getNodeName() == name) {
					elem = (Element) childNode;
					v.add(elem);
				}
			}
		}
		return v;
	}

	public static List<Element> getChildElemsListByName(final String name,
			final Node parent) {
		ArrayList<Element> v = new ArrayList<Element>();
		Element elem = null;
		for (Node childNode = parent.getFirstChild(); childNode != null; childNode = childNode
				.getNextSibling()) {
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if (childNode.getNodeName() == name) {
					elem = (Element) childNode;
					v.add(elem);
				}
			}
		}
		return v;
	}

	public static Document createDocFromNode(final Node node) {
		Document doc = null;
		try {
			doc = getEmptyDocument();
			Node impNode = doc.importNode(node, true);
			doc.appendChild(impNode);

		} catch (Exception excep) {
			// log.severe("Exception in createDocFromNode", E);
			LOG.log(Level.SEVERE, "Exception in createDocFromNode", excep);
		}

		return doc;
	}

	public static int getCurrentPosition(final Node refNode) {
		if (refNode == null) {
			return -1;
		}

		int counter = 0;
		Node current = refNode;

		while (current != null) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				counter++;
			}

			current = current.getPreviousSibling();
		}

		return counter;
	}

	// Parses a string containing XML and returns a DocumentFragment
	// containing the nodes of the parsed XML.
	public static DocumentFragment parseXmlFragment(final Document doc,
			final String fragment) {
		// Wrap the fragment in an arbitrary element
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<fragment>").append(fragment).append("</fragment>");
		try {
			// Create a DOM builder and parse the fragment
			Document d = getDBF().newDocumentBuilder().parse(
					new InputSource(new StringReader(sbuild.toString())));

			// Import the nodes of the new document into doc so that they
			// will be compatible with doc
			Node node = doc.importNode(d.getDocumentElement(), true);

			// Create the document fragment node to hold the new nodes
			DocumentFragment docfrag = doc.createDocumentFragment();

			// Move the nodes into the fragment
			while (node.hasChildNodes()) {
				docfrag.appendChild(node.removeChild(node.getFirstChild()));
			}

			// Return the fragment
			return docfrag;
		} catch (SAXException saxe) {
			// A parsing error occurred; the xml input is not valid
			LOG.log(Level.SEVERE, "Error thrown in XMLUtil.parseXmlFragment",
					saxe);

		} catch (ParserConfigurationException pcee) {
			LOG.log(Level.SEVERE, "Error thrown in XMLUtil.parseXmlFragment",
					pcee);
		} catch (IOException ioee) {
			LOG.log(Level.SEVERE, "Error thrown in XMLUtil.parseXmlFragment",
					ioee);
		}
		return null;
	}

	public static XPathDTO splitXpath(final String xPath) {

		String regex = "/";

		XPathDTO xd = new XPathDTO();
		xd.setXPath(xPath);

		String[] bits = xPath.split(regex);

		// System.out.println(bits.length);

		String startChars = "";
		int z = 1;
		for (int i = 0; i < bits.length; i++) {

			if (bits[i].length() == 0) {
				// must be a /
				startChars = startChars + "/";

			}
			if (bits[i].length() > 0) {
				// add to HT
				Integer valI = Integer.valueOf(z);
				xd.getTokens().put(valI, bits[i]);
				z++;
			}

			// System.out.println("Bit = "+bits[i]+ "length =
			// "+bits[i].length());

		}
		xd.setStartChars(startChars);
		// System.out.println("xd Start Chars = "+xd.getStartChars());
		// System.out.println("xd HT size = "+xd.tokens.size());

		return xd;

	}

	public static String getXPFromXD(final int numTok, final XPathDTO xd) {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append(xd.getStartChars());

		int i = 1;

		while (i <= numTok) {
			Integer valI = Integer.valueOf(i);
			sbuild.append(xd.getTokens().get(valI));
			if (i < numTok) {
				sbuild.append('/');
			}
			i++;

		}
		return sbuild.toString();
	}

	public static String getXPFromXdGeneral(final int numTok, final int start,
			final XPathDTO xd) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(xd.getStartChars());

		int starti = start;

		while (starti <= numTok) {
			Integer valI = Integer.valueOf(starti);
			sbuild.append(xd.getTokens().get(valI));
			if (starti < numTok) {
				sbuild.append('/');
			}
			starti++;

		}
		return sbuild.toString();
	}

	public static String getXPFromXdGlobal(final int start, final XPathDTO xd) {
		Integer valI = Integer.valueOf(start);

		StringBuilder sbuild = new StringBuilder();
		return sbuild.append(xd.getStartChars())
				.append(xd.getTokens().get(valI)).toString();

	}

	public static String getAttAsString(final String name, final String val) {

		StringBuffer sb = new StringBuffer();
		sb.append(" " + name + "=\"" + val + "\"");

		return sb.toString();
	}

	/**
	 * equivalent to the XPath expression './/tagName[@attrName='attrValue']'
	 */
	public static Element getElementByAttributeValue(final Node start,
			final String tagName, final String attrName, final String attrValue) {
		NodeList nl = ((Element) start).getElementsByTagName(tagName);
		int l = nl.getLength();

		if (l == 0) {
			return null;
		}

		Element e = null;
		String compareValue = null;

		for (int i = 0; i < l; i++) {
			e = (Element) nl.item(i);

			if (e.getNodeType() == Node.ELEMENT_NODE) {
				compareValue = e.getAttribute(attrName);

				if (compareValue.equals(attrValue)) {
					return e;
				}
			}
		}

		return null;
	}

	public static String processXslt(final String xmlInput,
			final String xsltFilename) {
		String retS = "";
		Transformer renderer = getTransformer(xsltFilename);
		retS = transform(renderer, xmlInput, "");
		return retS;
	}

	/*
	 * public static String processXslt(Node inNode, ActionDTO ad) { String RetS
	 * = ""; Transformer renderer = getTransformer(ad.getFilePath()); if
	 * (renderer != null) { renderer = XMLUtil.setStdTransParamsProps(renderer,
	 * ad .getCurrProps()); RetS = transform(renderer, inNode); } if (renderer
	 * == null) {
	 * log.severe("No transform possible as no renderer found, returning xml");
	 * try { RetS = XMLUtil.convertToStringLeaveCDATA(inNode); } catch
	 * (Exception excep) {
	 * log.severe("Exception trying to turn a node into string", E);
	 * log.log(Level.SEVERE, "ex in encodeXMLReplaceAmp content = " + content,
	 * E); } } return RetS; }
	 */

	/*
	 * public static String processXslt(Node inNode, String xsltFilename, String
	 * xmlFileName, boolean editable, String httpPath, String EdfPath) { String
	 * RetS = "";
	 * 
	 * Transformer renderer = getTransformer(xsltFilename); if (renderer !=
	 * null) { // log.severe("httpPath = "+httpPath); renderer =
	 * setStdTransParams(renderer, editable, xmlFileName,httpPath, EdfPath); //
	 * log.severe("httpPath2 = "+httpPath); RetS = Transform(renderer, inNode);
	 * // logNode("processXslt XMLUtil 1175",inNode); //
	 * log.severe("RetS straight after transform = "+RetS); } if (renderer ==
	 * null) { log
	 * .error("No transform possible as no renderer found, returning xml"); try
	 * { RetS = XMLUtil.convertToStringLeaveCDATA(inNode); } catch (Exception
	 * excep) { log.severe("Exception trying to turn a node into string", E); }
	 * }
	 * 
	 * return RetS; }
	 * 
	 * public static String processXslt(Node inNode, String xsltFilename, String
	 * xmlFileName, boolean editable, Properties params, String httpPath, String
	 * EdfPath) { String RetS = ""; // log.severe("httpPath " +httpPath);
	 * Transformer renderer = getTransformer(xsltFilename); if (renderer !=
	 * null) { renderer = setStdTransParams(renderer, editable,
	 * xmlFileName,httpPath, EdfPath); if (params != null && params.size() > 0)
	 * { renderer = setTransParams(renderer, params, EdfPath); } RetS =
	 * Transform(renderer, inNode); } if (renderer == null) { log
	 * .error("No transform possible as no renderer found, returning xml"); try
	 * { RetS = XMLUtil.convertToStringLeaveCDATA(inNode); } catch (Exception
	 * excep) { log.severe("Exception trying to tunr a node into string", E); }
	 * }
	 * 
	 * return RetS; }
	 * 
	 * public static String processXslt(Node inNode, String xsltFilename, String
	 * xmlFileName, String type, boolean editable, String httpPath, String
	 * EdfPath) {
	 * 
	 * // ProcessXSLT px = new ProcessXSLT();
	 * log.finest("processXslt called xsltFilename = " + xsltFilename +
	 * " xmlFileName = " + xmlFileName + " type = " + type);
	 * 
	 * String RetS = "";
	 * 
	 * Transformer renderer = getTransformer(xsltFilename);
	 * 
	 * if (renderer == null) { ConfigApplicationDTO cad =
	 * ConfigController.getConfig() .getAppConfigByName(type); // String
	 * abs_typehome = cad.getTypeHomeAbsolute(); String cadHome =
	 * cad.getCadURL(); // log.finest("processXslt abs_typehome = " +
	 * abs_typehome); xsltFilename = cadHome + File.separator + xsltFilename;
	 * log.finest("About to try & get " + xsltFilename); renderer =
	 * getTransformer(xsltFilename); }
	 * 
	 * if (renderer != null) { // log.severe("httpPath = "+httpPath); renderer =
	 * setStdTransParams(renderer, editable, xmlFileName,httpPath, EdfPath);
	 * RetS = Transform(renderer, inNode); } if (renderer == null) { log
	 * .error("No transform possible as no renderer found, returning xml"); try
	 * { RetS = XMLUtil.convertToStringLeaveCDATA(inNode); } catch (Exception
	 * excep) { log.severe("Exception trying to turn a node into string", E); }
	 * } return RetS;
	 * 
	 * }
	 */

	public static String transform(final Transformer renderer, final Node inNode) {

		/*
		 * Usually I'd use a DOMSource but Xalan on JDK 1.4 seems to have a
		 * problem with that So the dom has to be read out to string &
		 * reloaded....why this happens (only on JDK1.4) is a mystery. If an
		 * answer is found then the code below would be much more efficient
		 * DOMSource ds = new DOMSource(getDocument(xmlFileName, type));
		 */
		try {
			String enc = inNode.getOwnerDocument().getInputEncoding();
			String inputxml = XMLUtil.convertToStringLeaveCDATA(inNode);
			// log.severe("inputxml = "+inputxml);
			return transform(renderer, inputxml, enc);
		} catch (Exception excep) {
			// log.error("Error in Transform ", E);
			LOG.log(Level.SEVERE, "Error in Transform ", excep);
		}

		return "";
	}

	public static String transform(final Transformer renderer,
			final String inputxml, final String enc) {

		String locenc = CommonXMLStatics.DEFAULTENC;
		// String defEnc = CommonProps.DEFAULTENC;
		// log.severe("Enc = "+enc);
		if (enc == null || enc.length() == 0) {
			String docLevelEnc = BasicXMLParser.getFirstAttValue(inputxml,
					"encoding");
			if (docLevelEnc != null && docLevelEnc.length() > 0) {
				locenc = docLevelEnc;
			}
		} else {
			locenc = enc;
		}

		// log.severe("enc = "+enc);

		try {
			InputStream is = new ByteArrayInputStream(inputxml.getBytes(locenc));
			/*
			 * byte[] buf = inputxml.getBytes(docLevelEnc); InputStream is = new
			 * ByteArrayInputStream(buf); log.finest(inputxml);
			 */

			java.io.StringWriter sw = new java.io.StringWriter();
			renderer.transform(new StreamSource(is), new StreamResult(sw));
			return sw.toString();
			// log.finest("Transform RetS = " + RetS);
		} catch (Exception excep) {
			// log.severe("Exception in Transform input XML = " + inputxml, Ex);
			LOG.log(Level.SEVERE, "Exception in Transform input XML = ", excep);
		}
		return "";
	}

	public static Transformer getTransformer(final String xsltFileName) {

		Transformer renderer = null;
		ProcessXSLT px = new ProcessXSLT();
		try {
			renderer = px.getTransformer(xsltFileName);
		} catch (Exception excep) {
			LOG.severe("getTransformer Unable to get a transformer using "
					+ xsltFileName);
		}
		return renderer;
	}

	public static Transformer setStdTransParamsProps(
			final Transformer renderer, final Properties params) {

		// log.severe("setStdTransParamsProps params = "+params);

		if (params != null) {
			for (Enumeration<Object> e = params.keys(); e.hasMoreElements();) {

				String localkey = (String) e.nextElement();
				String val = params.getProperty(localkey);
				renderer.setParameter(localkey, val);
			}
		}
		return renderer;
	}

	/*
	 * public static Transformer setStdTransParams(Transformer renderer, boolean
	 * editable, String xmlFileName,String httpPath, String EdfPath) {
	 * 
	 * // log.severe("setStdTransParams httpPath = "+httpPath);
	 * 
	 * String EditPS = Boolean.toString(editable);
	 * renderer.setParameter(CommonProps.ACTION_PARAM_EDITABLE, EditPS);
	 * renderer.setParameter(CommonProps.ACTION_PARAM_SECRETUUID,
	 * CommonProps.SECRETUUID);
	 * renderer.setParameter(CommonProps.ACTION_PARAM_XMLFILENAME, xmlFileName);
	 * //renderer.setParameter(CommonProps.ACTION_PARAM_NODENAME, NodeName);
	 * renderer.setParameter(CommonProps.ACTION_PARAM_HTTP_PATH, httpPath); if
	 * (EdfPath != null && EdfPath.length() != 0) {
	 * renderer.setParameter(CommonProps.ACTION_PARAM_EDF_URL, EdfPath); }
	 * 
	 * 
	 * return renderer; }
	 * 
	 * public static Transformer setTransParams(Transformer renderer, Properties
	 * params, String EdfPath) {
	 * 
	 * if (params != null) {
	 * 
	 * for (Enumeration e = params.keys(); e.hasMoreElements();) {
	 * 
	 * String localkey = (String) e.nextElement(); String val =
	 * params.getProperty(localkey); renderer.setParameter(localkey, val); } if
	 * (EdfPath != null && EdfPath.length() != 0) {
	 * renderer.setParameter(CommonProps.ACTION_PARAM_EDF_URL, EdfPath); } }
	 * 
	 * return renderer; }
	 */
	public static void insertAfter(final Node newChild, final Node refChild) {
		if (refChild == null) {
			LOG.severe("refChild == null");
			throw new DOMException(DOMException.NOT_FOUND_ERR,
					"refChild == null");
		}

		Node nextSibling = refChild.getNextSibling();
		// log.severe("insertAfter 1 nextSibling ="+nextSibling);

		if (nextSibling == null) {
			// log.severe("insertAfter 2");
			// log.severe("refChild = "+refChild);
			// log.severe("newChild = "+newChild);
			// log.severe("refChild.getParentNode() ="+refChild.getParentNode());

			refChild.getParentNode().appendChild(newChild);
			// Parent.appendChild(newChild);

			// log.severe("insertAfter 3");
		} else {
			// log.severe("insertAfter 4");
			refChild.getParentNode().insertBefore(newChild, nextSibling);
			// Parent.insertBefore(newChild, nextSibling);
			// log.severe("insertAfter 5");
		}
	}

	public static void logNode(final String message, final Node nodeIn) {

		try {
			LOG.severe("logNode Message = " + message);
			LOG.severe("logNode XML = \n" + convertToStringLeaveCDATA(nodeIn));
		} catch (Exception excep) {
			// log.severe(E2);
			LOG.log(Level.SEVERE, "Exception in logNode", excep);
		}

	}

	public static String getpathToRoot(final Node nodeIn) {
		// log.severe("getpathToRoot called");
		String path = "";
		Node parent = null;
		int y = 0;
		if (nodeIn != null) {
			Node locNode = nodeIn;
			while (locNode.getParentNode() != null) {

				parent = locNode.getParentNode();
				if (y > 0) {
					path = ":" + path;
				}
				y++;
				int i = getCurrentPosition(locNode);
				i = i - 1;
				// log.finest("Current position of Node = " + i);
				// Element elem = (Element) n;
				// log.finest("Node = " + elem.getLocalName() +
				// " AttribName = "+
				// elem.getAttribute("name"));
				// Check to see it has children as otherwise the htmltree has a
				// problem with expanding leaves.
				if (nodeIn.hasChildNodes()) {
					path = i + path;
				}
				locNode = parent;

			}
		}

		// log.severe("path = " + path);
		if (path == null || path.length() == 0) {
			path = "0";

		}

		return path;
	}

	public static void moveDown(final Node currentN) {
		Node nextSibling = findNextElement(currentN, false);
		Node nextNextSibling = findNextElement(nextSibling, false);
		Node parent = currentN.getParentNode();
		parent.removeChild(currentN);
		if (nextNextSibling != null) {
			parent.insertBefore(currentN, nextNextSibling);
		} else {
			parent.appendChild(currentN);
		}

	}

	public static void moveUp(final Node currentN) {
		Node prevSibling = findPreviousElement(currentN, false);
		if (prevSibling != null) {
			Node parent = currentN.getParentNode();
			parent.removeChild(currentN);
			parent.insertBefore(currentN, prevSibling);
		}

	}

	public static Element findNextElement(final Node current,
			final boolean sameName) {
		String name = null;
		if (sameName) {
			name = current.getNodeName();
		}
		int type = Node.ELEMENT_NODE;
		return (Element) getNext(current, name, type);
	}

	public static Element findPreviousElement(final Node current,
			final boolean sameName) {
		String name = null;
		if (sameName) {
			name = current.getNodeName();
		}
		int type = Node.ELEMENT_NODE;
		return (Element) getPrevious(current, name, type);
	}

	public static Node getNext(final Node current, final boolean sameName) {
		String name = null;
		if (sameName) {
			name = current.getNodeName();
		}
		int type = current.getNodeType();
		return getNext(current, name, type);
	}

	public static Node getPrevious(final Node current, final boolean sameName) {
		String name = null;
		if (sameName) {
			name = current.getNodeName();
		}

		int type = current.getNodeType();
		return getPrevious(current, name, type);

	}

	public static Node getNext(final Node current, final String name,
			final int type) {
		Node next = current.getNextSibling();
		if (next == null) {
			return null;
		}

		for (Node node = next; node != null; node = node.getNextSibling()) {
			if (type >= 0 && node.getNodeType() != type) {
				continue;
			} else {
				if (name == null) {
					return node;
				}
				if (name.equals(node.getNodeName())) {
					return node;
				}
			}
		}
		return null;
	}

	public static Node getPrevious(final Node current, final String name,
			final int type) {
		Node prev = current.getPreviousSibling();
		if (prev == null) {
			return null;
		}

		for (Node node = prev; node != null; node = node.getPreviousSibling()) {

			if (type >= 0 && node.getNodeType() != type) {
				continue;
			} else {
				if (name == null) {
					return node;
				}
				if (name.equals(node.getNodeName())) {
					return node;
				}
			}
		}
		return null;
	}

	public static Node removeAllAttribFromNodeByName(final String attName,
			final Node target) throws Exception {
		System.out.println("removeAllAttribFromNodeByName called attName = "
				+ attName);
		String xPath = "//@" + attName + "/parent::*";
		NodeList nl = XMLUtil.getNodesListXpathNode(xPath, target);
		System.out
				.println("removeAllAttribFromNodeByName(String attName,Node target)called size = "
						+ nl.getLength());
		Node n = null;
		for (int y = 0; y < nl.getLength(); y++) {
			n = nl.item(y);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				el.removeAttribute(attName);
			}
		}

		return target;
	}

	public static synchronized Document removeAllAttribByName(
			final String attName, final Document doc) throws Exception {
		LOG.finest("removeSecretUUID Doc called");
		String xPath = "//@" + attName + "/parent::*";

		NodeList nl = XMLUtil.getNodesListXpathNode(xPath, doc);
		Node n = null;
		for (int y = 0; y < nl.getLength(); y++) {
			n = nl.item(y);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				el.removeAttribute(attName);
			}
		}
		return doc;
	}

	public static String logElement(final Element elem, final int level) {

		String estr = "";
		String indentText = "  ";
		String addIndT = "";

		// add indent
		int ind = 0;
		while (ind < level) {
			addIndT = addIndT + indentText;
			ind++;

		}
		String name = elem.getNodeName();
		estr = "\n" + addIndT + "<" + name + " ";
		// Attribs
		NamedNodeMap namedNodeMap = elem.getAttributes();
		StringBuilder sb = new StringBuilder(estr);
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Attr att = (Attr) namedNodeMap.item(i);
			sb.append(" " + estr + att.getName() + "=\"" + att.getNodeValue()
					+ "\" ");
			// Es = " " + Es + att.getName() + "=\"" + att.getNodeValue() +
			// "\" ";
		}
		sb.append(">");
		// Es = Es + ">";
		estr = sb.toString();
		NodeList pl = elem.getChildNodes();
		int index = pl.getLength();
		// text nodes
		if (index > 0) {
			int i = 0;
			while (i < index) {
				Node domNode = pl.item(i);
				if ((domNode.getNodeType()) == org.w3c.dom.Node.TEXT_NODE) {
					String Etext = domNode.getNodeValue();
					estr = estr + "\n " + addIndT + addIndT + Etext;
				}
				i++;
			}
		}
		// Child Elements
		if (index > 0) {
			// level++;
			int i = 0;
			while (i < index) {
				Node domNode = pl.item(i);
				if ((domNode.getNodeType()) == org.w3c.dom.Node.ELEMENT_NODE) {
					Element el = (Element) domNode;
					estr = estr + logElement(el, level + 1);
				}
				i++;
			}
		}
		estr = estr + "\n" + addIndT + "</" + name + ">";

		return estr;
	}

	public static boolean testXML(final String xmlText) {
		boolean isOK = false;

		try {
			getDocumentFromXMLString(xmlText, false);
			isOK = true;
		} catch (Exception excep) {
			// log.severe("testXML Exception thrown as string not well formed.",
			// E);
			LOG.log(Level.SEVERE,
					"testXML Exception thrown as string not well formed.",
					excep);
			isOK = false;
		}

		return isOK;
	}

	public static String replacePattern(final String str, final String pattern,
			final String replace) {
		LOG.finest("replacePattern called str = " + str + " pattern = "
				+ pattern + " replace = " + replace);
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

}
