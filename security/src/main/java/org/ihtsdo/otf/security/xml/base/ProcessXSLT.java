package org.ihtsdo.otf.security.xml.base;

/**
 * Implementation of the ProcessXslt interface for the Xalan 2 XSLT processor.
 * 
 * @author Adam Flinton , 2001
 * @version 1.1
 */
// Imported TraX classes
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.ihtsdo.otf.security.objectcache.ObjectCache;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ProcessXSLT {

	private static final Logger LOG = Logger.getLogger(ProcessXSLT.class
			.getName());

	/**
	 * Create a new ProcessXalan1 object.
	 */
	public ProcessXSLT() {
		super();
	}

	/**
	 * A Method which returns the template
	 */
	public static Templates getSheet(final String fileName) throws Exception {
		Templates s = (Templates) ObjectCache.INSTANCE.get(fileName);
		if (s == null) {
			synchronized (ProcessXSLT.class) // make thread safe
			{
				s = (Templates) ObjectCache.INSTANCE.get(fileName);
				if (s == null) {
					String full = fullqual(fileName);
					TransformerFactory tFactory = TransformerFactory
							.newInstance();
					s = tFactory.newTemplates(new StreamSource(full));
					ObjectCache.INSTANCE.put(fileName, s);
				}
			}
		}
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
	public final String transformFiles(final String xmlfileName,
			final String xslfileName) throws Exception {
		String result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		String full = fullqual(xmlfileName);
		InputSource src = new InputSource(full);
		String srcURL = src.getSystemId();
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		trans.transform(new StreamSource(srcURL), new StreamResult(sw));
		result = sw.toString();
		return result;
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
	public final String transformStream(final java.io.InputStream srcStream,
			final String xslfileName) throws Exception {
		String result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		trans.transform(new StreamSource(srcStream), new StreamResult(sw));
		result = sw.toString();
		return result;
	}

	public final InputStream transformStream2Stream(
			final java.io.InputStream srcStream, final String xslfileName)
			throws Exception {
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		Transformer trans = s.newTransformer();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
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
	public static String transformString(final String inputxml)
			throws Exception {
		String media = null, title = null, charset = null;
		String result = "";
		String full = fullqual(inputxml);
		InputSource src = new InputSource(full);
		String srcURL = src.getSystemId();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Source stylesheet = tFactory.getAssociatedStylesheet(
					new StreamSource(srcURL), media, title, charset);
			Transformer transformer = tFactory.newTransformer(stylesheet);
			java.io.StringWriter sw = new java.io.StringWriter();
			transformer.transform(new StreamSource(srcURL),
					new StreamResult(sw));
			result = sw.toString();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "An exception has occurred", e);
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
	public final String transformString(final String inputxml,
			final String xslfileName) throws Exception {
		String result = "";
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		byte[] buf = inputxml.getBytes();
		InputStream is = new ByteArrayInputStream(buf);
		java.io.StringWriter sw = new java.io.StringWriter();
		Transformer trans = s.newTransformer();
		trans.transform(new StreamSource(is), new StreamResult(sw));
		result = sw.toString();
		return result;
	}

	public final Document transformDoc(final Node inputxml,
			final String xslfileName) throws Exception {

		DOMSource domSource = new DOMSource(inputxml);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		Transformer trans = s.newTransformer();

		trans.transform(domSource, result);

		return resultD;
	}

	public final Document transformNodeProps(final Node inputxml,
			final String xslfileName, final Properties props) throws Exception {

		DOMSource domSource = new DOMSource(inputxml);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		Transformer trans = s.newTransformer();
		trans.setOutputProperties(props);
		trans.transform(domSource, result);

		return resultD;
	}

	public final Document transformNodeIncDoc(final Node inputxml,
			final String xslfileName, final String xMLFilename,
			final Properties props) throws Exception {

		boolean isNode = false;

		if (inputxml.getNodeType() != Node.DOCUMENT_NODE) {
			isNode = true;
		}
		String fullfn = fullqual(xslfileName);
		InputSource src1 = new InputSource(fullfn);
		String srcURL1 = src1.getSystemId();
		Templates s = getSheet(srcURL1);
		Transformer trans = s.newTransformer();

		DOMSource domSource = null;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document resultD = builder.newDocument();
		Result result = new DOMResult(resultD);

		if (isNode) {
			Document parent = inputxml.getOwnerDocument();
			domSource = new DOMSource(parent);
			Element inputE = (Element) inputxml;
			String uuid = inputE.getAttribute(CommonXMLStatics.SECRETUUID);
			trans.setParameter(CommonXMLStatics.ACTION_PARAM_NODELOCATE, uuid);
			if (props != null && props.size() > 0) {

				for (Enumeration<Object> e = props.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = props.getProperty(key);
					trans.setParameter(key, value);
				}

			}
		} else {
			domSource = new DOMSource(inputxml);
		}

		trans.setParameter(CommonXMLStatics.ACTION_PARAM_XMLFILENAME,
				xMLFilename);
		trans.transform(domSource, result);

		return resultD;
	}

	public final Transformer getTransformer(final String xslfilename)
			throws Exception {

		Templates s = getSheet(xslfilename);
		Transformer trans = s.newTransformer();

		return trans;
	}

	public static Transformer getEmptyTransformer()
			throws TransformerConfigurationException {

		String def = "DEFAULTTRANSFORMER";
		Transformer transformer = null;
		transformer = (Transformer) ObjectCache.INSTANCE.get(def);

		if (transformer == null) {
			synchronized (ProcessXSLT.class) // make thread safe
			{
				transformer = (Transformer) ObjectCache.INSTANCE.get(def);
				if (transformer == null) {
					// may have changed between first if and synch call...

					transformer = TransformerFactory.newInstance()
							.newTransformer();
					Properties props = new Properties();
					props.put(OutputKeys.METHOD, "xml");
					props.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
					props.put(OutputKeys.INDENT, "yes");
					transformer.setOutputProperties(props);
					ObjectCache.INSTANCE.put(def, transformer);
				}
			}
		}

		return transformer;
	}

	public static String fullqual(final String filename) throws Exception {
		String fname = filename;
		String fqn = "";
		java.net.URL fins = ProcessXSLT.class.getResource(filename);
		File f1;
		f1 = new File(fname);
		if (f1.exists()) {
			try {
				fname = f1.toURL().toString();
			} catch (java.net.MalformedURLException mfu) {
				LOG.log(Level.SEVERE,
						"java.Net.MalfromedURLException caught in GetFileURL.fullqual Error = ",
						mfu);
			}
			return fname;
		}
		// end checking if local file or URL
		else if (fins != null) {
			return fins.toString();
		} else {
			fqn = fname;
			return fqn;
		}
	}

}
