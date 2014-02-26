package org.ihtsdo.otf.security.xml.base;

// No copyright, no warranty; use as you will.
// Written by Adam Flinton, 2001
//
// Version 1.1
// Changes from version 1.01: New in 1.1

/**
 * Implementation of the ProcessXslt interface for the Xalan 2 XSLT processor.
 * 
 * @author Adam Flinton
 * @version 1.1
 */
// Imported TraX classes
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ProcessXSLT {

	// private static final Log log = LogFactory.getLog(ProcessXSLT.class);
	private static final Logger log = Logger.getLogger(ProcessXSLT.class
			.getName());

	// private static ObjectCache oc = new ObjectCache();
	/**
	 * Create a new ProcessXalan1 object.
	 */
	public ProcessXSLT() {
		super();
	}

	/**
	 * A Method which returns the template
	 */
	public static Templates getSheet(String fileName)
			throws TransformerException, TransformerConfigurationException,
			org.xml.sax.SAXException, java.io.FileNotFoundException, Exception {
		String full = fullqual(fileName);
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Templates s = tFactory.newTemplates(new StreamSource(full));
		return s; // return the cached copy.
	}

	/**
	 * Transforms an XML document stored in a file.
	 * 
	 * @param xmlfileName
	 *            The name of the file
	 * @param xslfileName
	 *            The name of the XSLT stylesheet to use
	 * 
	 * @return The output of the transformation
	 */
	public String transformFiles(String xmlfileName, String xslfileName)
			throws Exception {
		// GetFileURL gfu = new GetFileURL();
		String Result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		String full = fullqual(xmlfileName);
		// System.out.println("XMLFILE fullqual= " +full);
		InputSource src = new InputSource(full);
		String SrcURL = src.getSystemId();
		// System.out.println("XMLFILE SysID= " +SrcURL);
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		trans.transform(new StreamSource(SrcURL), new StreamResult(sw));
		Result = sw.toString();
		return Result;
	}

	/**
	 * Transforms an XML document stored in an InputStream.
	 * 
	 * @param srcStream
	 *            The InputStream
	 * @param xslfileName
	 *            The name of the XSLT stylesheet to use
	 * 
	 * @return The output of the transformation
	 */
	public String transformStream(java.io.InputStream srcStream,
			String xslfileName) throws Exception {
		// GetFileURL gfu = new GetFileURL();
		String Result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		// trans.setURIResolver(null);
		trans.transform(new StreamSource(srcStream), new StreamResult(sw));
		Result = sw.toString();
		return Result;
	}

	public InputStream transformStream2Stream(java.io.InputStream srcStream,
			String xslfileName) throws Exception {
		// GetFileURL gfu = new GetFileURL();
		// String Result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);

		// log.error("Encoding = "+src1.getEncoding()
		// +" for file :"+xslfileName);

		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		// java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// trans.setURIResolver(null);
		trans.transform(new StreamSource(srcStream), new StreamResult(out));

		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * Transforms an XML document stored in a string.
	 * 
	 * @param inputxml
	 *            The string
	 * 
	 * @return The output of the transformation
	 */
	public static String transformString(String inputxml)
			throws TransformerException, TransformerConfigurationException,
			Exception {
		String media = null, title = null, charset = null;
		String result = "";
		// GetFileURL gfu = new GetFileURL();
		String full = fullqual(inputxml);
		// System.out.println("XMLFILE fullqual= " +full);
		// InputSource src = new InputSource(gfu.getFileURL(xmlFilename));
		InputSource src = new InputSource(full);
		String SrcURL = src.getSystemId();
		// System.out.println("XMLFILE SysID= " +SrcURL);
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Source stylesheet = tFactory.getAssociatedStylesheet(
					new StreamSource(SrcURL), media, title, charset);
			Transformer transformer = tFactory.newTransformer(stylesheet);
			java.io.StringWriter sw = new java.io.StringWriter();
			transformer.transform(new StreamSource(SrcURL),
					new StreamResult(sw));
			// System.out.println("3");
			result = sw.toString();
			// System.out.println("************* The result is in foo.out
			// *************");
			// System.out.println("The result string is " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Transforms an XML document stored in a string.
	 * 
	 * @param inputxml
	 *            The string
	 * @param xslfileName
	 *            The name of the XSLT stylesheet to use
	 * 
	 * @return The output of the transformation
	 */
	public String transformString(String inputxml, String xslfileName)
			throws Exception {
		// GetFileURL gfu = new GetFileURL();
		String Result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		byte[] buf = inputxml.getBytes();
		InputStream is = new ByteArrayInputStream(buf);
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		trans.transform(new StreamSource(is), new StreamResult(sw));
		Result = sw.toString();
		return Result;
	}

	public Document transformDoc(Node inputxml, String xslfileName)
			throws Exception {

		DOMSource domSource = new DOMSource(inputxml);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		Transformer trans = s.newTransformer();

		trans.transform(domSource, result);

		return resultD;
	}

	public Document transformNodeProps(Node inputxml, String xslfileName,
			Properties props) throws Exception {

		DOMSource domSource = new DOMSource(inputxml);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		Transformer trans = s.newTransformer();
		trans.setOutputProperties(props);
		// trans.setParameter(name, value)

		trans.transform(domSource, result);

		return resultD;
	}

	public Document transformNodeIncDoc(Node inputxml, String xslfileName,
			String XMLFilename, Properties props) throws Exception {

		boolean isNode = false;

		if (inputxml.getNodeType() != Node.DOCUMENT_NODE) {
			isNode = true;
		}

		// log.error("xslfileName = "+xslfileName);
		String fullfn = fullqual(xslfileName);
		// log.error("fullfn = "+fullfn);
		InputSource src1 = new InputSource(fullfn);
		String SrcURL1 = src1.getSystemId();
		Templates s = getSheet(SrcURL1);
		Transformer trans = s.newTransformer();

		DOMSource domSource = null;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		if (isNode) {
			Document parent = inputxml.getOwnerDocument();
			domSource = new DOMSource(parent);
			// log.error("Setting P_Node");
			Element inputE = (Element) inputxml;
			String uuid = inputE.getAttribute(CommonXMLStatics.SECRETUUID);
			// log.error("uuid = "+uuid);
			trans.setParameter(CommonXMLStatics.ACTION_PARAM_NODELOCATE, uuid);
			if (props != null && props.size() > 0) {

				for (Enumeration e = props.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = props.getProperty(key);
					trans.setParameter(key, value);
				}

			}
			// XMLUtil.logNode("ProcessXSLT transformNodeIncDoc inputxml = ",inputxml);
		} else {
			domSource = new DOMSource(inputxml);
		}

		trans.setParameter(CommonXMLStatics.ACTION_PARAM_XMLFILENAME,
				XMLFilename);
		// Debug
		/*
		 * java.io.StringWriter sw = new java.io.StringWriter();
		 * trans.transform(domSource, new StreamResult(sw));
		 * log.error("transformed String = "+sw.toString());
		 */

		trans.transform(domSource, result);

		return resultD;
	}

	public Transformer getTransformer(String xslfilename) throws Exception {

		Templates s = getSheet(xslfilename);
		Transformer trans = s.newTransformer();

		return trans;
	}

	public static Transformer getEmptyTransformer()
			throws TransformerConfigurationException,
			TransformerFactoryConfigurationError {

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		Properties props = new Properties();
		props.put(OutputKeys.METHOD, "xml");
		props.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
		props.put(OutputKeys.INDENT, "yes");
		transformer.setOutputProperties(props);

		return transformer;
	}

	public static String fullqual(String filename) throws Exception {
		// System.out.println("Entering GFU.fullqual");
		String fname = filename;
		String name = "";
		String fqn = "";
		// String fpath = "file:///";
		java.net.URL fins = ProcessXSLT.class.getResource(filename);
		File f1;
		f1 = new File(fname);
		// System.out.println("Filename passed in = " + filename);
		// System.out.println("fname now = " + fname);
		if (f1.exists()) {
			try {
				fname = f1.toURL().toString();
				name = f1.getName();
			} catch (java.net.MalformedURLException mfu) {
				System.out
						.println("java.Net.MalfromedURLException caught in GetFileURL.fullqual Error = "
								+ mfu);
			}
			// fname = fpath + f1.getAbsolutePath();
			// System.out.println("File found thus fname now = " + fname);
			return fname;
		} // end checking if local file or URL
		else if (fins != null) {
			// System.out.println("Fins = " + fins.toString());
			return fins.toString();
		} else {
			// System.out.println("File not in a jar nor a local file thus fname
			// now = " + fname);

			fqn = fname;

			return fqn;
		}
	}

}
