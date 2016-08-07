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

import test.common.Test;
import test.common.testerAgentControlOntology.TestResult;
import test.common.xml.TestsTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// For creating a TreeModel
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
   This cell renderer is used to show the proper icons for 
   tests passed, failed and skipped.
   @author Giovanni Caire - TILAB
   @author Alessandro Negri - AOTLab UniPR
   @author Matteo Bisi - UniPR
   @author Yuri Ferrari - UniPR
   @author Rossano Vitulli - UniPR
 */
public class TestResultCellRenderer extends DefaultTreeCellRenderer {
	private TestIconManager theIconManager = TestIconManager.theIconManager;

	private static class TestIconManager {
		static TestIconManager theIconManager = new TestIconManager();
		
		private Icon errorIcon = new ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/failed.png"));
		private Icon skippedIcon = new ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/skipped.png"));
		private Icon successIcon = new ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/success.png"));
		private Icon defaultIcon = new ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/default.png"));
	}

	private TestSuiteGui myGui;
  
  TestResultCellRenderer(TestSuiteGui gui) {
    super();
    myGui = gui;
  }

  /**
     Return the Component that shows a given element in the tree
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

    Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);      
    String name = value.toString();
    String tip = null;

    if (leaf) {
      setIcon(theIconManager.defaultIcon);
    }
		if (name.endsWith("SKIPPED")) {
			setIcon(theIconManager.skippedIcon);
		}
    
    TreePath path = tree.getPathForRow(row);
    if (path != null) {
    	TestResult tr = myGui.getTestResult(path);      
      if (tr != null) {
      	// This is a test for which a TestResult is available
    		switch (tr.getResult()) {
    		case Test.TEST_PASSED:
          setIcon(theIconManager.successIcon);
          tip = " - Passed";
    			break;
    		case Test.TEST_FAILED:
          setIcon(theIconManager.errorIcon);
          tip = " - Failed";
    			break;
    		case Test.TEST_SKIPPED:
          setIcon(theIconManager.skippedIcon);
          tip = " - Skipped";
    			break;
    		}
      }	      	
    }
          
    if (c instanceof JComponent && tip != null) {
      ((JComponent) c).setToolTipText(tip);
    } 
    return c;
  } 
}
