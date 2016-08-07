/*****************************************************************
 JADE - Java Agent DEvelopment Framework is a framework to develop 
 multi-agent systems in compliance with the FIPA specifications.
 Copyright (C) 2000 CSELT S.p.A. 
 
 GNU Lesser General Public License
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

package test.common.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.IOException;

import java.net.URL;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Elisabetta Cortese - TiLab
 * @author Giovanni Caire - TiLab
 */
public class XMLManager {
	// TestSuite tags and attributes
	public static final String FUNCTIONALITIES_TAG = "TesterList";
	public static final String TESTS_TAG = "TestsList";
	
	public static final String FUNC_TAG = "Tester";
	
	public static final String FUNC_CLASS_TAG = "ClassName";
	public static final String FUNC_DESCRIPTION_TAG = "Description";
	public static final String FUNC_TESTSLIST_TAG = "TestsListRif";
	
	public static final String TEST_TAG = "Test";
	
	public static final String TEST_CLASS_TAG = "TestClassName";
	public static final String TEST_WHAT_TAG = "WhatTest";
	public static final String TEST_HOW_TAG = "HowWorkTest";
	public static final String TEST_WHEN_TAG = "WhenTestPass";
	public static final String TEST_ARG_TAG = "Argument";
	
	public static final String NAME_ATTR = "name";
	public static final String SKIP_ATTR = "skip";
	public static final String KEY_ATTR = "key";
	public static final String VALUE_ATTR = "value";
	
	// Used to distinguish TestSuite tags from text formatting tags .
	private static String[] testSuiteTags = {
		FUNC_TAG,
		FUNC_CLASS_TAG,
		FUNC_DESCRIPTION_TAG,   
		FUNC_TESTSLIST_TAG,
		TEST_TAG,
		TEST_CLASS_TAG,
		TEST_WHAT_TAG,
		TEST_HOW_TAG,
		TEST_WHEN_TAG,
		TEST_ARG_TAG,
		FUNCTIONALITIES_TAG,
		TESTS_TAG	
	};
	
	public static boolean isTestSuiteTag(String tag) {
		for (int i=0; i < testSuiteTags.length; i++) {
			if (tag.equalsIgnoreCase(testSuiteTags[i])) { 
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Constructor for XMLManager.
	 */
	public XMLManager() {
	}
	
	/**
	 Retrieve the list (as an array of <code>FunctionalityDescriptor</code>
	 objects) of all functionalities tested by the TestSuite.
	 @param xmlFileName The name of the XML file describing 
	 the list of functionalities. 
	 */
	public static FunctionalityDescriptor[] getFunctionalities(String xmlFileName){
		List l = new ArrayList();
		Document doc = getDocument(xmlFileName);
		if (doc != null) {
			NodeList list = doc.getElementsByTagName(FUNC_TAG);
			
			for (int i = 0; i < list.getLength(); i++) {
				Element e = (Element)list.item(i);
				FunctionalityDescriptor fd = getFunctionalityDescriptor(e);
				l.add(fd);
			}
		}
		return (FunctionalityDescriptor[]) l.toArray(new FunctionalityDescriptor[0]);
	}
	
	/**
	 Retrieve the list (as an array of <code>TestDescriptor</code>
	 objects) of all tests related to a functionality.
	 @param xmlFileName The name of the XML file describing 
	 the list of tests. 
	 */
	public static TestDescriptor[] getTests(String xmlFileName){
		List l = new ArrayList();
		Document doc = getDocument(xmlFileName);
		if (doc != null) {
			NodeList list = doc.getElementsByTagName(TEST_TAG);
			
			for (int i = 0; i < list.getLength(); i++) {
				Element e = (Element)list.item(i);
				TestDescriptor td = getTestDescriptor(e);
				l.add(td);
			}
		}
		return (TestDescriptor[]) l.toArray(new TestDescriptor[0]);		
	}
	
	/**
	 Fill a <code>FunctionalityDescriptor</code> from a
	 DOM element node
	 */
	public static FunctionalityDescriptor getFunctionalityDescriptor(Element e){
		FunctionalityDescriptor fd = new FunctionalityDescriptor();		
		fd.setName(e.getAttribute(NAME_ATTR).trim());
		fd.setSkip(e.getAttribute(SKIP_ATTR).trim());
		
		NodeList children = e.getChildNodes();
		
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n instanceof Element) {
				Element e1 = (Element) n;
				String tag = e1.getTagName();
				if (tag.equals(FUNC_CLASS_TAG)) {
					fd.setTesterClassName(getContent(e1));
				}
				else if (tag.equals(FUNC_TESTSLIST_TAG)) {
					fd.setTestsListFile(getContent(e1));
				}
				else if (tag.equals(FUNC_DESCRIPTION_TAG)) {
					fd.setDescription(getContent(e1));
				}
			}
		}
		return fd;
	} 
	
	/**
	 Fill a <code>FunctionalityDescriptor</code> from a
	 DOM element node
	 */
	public static TestDescriptor getTestDescriptor(Element e){
		TestDescriptor td = new TestDescriptor();
		td.setName(e.getAttribute(NAME_ATTR).trim());
		td.setSkip(e.getAttribute(SKIP_ATTR).trim());
		
		NodeList children = e.getChildNodes();
		
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n instanceof Element) {
				Element e1 = (Element) n;
				String tag = e1.getTagName();
				if (tag.equals(TEST_CLASS_TAG)) {
					td.setTestClass(getContent(e1));
				}
				else if (tag.equals(TEST_WHAT_TAG)) {
					td.setWhat(getContent(e1));
				}
				else if (tag.equals(TEST_HOW_TAG)) {
					td.setHow(getContent(e1));
				}
				else if (tag.equals(TEST_WHEN_TAG)) {
					td.setPassedWhen(getContent(e1));
				}
				else if (tag.equals(TEST_ARG_TAG)) {
					td.setArg(e1.getAttribute(KEY_ATTR).trim(), e1.getAttribute(VALUE_ATTR).trim());
				}
			}
		}
		return td;
	} 
	
	public static String getContent(Element e) {
		String s = "";
		NodeList children = e.getChildNodes();
		
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			int type = n.getNodeType();
			switch (type) {
			case Node.ELEMENT_NODE:
				// Skip elements that are displayed as tree elements
				String tag = n.getNodeName();
				if (!isTestSuiteTag(tag)) {
					s += "<" + tag + ">";
					s += getContent((Element) n);
					s += "</" + tag + ">";
				}
				break;
			case Node.TEXT_NODE:
				s += n.getNodeValue();
				break;
			case Node.CDATA_SECTION_NODE:
				// Convert angle brackets and ampersands for display
				StringBuffer sb = new StringBuffer(n.getNodeValue());
				for (int j=0; j <sb.length(); j++) {
					if (sb.charAt(j) == '<') {
						sb.setCharAt(j, '&');
						sb.insert(j+1, "lt;");
						j += 3;
					} 
					else if (sb.charAt(j) == '&') {
						sb.setCharAt(j, '&');
						sb.insert(j+1, "amp;");
						j += 4;
					}
				}
				s += "<pre>" + sb + "\n</pre>";
				break;
			}
		}
		
		return s.trim();
	}
	
	public static Element getSubElement(Element e, String tag) {
		NodeList children = e.getChildNodes();
		
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n instanceof Element) {
				Element subEl = (Element) n;
				if (tag.equalsIgnoreCase(subEl.getTagName())) {
					return subEl;
				}
			}
		}
		return null;
	}
	
	public static Document getDocument(String fileName){
		Document doc= null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			URL url = ClassLoader.getSystemResource(fileName);
			if (url == null) {
				System.out.println("Can't find file "+fileName);
				return null;
			}
			doc = builder.parse(url.openStream());
			
		} catch (SAXParseException spe) {
			// Error generated by the parser
			System.out.println("\n** Parsing error"
					+ ", line " + spe.getLineNumber()
					+ ", uri " + spe.getSystemId());
			System.out.println("   " + spe.getMessage() );
			
			// Use the contained exception, if any
			Exception  x = spe;
			if (spe.getException() != null)
				x = spe.getException();
			x.printStackTrace();
			
		} catch (SAXException sxe) {
			// Error generated during parsing)
			Exception  x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
			
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
			
		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}
		return doc;
	}
}
