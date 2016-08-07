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

package test.common.testSuite.gui;

import test.common.xml.TestsTree;
import test.common.xml.FunctionalityDescriptor;

// Basic GUI components
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JEditorPane;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import java.io.Serializable;

/**
 * @author Giovanni Caire - TILAB
 * @author Elisabetta Cortese - TiLab
 * @author Alessandro Negri - AOTLab UniPR
 * @author Matteo Bisi - UniPR
 * @author Yuri Ferrari - UniPR
 * @author Rossano Vitulli - UniPR
 */
public class FunctionalitySelectionDlg extends JDialog implements Serializable {

  private static final int              windowHeight = 460;
  private static final int              leftWidth = 300;
  private static final int              rightWidth = 340;
  private static final int              windowWidth = leftWidth+rightWidth;

  private FunctionalityDescriptor selectedFunc = null;
  
  public static FunctionalityDescriptor showSelectionDlg(JFrame parent, String xmlFileName) {
  	FunctionalitySelectionDlg dlg = new FunctionalitySelectionDlg(parent, xmlFileName);
		return dlg.getSelectedFunc();
  }

  private FunctionalitySelectionDlg(JFrame parent, String xmlFile) {
  	super(parent, "Test List Details", true);

  	JPanel p = new JPanel();
  	p.setLayout(new BorderLayout());
  	
  	// Set a nice border
    EmptyBorder    eb = new EmptyBorder(5, 5, 5, 5);
    BevelBorder    bb = new BevelBorder(BevelBorder.LOWERED);
    CompoundBorder cb = new CompoundBorder(eb, bb);
    p.setBorder(new CompoundBorder(cb, eb));

    // Build left-side view
  	final TestsTree tree = new TestsTree();
  	tree.setTreeElementNames(0);  	
  	tree.showTestsHierarchy(xmlFile, null, false);
  	
    JScrollPane treeView = new JScrollPane(tree);
    treeView.setPreferredSize(new Dimension(leftWidth, windowHeight));

    // Build right-side view
    // (must be final to be referenced in inner class)
    final JEditorPane htmlPane = new JEditorPane("text/html", "");
    htmlPane.setEditable(false);
    JScrollPane htmlView = new JScrollPane(htmlPane);
    htmlView.setPreferredSize(new Dimension(rightWidth, windowHeight));

    // Wire the two views together. Use a selection listener
    // created with an anonymous inner-class adapter.
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath p = e.getNewLeadSelectionPath();
        if (p != null) {
        	String content = tree.getDisplayContent(p.getLastPathComponent());
          htmlPane.setText(content);
        } 
      } 
    });

    // Build split-pane view
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, htmlView);
    splitPane.setContinuousLayout(true);
    splitPane.setDividerLocation(leftWidth);
    splitPane.setPreferredSize(new Dimension(windowWidth+10, windowHeight+10));

    p.add(BorderLayout.CENTER, splitPane);

  	getContentPane().add(BorderLayout.CENTER, p);
  	
    // Buttons in the SOUTH part
    p = new JPanel();
    JButton bOk = new JButton("OK");
    JButton bCancel = new JButton("Cancel");
    // Adjust buttons size (note that the preferred size of the
    // cancel button can't be shrinked
    bOk.setPreferredSize(bCancel.getPreferredSize());

    // Handler for the OK button
    bOk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          TreePath path = tree.getSelectionPath();
          selectedFunc = tree.getFunctionality(path.getPathComponent(2));
        } 
        catch (Exception ex) {
          // Maybe the user didn't select a valid functionality.
          // In this case the OK button acts like the CANCEL one
        } 
        dispose();
      } 
    });
    p.add(bOk);

    // Handler for the CANCEL button
    bCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      } 
    });
    p.add(bCancel);
    getContentPane().add(p, BorderLayout.SOUTH);

    // Handler for the right corner exit button
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose();
      } 
    });

    // Visualize the dialog window in a "Modal way"
    setModal(true);
    pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int       w = windowWidth+10;
    int       h = windowHeight+10;
    setLocation(screenSize.width/3-w/2, screenSize.height/2-h/2);
    setSize(w, h);
    show();
  } 

  private FunctionalityDescriptor getSelectedFunc() {
  	return selectedFunc;
  } 
}
