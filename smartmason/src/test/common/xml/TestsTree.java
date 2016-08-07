/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * GNU Lesser General Public License
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

package test.common.xml;

import test.common.*;

import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 @author Giovanni Caire - TILAB
 @author Alessandro Negri - AOTLab UniPR
 @author Matteo Bisi - UniPR
 @author Yuri Ferrari - UniPR
 @author Rossano Vitulli - UniPR
 */
public class TestsTree extends JTree implements Serializable {
	public static final String DEFAULT_TREE_ROOT_NAME = "JADE Test Suite";
	
	private static final int      ELEMENT_TYPE = Node.ELEMENT_NODE;
	private static final int      ATTR_TYPE = Node.ATTRIBUTE_NODE;
	private static final int      TEXT_TYPE = Node.TEXT_NODE;
	private static final int      CDATA_TYPE = Node.CDATA_SECTION_NODE;
	private static final int      PROCINSTR_TYPE = Node.PROCESSING_INSTRUCTION_NODE;
	private static final int      COMMENT_TYPE = Node.COMMENT_NODE;
	private static final int      DOCUMENT_TYPE = Node.DOCUMENT_NODE;
	private static final int      DOCTYPE_TYPE = Node.DOCUMENT_TYPE_NODE;
	private static final int      NOTATION_TYPE = Node.NOTATION_NODE;
	
	// DOM node-types (Array indexes = nodeType() values.)
	private static final String[] typeName = {
		"none", "Element", "Attr", "Text", "CDATA", "ProcInstr", "Comment", "Document", "DocType", "Notation", 
	};
	
	// The elements in the dom tree that must be displyed in the JTree:
	// 0) All (used when displaying the selection dialog)
	// 1) Only the tests (used when displaying the tests in the group that is being executed)
	// 2) Functionalities and tests (used when displaying the final report after a RUN_ALL procedure)
	private static String[][]     allTreeElementNames = {
		{XMLManager.FUNC_TAG, XMLManager.FUNC_CLASS_TAG, XMLManager.FUNC_DESCRIPTION_TAG, XMLManager.FUNC_TESTSLIST_TAG, XMLManager.TEST_TAG, XMLManager.TEST_CLASS_TAG, XMLManager.TEST_WHAT_TAG, XMLManager.TEST_HOW_TAG, XMLManager.TEST_WHEN_TAG, XMLManager.TEST_ARG_TAG, XMLManager.FUNCTIONALITIES_TAG, XMLManager.TESTS_TAG}, 
		{XMLManager.FUNCTIONALITIES_TAG, XMLManager.FUNC_TAG, XMLManager.FUNC_TESTSLIST_TAG, XMLManager.TESTS_TAG, XMLManager.TEST_TAG}
	};
	
	private String[] treeElementNames = allTreeElementNames[1];
	
	private Document document;
	private String rootName = DEFAULT_TREE_ROOT_NAME;
	private boolean compress = true;
	
	/**
	 */
	public TestsTree() {
	}
	
	/**
	 Shows the test hierarchy defined in the given XML file
	 */
	public boolean showTestsHierarchy(String xmlFileName, String rootName, boolean expand) {
		if (rootName != null) {
			this.rootName = rootName;
		}
		else {
			this.rootName = DEFAULT_TREE_ROOT_NAME;
		}
		
		if ((document = XMLManager.getDocument(xmlFileName)) != null) {
			setModel(new DomToTreeModelAdapter());
			
			if (expand) {
				expandAll();
			} 
			return true;
		} 
		else {
			return false;
		}
	} 
	
	public String getDisplayContent(Object n) {
		return ((AdapterNode) n).content();
	}
	
	public FunctionalityDescriptor getFunctionality(Object n) {
		Element el = (Element) ((AdapterNode) n).domNode;
		return XMLManager.getFunctionalityDescriptor(el);
	}
	
	public Object getSelectedTest() {
		TreePath[] paths = getSelectionPaths();
		if (paths != null && paths.length == 1) {
			return paths[0].getLastPathComponent();
		} 
		return null;
	} 
	
	public void fireTestChanged(final String testName, final boolean expand) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DomToTreeModelAdapter fModel = (DomToTreeModelAdapter) getModel();
				
				Vector vpath = new Vector();
				int    index = fModel.buildPath(testName, fModel.getRoot(), vpath);
				if (index >= 0) {
					Object[] path = new Object[vpath.size()];
					vpath.copyInto(path);
					TreePath treePath = new TreePath(path);
					fModel.fireTreeNodesChanged(treePath, index);
					if (expand) {
						Object[] fullPath = new Object[vpath.size()+1];
						vpath.copyInto(fullPath);
						fullPath[vpath.size()] = fModel.getChild(treePath.getLastPathComponent(), index);
						TreePath fullTreePath = new TreePath(fullPath);
						scrollPathToVisible(fullTreePath);
					} 
				} 
			} 
		});
	} 
	
	/**
	 Expand the tree
	 */
	public void expandAll() {
		for (int row = 0; row < getRowCount(); ++row) {
			expandRow(row);
		} 
	} 
	
	
	public void setTreeElementNames(int value) {
		treeElementNames = allTreeElementNames[value];
	} 
	
	
	private boolean treeElement(String elementName) {
		for (int i = 0; i < treeElementNames.length; i++) {
			if (elementName.equals(treeElementNames[i])) {
				return true;
			} 
		} 
		return false;
	} 
	
	/**
	 * Inner class AdapterNode.
	 * This class wraps a DOM node into an Object that has a
	 * String representation consistent with what we want to display
	 * in the tree.
	 * Some utility methods to provide children, index values and
	 * child counts are also provided.
	 */
	private class AdapterNode implements Serializable {
		Node domNode;
		
		/**
		 * Construct an Adapter node from a DOM node
		 */
		private AdapterNode(Node node) {
			domNode = node;
		}
		
		/**
		 * Return a String representation of the wrapped node consistent
		 * with what we want to display in the tree.
		 */
		public String toString() {
			// Default value
			String s = typeName[domNode.getNodeType()];
			
			if (domNode instanceof Element) {
				Element e = (Element) domNode;
				String  tag = e.getTagName();
				if (tag.equalsIgnoreCase(XMLManager.FUNC_TAG) || tag.equalsIgnoreCase(XMLManager.TEST_TAG)) {
					// The string representation is: <name-attribute> [SKIPPED]
					s = e.getAttribute(XMLManager.NAME_ATTR);
					if ("true".equalsIgnoreCase(e.getAttribute(XMLManager.SKIP_ATTR))) {
						s += " SKIPPED";
					} 
				} 
				else if (tag.equalsIgnoreCase(XMLManager.TEST_ARG_TAG)) {
					// The string representation is: Arg: <key-attribute> = <value-attribute>
					s = "Arg: "+e.getAttribute(XMLManager.KEY_ATTR)+" = "+e.getAttribute(XMLManager.VALUE_ATTR);
				} 
				else {
					// The string representation is just the tag
					s = tag;
				} 
			} 
			
			if (s.equals("Notation")) {
				// The root
				s = rootName;
			} 
			return s;
		} 
		
		/**
		 * Return the content to be displayed when this
		 * <code>AdapterNode</code> is selected.
		 */
		private String content() {
			String s = "";
			if (domNode instanceof Element) {
				Element e = (Element) domNode;
				String  tag = e.getTagName();
				if (tag.equalsIgnoreCase(XMLManager.FUNC_TAG)) {
					// Display the content of the <Description> sub-element
					s = XMLManager.getContent(XMLManager.getSubElement(e, XMLManager.FUNC_DESCRIPTION_TAG));
				} 
				else if (tag.equalsIgnoreCase(XMLManager.TEST_TAG)) {
					// Display the content of the <What> sub-element
					s = XMLManager.getContent(XMLManager.getSubElement(e, XMLManager.TEST_WHAT_TAG));
				} 
				else {
					// Display the content of this element
					s = XMLManager.getContent(e);
				} 
			} 
			return s;
		} 
		
		/*
		 * Return children, index, and count values
		 */
		private int index(AdapterNode child) {
			int count = childCount();
			for (int i = 0; i < count; i++) {
				AdapterNode n = this.child(i);
				if (child.domNode == n.domNode) {
					return i;
				} 
			} 
			return -1;
		} 
		
		private AdapterNode child(int searchIndex) {
			// Note: JTree index is zero-based.
			Node node = domNode.getChildNodes().item(searchIndex);
			if (compress) {
				// Return Nth displayable node
				int elementNodeIndex = 0;
				for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
					node = domNode.getChildNodes().item(i);
					if (node.getNodeType() == ELEMENT_TYPE && treeElement(node.getNodeName()) && elementNodeIndex++ == searchIndex) {
						break;
					} 
				} 
			} 
			return new AdapterNode(node);
		} 
		
		private int childCount() {
			if (!compress) {
				return domNode.getChildNodes().getLength();
			} 
			int count = 0;
			for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
				org.w3c.dom.Node node = domNode.getChildNodes().item(i);
				if (node.getNodeType() == ELEMENT_TYPE && treeElement(node.getNodeName())) {
					// Note:
					// Have to check for proper type.
					// The DOCTYPE element also has the right name
					++count;
				} 
			} 
			return count;
		} 
	}    // END of Inner class AdapterNode
	
	/**
	 * Inner class DomToTreeModelAdapter.
	 * This adapter converts the current Document (a DOM) into
	 * a JTree model.
	 */
	private class DomToTreeModelAdapter implements TreeModel, Serializable {
		// Basic TreeModel operations
		public Object getRoot() {
			return new AdapterNode(document);
		} 
		
		public boolean isLeaf(Object aNode) {
			// Determines whether the icon shows up to the left.
			// Return true for any node with no children
			AdapterNode node = (AdapterNode) aNode;
			Node        n = node.domNode;
			if (n.getNodeName().equalsIgnoreCase(XMLManager.FUNC_TESTSLIST_TAG)) {
				Document d = XMLManager.getDocument(n.getFirstChild().getNodeValue());
				if (d == null) {
					return true;
				} 
				NodeList nl = d.getElementsByTagName(XMLManager.TESTS_TAG);
				node.domNode = (Node) nl.item(0);
				return false;
			} 
			else {
				return (node.childCount() == 0);
			} 
		} 
		
		/**
		 * Return the number of child elements of an element in the tree
		 */
		public int getChildCount(Object parent) {
			AdapterNode node = (AdapterNode) parent;
			return node.childCount();
		} 
		
		/**
		 * Return the child of a given element at position <code>index</code>
		 */
		public Object getChild(Object parent, int index) {
			AdapterNode node = (AdapterNode) parent;
			return node.child(index);
		} 
		
		/**
		 * Return the index of a child of a given element in the tree
		 */
		public int getIndexOfChild(Object parent, Object child) {
			AdapterNode node = (AdapterNode) parent;
			return node.index((AdapterNode) child);
		} 
		
		public void valueForPathChanged(TreePath path, Object newValue) {
			// Null. We won't be making changes in the GUI
			// If we did, we would ensure the new value was really new,
			// adjust the model, and then fire a TreeNodesChanged event.
		} 
		
		/*
		 * Use these methods to add and remove event listeners.
		 * (Needed to satisfy TreeModel interface, but not used.)
		 */
		private Vector listenerList = new Vector();
		
		public void addTreeModelListener(TreeModelListener listener) {
			if (listener != null &&!listenerList.contains(listener)) {
				listenerList.addElement(listener);
			} 
		} 
		
		public void removeTreeModelListener(TreeModelListener listener) {
			if (listener != null) {
				listenerList.removeElement(listener);
			} 
		} 
		
		/*
		 * Invoke these methods to inform listeners of changes.
		 * (Not needed for this example.)
		 * Methods taken from TreeModelSupport class described at
		 * http://java.sun.com/products/jfc/tsc/articles/jtree/index.html
		 * That architecture (produced by Tom Santos and Steve Wilson)
		 * is more elegant. I just hacked 'em in here so they are
		 * immediately at hand.
		 */
		public void fireTreeNodesChanged(TreePath path, int index) {
			int[]          indices = {index};
			Object[]       changedChildren = {
					getChild(path.getLastPathComponent(), index)
			};
			TreeModelEvent e = new TreeModelEvent(this, path, indices, changedChildren);
			
			Enumeration    listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesChanged(e);
			} 
		} 
		
		public void fireTreeNodesInserted(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesInserted(e);
			} 
		} 
		
		public void fireTreeNodesRemoved(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesRemoved(e);
			} 
		} 
		public void fireTreeStructureChanged(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeStructureChanged(e);
			} 
		} 
		
		/**
		 Build the path from "node" to "target".
		 The path will not include the target node
		 */
		private int buildPath(String target, Object node, Vector path) {
			if (target.equals(node.toString())) {
				// If target == node the path is empty
				return 0;
			}
			else {
				// Otherwise if there is a path from one of node's children to target 
				// --> insert node at the beginning of the path
				for (int i = 0; i < getChildCount(node); i++) {
					int index = buildPath(target, getChild(node, i), path);
					if (index >= 0) {
						path.insertElementAt(node, 0);
						return i;
					}
				}
			}
			// Thre is no path from node to target
			return -1;
		}
	}    // END of Inner class DomToTreeModelAdapter
	
}
