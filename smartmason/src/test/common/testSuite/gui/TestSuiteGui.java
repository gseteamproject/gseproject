/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

package test.common.testSuite.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;
import java.io.*;
import jade.gui.GuiEvent;
import jade.gui.JadeLogoButton;

import test.common.testSuite.TestSuiteAgent;
import test.common.testerAgentControlOntology.TestResult;
import test.common.TestUtility;
import test.common.TestException;
import test.common.remote.TSDaemon;
import test.common.remote.RemoteManager;
import test.common.xml.*;
import test.common.Logger;


/**
 * @author Alessandro Negri - AOTLab UniPR
 * @author Matteo Bisi - UniPR
 * @author Yuri Ferrari - UniPR
 * @author Rossano Vitulli - UniPR
 * @version $Date: 2004-03-12 12:51:16 +0100 (ven, 12 mar 2004) $ $Revision: 341 $
 */
public class TestSuiteGui extends javax.swing.JFrame {

  // Gui states used to enable/disable buttons
  public static final int                 IDLE_STATE = 0;
  public static final int                 READY_STATE = 1;
  public static final int                 RUNNING_STATE = 2;
  public static final int                 DEBUGGING_STATE = 3;
  public static final int                 STEPPING_STATE = 4;
  public static final int                 CONFIGURING_STATE = 5;
  public static final int                 RUNALL_STATE = 6;
  
  private static final int                HISTORY_MAX_LENGTH = 4;
  
  private int                             status;

  private TestSuiteAgent myAgent;
  private String xmlFileName;
  private TSDaemonConnectionConfiguration daemonConf = new TSDaemonConnectionConfiguration();

  private int totalRuns = 0;
  private int totalPassed = 0;
  private int totalFailed = 0;

  // Graphic components
  private javax.swing.JToolBar                      JToolBar;
  private javax.swing.JButton                       configB;
  private javax.swing.JButton                       connectB;
  private test.common.testSuite.gui.CounterPanel    counterPanel;
  private javax.swing.JButton                       debugB;
  private javax.swing.JButton                       exitB;
  private test.common.testSuite.gui.FailedTestsView failedTestsView;
  private javax.swing.JComboBox                     historyCB;
  private javax.swing.JLabel                        jLabel1;
  private javax.swing.JLabel                        jLabel2;
  private javax.swing.JSeparator                    jSeparator;
  private javax.swing.JSplitPane                    jSplitPane;
  private javax.swing.JLabel                        jStatusLabel;
  private javax.swing.JButton                       loggerB;
  private javax.swing.JButton                       openB;
  private test.common.testSuite.gui.ProgressBar     progressBar;
  private javax.swing.JPanel                        rootPanel;
  private javax.swing.JButton                       runAllB;
  private javax.swing.JButton                       runB;
  private javax.swing.JButton                       selectB;
  private javax.swing.JButton                       stepB;
  private test.common.xml.TestsTree                 testsTree;
  private javax.swing.JScrollPane                   testsTreeView;

  /**
   * Default contructor. Creates new form TestSuiteGui.
   * @param myAgent The <code>TestSuiteAgent</code> who handle the GUI.
   * @param xmlFileName The name of XML file with complete list of test availables.
   */
  public TestSuiteGui(TestSuiteAgent myAgent, String xmlFileName) {
    this.myAgent = myAgent;
    this.xmlFileName = xmlFileName;
    initComponents();

    jStatusLabel.setText("Current logger type: "+test.common.Logger.getLoggerStringType()+", click on Logger button to change it");
  }

  /**
   */
  private void initComponents() {    // GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    rootPanel = new javax.swing.JPanel();
    JToolBar = new javax.swing.JToolBar();
    exitB = new javax.swing.JButton();
    connectB = new javax.swing.JButton();
    openB = new javax.swing.JButton();
    configB = new javax.swing.JButton();
    selectB = new javax.swing.JButton();
    runB = new javax.swing.JButton();
    runAllB = new javax.swing.JButton();
    debugB = new javax.swing.JButton();
    stepB = new javax.swing.JButton();
    loggerB = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    historyCB = new javax.swing.JComboBox();
    try {
      loadHistory();
    } 
    catch (IOException e) {
      // fails to load history file
    } 
    jLabel2 = new javax.swing.JLabel();
    jSeparator = new javax.swing.JSeparator();
    progressBar = new test.common.testSuite.gui.ProgressBar();
    jSplitPane = new javax.swing.JSplitPane();
    failedTestsView = new test.common.testSuite.gui.FailedTestsView();
    testsTree = new test.common.xml.TestsTree();
    testsTree.setModel(null);
    testsTree.setCellRenderer(new TestResultCellRenderer(this));
    testsTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if (path != null) {
		      failedTestsView.clear();
        	TestResult tr = getTestResult(path);
		      if (tr != null) {
		      	// The selected element is a test and a result for this test is 
		      	// available --> if there is an error message, shows it in the failedTestsView
		      	if (tr.getErrorMsg() != null) {
			      	failedTestsView.print(tr.getErrorMsg());
		      	}
		      }
        } 
      } 
    });
    testsTreeView = new JScrollPane(testsTree);
    jStatusLabel = new javax.swing.JLabel();
    counterPanel = new test.common.testSuite.gui.CounterPanel();

    setTitle("JADE Test Suite");
    setMaximizedBounds(new java.awt.Rectangle(0, 0, 589, 516));
    setName("rootFrame");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        exit();
      } 
    });

    rootPanel.setLayout(new java.awt.GridBagLayout());

    rootPanel.setMinimumSize(new java.awt.Dimension(484, 190));
    JToolBar.setFloatable(false);
    exitB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/exit.png")));
    exitB.setMnemonic('X');
    exitB.setText("Exit");
    exitB.setToolTipText("Exit the JADE Test Suite");
    exitB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    exitB.setIconTextGap(0);
    exitB.setMaximumSize(new java.awt.Dimension(64, 64));
    exitB.setMinimumSize(new java.awt.Dimension(64, 64));
    exitB.setPreferredSize(new java.awt.Dimension(64, 64));
    exitB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    exitB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exitBActionPerformed(evt);
      } 
    });

    JToolBar.add(exitB);

    connectB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/connect_to_network.png")));
    connectB.setMnemonic('C');
    connectB.setText("Connect");
    connectB.setToolTipText("Use the Test Suite Daemon to launch other JADE instances remotely");
    connectB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    connectB.setIconTextGap(0);
    connectB.setMaximumSize(new java.awt.Dimension(64, 64));
    connectB.setMinimumSize(new java.awt.Dimension(64, 64));
    connectB.setPreferredSize(new java.awt.Dimension(64, 64));
    connectB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    connectB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectBActionPerformed(evt);
      } 
    });

    JToolBar.add(connectB);

    openB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/fileopen.png")));
    openB.setMnemonic('O');
    openB.setText("Open");
    openB.setToolTipText("Load a functionality test group");
    openB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    openB.setIconTextGap(0);
    openB.setMaximumSize(new java.awt.Dimension(64, 64));
    openB.setMinimumSize(new java.awt.Dimension(64, 64));
    openB.setPreferredSize(new java.awt.Dimension(64, 64));
    openB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    openB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openBActionPerformed(evt);
      } 
    });

    JToolBar.add(openB);

    configB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/SuSEconf.png")));
    configB.setMnemonic('G');
    configB.setText("Config");
    configB.setToolTipText("Set arguments for the current test group");
    configB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    configB.setIconTextGap(0);
    configB.setMaximumSize(new java.awt.Dimension(64, 64));
    configB.setMinimumSize(new java.awt.Dimension(64, 64));
    configB.setPreferredSize(new java.awt.Dimension(64, 64));
    configB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    configB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        configBActionPerformed(evt);
      } 
    });

    JToolBar.add(configB);

    selectB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/button_ok.png")));
    selectB.setText("Select");
    selectB.setToolTipText("Select the tests to execute within the loaded group");
    selectB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    selectB.setIconTextGap(0);
    selectB.setMaximumSize(new java.awt.Dimension(64, 64));
    selectB.setMinimumSize(new java.awt.Dimension(64, 64));
    selectB.setPreferredSize(new java.awt.Dimension(64, 64));
    selectB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    selectB.setEnabled(false);
    selectB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectBActionPerformed(evt);
      } 
    });

    JToolBar.add(selectB);

    runB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/misc.png")));
    runB.setMnemonic('R');
    runB.setText("Run");
    runB.setToolTipText("Run the current functionality test group");
    runB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    runB.setIconTextGap(0);
    runB.setMaximumSize(new java.awt.Dimension(64, 64));
    runB.setMinimumSize(new java.awt.Dimension(64, 64));
    runB.setPreferredSize(new java.awt.Dimension(64, 64));
    runB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    runB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runBActionPerformed(evt);
      } 
    });

    JToolBar.add(runB);

    runAllB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/kbackgammon_engine.png")));
    runAllB.setMnemonic('A');
    runAllB.setText("Run All");
    runAllB.setToolTipText("Run all tests");
    runAllB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    runAllB.setIconTextGap(0);
    runAllB.setMaximumSize(new java.awt.Dimension(64, 64));
    runAllB.setMinimumSize(new java.awt.Dimension(64, 64));
    runAllB.setPreferredSize(new java.awt.Dimension(64, 64));
    runAllB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    runAllB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runAllBActionPerformed(evt);
      } 
    });

    JToolBar.add(runAllB);

    debugB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/search.png")));
    debugB.setMnemonic('D');
    debugB.setText("Debug");
    debugB.setToolTipText("Debug the current functionality test group");
    debugB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    debugB.setIconTextGap(0);
    debugB.setMaximumSize(new java.awt.Dimension(64, 64));
    debugB.setMinimumSize(new java.awt.Dimension(64, 64));
    debugB.setPreferredSize(new java.awt.Dimension(64, 64));
    debugB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    debugB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        debugBActionPerformed(evt);
      } 
    });

    JToolBar.add(debugB);

    stepB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/forward.png")));
    stepB.setMnemonic('N');
    stepB.setText("Next");
    stepB.setToolTipText("Execute next test");
    stepB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    stepB.setIconTextGap(0);
    stepB.setMaximumSize(new java.awt.Dimension(64, 64));
    stepB.setMinimumSize(new java.awt.Dimension(64, 64));
    stepB.setPreferredSize(new java.awt.Dimension(64, 64));
    stepB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    stepB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stepBActionPerformed(evt);
      } 
    });

    JToolBar.add(stepB);

    loggerB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/kexi.png")));
    loggerB.setText("Log");
    loggerB.setToolTipText("Select the log type");
    loggerB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    loggerB.setIconTextGap(0);
    loggerB.setMaximumSize(new java.awt.Dimension(64, 64));
    loggerB.setMinimumSize(new java.awt.Dimension(64, 64));
    loggerB.setPreferredSize(new java.awt.Dimension(64, 64));
    loggerB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    loggerB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        loggerBActionPerformed(evt);
      } 
    });

    JToolBar.add(loggerB);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 4);
    rootPanel.add(JToolBar, gridBagConstraints);

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel1.setText("Current functionality: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(21, 4, 21, 4);
    rootPanel.add(jLabel1, gridBagConstraints);

    historyCB.setMaximumRowCount(60);
    historyCB.setMinimumSize(new java.awt.Dimension(200, 25));
    historyCB.setPreferredSize(new java.awt.Dimension(200, 25));
    historyCB.setLightWeightPopupEnabled(false);
    historyCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        historyActionPerformed(evt);
      } 
    });
    historyCB.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        historyKeyTyped(evt);
      } 
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(17, 2, 16, 4);
    rootPanel.add(historyCB, gridBagConstraints);

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Progress: ");
    jLabel2.setMaximumSize(null);
    jLabel2.setMinimumSize(null);
    jLabel2.setPreferredSize(null);
    jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    rootPanel.add(jLabel2, new java.awt.GridBagConstraints());

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
    rootPanel.add(jSeparator, gridBagConstraints);

    progressBar.setLayout(new java.awt.GridLayout(1, 1));

    progressBar.setMinimumSize(new java.awt.Dimension(200, 20));
    progressBar.setPreferredSize(new java.awt.Dimension(200, 25));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 4);
    rootPanel.add(progressBar, gridBagConstraints);

    jSplitPane.setDividerLocation(200);
    jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    failedTestsView.setLayout(new java.awt.GridLayout(1, 1));

    failedTestsView.setPreferredSize(new java.awt.Dimension(300, 150));
    jSplitPane.setRightComponent(failedTestsView);

    testsTreeView.setPreferredSize(new java.awt.Dimension(400, 200));
    jSplitPane.setLeftComponent(testsTreeView);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 100.0;
    gridBagConstraints.weighty = 100.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    rootPanel.add(jSplitPane, gridBagConstraints);

    jStatusLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    jStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/idle_buttons.gif")));
    jStatusLabel.setIconTextGap(10);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 0);
    rootPanel.add(jStatusLabel, gridBagConstraints);

    // Initially status must be in IDLE state
    setStatus(IDLE_STATE);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
    rootPanel.add(counterPanel, gridBagConstraints);

    getContentPane().add(rootPanel, java.awt.BorderLayout.CENTER);
		
		// add the button with the JADE Logo to the toolbar
    JToolBar.addSeparator(); 
    JadeLogoButton logo = new JadeLogoButton();	
    logo.setText("Web");
    logo.setToolTipText("Browse the JADE web site");
    logo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    //logo.setIconTextGap(0);
    logo.setMaximumSize(new java.awt.Dimension(64, 64));
    logo.setMinimumSize(new java.awt.Dimension(64, 64));
    logo.setPreferredSize(new java.awt.Dimension(64, 64));
    logo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    JToolBar.add(logo);

		// set the icon for this frame
		setIconImage(getToolkit().getImage(logo.getClass().getResource("/jade/gui/images/logosmall.jpg")));

    pack();
  }                                                                        // GEN-END:initComponents
 
  
  private void openBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_openBActionPerformed
    open();
  }
  
  private void configBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_configBActionPerformed
    config();
  }                                                                         // GEN-LAST:event_configBActionPerformed
 
  private void selectBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_selectBActionPerformed
    select();
  }                                                                        // GEN-LAST:event_selectBActionPerformed
  
  private void runBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_runBActionPerformed
    run();
  }                                                                        // GEN-LAST:event_runBActionPerformed
 
  private void stepBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_stepBActionPerformed
    step();
  }                                                                       // GEN-LAST:event_stepBActionPerformed
 
  private void debugBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_debugBActionPerformed
    debug();
  }                                                                        // GEN-LAST:event_debugBActionPerformed
 
  private void historyActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_jComboBoxActionPerformed
  	// Note that in the RUNALL state each newly loaded functionality
  	// is added to the historyCB to update the GUI. This fires an
  	// action event that must be ignored 
  	if (status != RUNALL_STATE) {
	    // User changed test selection from history list
	    //addToHistory(historyCB.getSelectedItem());
	    resetAll(true);
	
	    // Post a LOAD event 
	    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.LOAD_EVENT);
	    ev.addParameter(historyCB.getSelectedItem());
	    myAgent.postGuiEvent(ev);
  	}
  }                                                                // GEN-LAST:event_jComboBoxActionPerformed
 
  private void historyKeyTyped(java.awt.event.KeyEvent evt) {    // GEN-FIRST:event_jComboBoxKeyTyped
    // User pressed ENTER on historyCB to start loaded Test Group
  	// Note that this may happen also in the IDLE state --> do nothing in this case
  	if (status == READY_STATE) {
	    if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
	      run();
	    }
  	}
  }                                                                      // GEN-LAST:event_jComboBoxKeyTyped
 
  private void runAllBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_runAllBActionPerformed
    runAll();
  }                                                                     // GEN-LAST:event_runAllBActionPerformed
 
  private void connectBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_connectBActionPerformed
    connect();
  }                                                                      // GEN-LAST:event_connectBActionPerformed
 
  private void exitBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_exitBActionPerformed
    showExitForm();
  }                                                                      // GEN-LAST:event_exitBActionPerformed
 
  private void loggerBActionPerformed(java.awt.event.ActionEvent evt) {    // GEN-FIRST:event_loggerBActionPerformed
    LoggerDialog lDialog = new LoggerDialog(this, Logger.getLogger().getLoggerType());
    lDialog.setLocationRelativeTo(this);
    lDialog.show();
    if (lDialog.getExitValue() == LoggerDialog.OK) {
      // The user clicked "Ok"
      int selectedLoggerType = lDialog.getSelectedLoggerType();
      if (Logger.getLogger().getLoggerType() != selectedLoggerType) {
        Logger.getLogger().deleteLogger();
        switch (selectedLoggerType) {
        case Logger.TXT_LOGGER:
          Logger.getLogger().setFileLogger(Logger.TXT_LOGGER);
          break;
        case Logger.CONSOLE_LOGGER:
          Logger.getLogger().setTextLogger();
          break;
        case Logger.HTML_LOGGER:
          Logger.getLogger().setFileLogger(Logger.HTML_LOGGER);
          break;
        }
      } 
      jStatusLabel.setText("Logger type is now set to: "+Logger.getLogger().getLoggerStringType());
    }
  }                                                                          // GEN-LAST:event_loggerBActionPerformed
 
 
 
  /*
   * Method called by the ActionPerformed on Exit button. Post a new <code>GuiEvent</code> with EXIT_EVENT
   * @see jade.gui.GuiEvent
   */
  void exit() {
    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.EXIT_EVENT);
    myAgent.postGuiEvent(ev);
    try {
      saveHistory();
    } 
    catch (IOException e) {
      Logger.getLogger().log(("Couldn't save test run history"));
    } 
  } 

  /*
   * Method called by the ActionPerformed on connect button.
   */
  void connect() {
    TSDaemonConnectionDlg.configure(daemonConf);
    if (daemonConf.getChanged()) {
      if (daemonConf.getConnect()) {
        try {
          RemoteManager rm = TestUtility.createRemoteManager(daemonConf.getHostName(), TSDaemon.DEFAULT_PORT, TSDaemon.DEFAULT_NAME);
          TestUtility.setDefaultRemoteManager(rm);
        } 
        catch (TestException te) {
          Logger.getLogger().log("Error connecting to the Test Suite Daemon. "+te.getMessage());
        } 
      } 
      else {
        TestUtility.setDefaultRemoteManager(null);
      } 
    } 
  } 

  /*
   * Method called by the ActionPerformed on Open button. Show the selection dialog with the list of tests availables.
   */
  void open() {
    FunctionalityDescriptor func = FunctionalitySelectionDlg.showSelectionDlg(this, xmlFileName);
    // Note that the LOAD_EVENT will be posted to the TestSuiteAGent by
    // the ActionListener of the historyCB.
    if (func != null) {
      addToHistory(func);
    } 
  } 

  // Method called by the ActionPerformed on Config button
  void config() {
    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.CONFIGURE_EVENT);
    setStatus(CONFIGURING_STATE);
    myAgent.postGuiEvent(ev);
  } 

  /*
   * Method called by the ActionPerformed on Run button.
   * If we are in the READY state --> the event to be posted to the
   * agent is RUN. Otherwise (the tester was already executing his
   * test group in debug mode) it is GO.
   * @see jade.gui.GuiEvent
   */
  void run() {
    GuiEvent ev = null;
    // If we are in the READY state --> the event to be posted to the
    // agent is RUN. Otherwise (the tester was already executing his
    // test group in debug mode) it is GO
    if (status == READY_STATE) {
      totalRuns = 0;
      totalPassed = 0;
      totalFailed = 0;
      // Reset the counter panel and the progress bar, if in run mode.
      resetAll(true);
      jStatusLabel.setText("Running test group...");
	    ev = new GuiEvent(this, TestSuiteAgent.RUN_EVENT);
    } 
    else if (status == DEBUGGING_STATE) {
      jStatusLabel.setText("Running remaining tests of the group...");
	    ev = new GuiEvent(this, TestSuiteAgent.GO_EVENT);
    } 
    jStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/progress_buttons.gif")));

    setStatus(RUNNING_STATE);
    myAgent.postGuiEvent(ev);
  } 

  /*
   * Method called by the ActionPerformed on RunAll button.
   * If we are in the READY state --> the event to be posted to the
   * agent is RUNALL. Otherwise (the tester was already executing his
   * test group in debug mode) it is GO.
   * @see jade.gui.GuiEvent
   */
  void runAll() {
    jStatusLabel.setText("Running all tests...");
    jStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/progress_buttons.gif")));
    counterPanel.reset(false, true);

    totalRuns = 0;
    totalPassed = 0;
    totalFailed = 0;

    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.RUNALL_EVENT);

    // NOTE that we don't set the GUI status to RUNNING here otherwise the
    // TestSuiteAgent cannot know whether a tester is currently loaded or not.
    myAgent.postGuiEvent(ev);
  } 

  /*
   * Method called by the ActionPerformed on Debug button. Post a new <code>GuiEvent</code> with DEBUG_EVENT
   * @see jade.gui.GuiEvent
   */
  void debug() {
    resetAll(true);
    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.DEBUG_EVENT);
    setStatus(DEBUGGING_STATE);
    myAgent.postGuiEvent(ev);
  } 

  /*
   * Method called by the ActionPerformed on Step button. Post a new <code>GuiEvent</code> with STEP_EVENT
   * @see jade.gui.GuiEvent
   */
  void step() {
    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.STEP_EVENT);
    setStatus(STEPPING_STATE);
    myAgent.postGuiEvent(ev);
  } 

  // Method called by the SelectAction
  void select() {
    GuiEvent ev = new GuiEvent(this, TestSuiteAgent.SELECT_EVENT);
    setStatus(CONFIGURING_STATE);
    myAgent.postGuiEvent(ev);
  } 

  public TestResult getTestResult(TreePath path) {
		String name = path.getLastPathComponent().toString();
		if (name.endsWith(" SKIPPED")) {
			name = name.substring(0, name.length() - 8);
		}
		TestResult tr = null;
    //System.out.println("Name is "+name+" path is "+path);
    if (path.getPathComponent(0).toString().equals(TestsTree.DEFAULT_TREE_ROOT_NAME)) {
    	// The tree that is being shown is the global tree at the end of a RUN_ALL procedure
    	if (path.getPathCount() >= 3) {
	  		String funcName = path.getPathComponent(2).toString();
	      tr = myAgent.getTestResult(name, funcName);
    	}
    }
    else {
    	// The tree that is being shown is a single functionality tree
  		tr = myAgent.getTestResult(name, null);
    }
    return tr;
  }
  
  /**
   * Utility method to show the GUI in the center of the screen.
   */
  public void showCorrect() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int       centerX = (int) screenSize.getWidth()/2;
    int       centerY = (int) screenSize.getHeight()/2;
    setLocation(centerX-getWidth()/2, centerY-getHeight()/2);
    show();
  } 

  /**
   * Set current GUI execution state.
   * @param status Used to store internal GUI state
   */
  public void setStatus(int status) {
    this.status = status;
    updateEnabled();
  } 

  /**
   * Get current GUI execution state.
   * @return the internal status of the GUI
   */
  public int getStatus() {
    return this.status;
  } 

  private void updateEnabled() {
    historyCB.setEnabled(status == IDLE_STATE || status == READY_STATE);
    openB.setEnabled(status == IDLE_STATE || status == READY_STATE);
    loggerB.setEnabled(status == IDLE_STATE || status == READY_STATE);
    runB.setEnabled(status == READY_STATE || status == DEBUGGING_STATE);
    runAllB.setEnabled(status == IDLE_STATE || status == READY_STATE);
    debugB.setEnabled(status == READY_STATE);
    stepB.setEnabled(status == DEBUGGING_STATE);
    configB.setEnabled(status == READY_STATE);
    selectB.setEnabled(status == READY_STATE);
  } 

  /**
     Put/move a given FunctionalityDescriptor at the beginning of the history
   */
  public void addToHistory(Object element) {
    for (int i = 0; i < historyCB.getItemCount(); i++) {
      if (element.equals(historyCB.getItemAt(i))) {
        if (i != 0) {
          historyCB.removeItemAt(i);
        } 
        else {
        	// The element is already in the history and is already the first one
        	// Do nothing
        	return;
        }
      } 
    } 
  	System.out.println("El "+element +" inserted");
    historyCB.insertItemAt(element, 0);
    historyCB.setSelectedIndex(0);
    resizeHistory();
  } 

  private void resizeHistory() {
    if (historyCB.getItemCount() > HISTORY_MAX_LENGTH) {
      historyCB.removeItemAt(HISTORY_MAX_LENGTH);
    } 
  } 

  private void loadHistory() throws IOException {
    /*BufferedReader br = new BufferedReader(new FileReader(getSettingsFile()));
    int            itemCount = 0;
    try {
      String line;
      while ((line = br.readLine()) != null) {
        if (TestManager.getTestGroupAlias().indexOf(line) != -1) {
          historyCB.addItem(line);
          itemCount++;
        } 
      } 
      if (itemCount > 0) {
        historyCB.setSelectedIndex(0);
        // If there is an Item in history file, selected tester agent must be loaded
        Logger.getLogger().log("Functionality selected: "+historyCB.getItemAt(0).toString());
        GuiEvent ev = new GuiEvent(this, TestSuiteAgent.LOAD_EVENT);
        ev.addParameter(historyCB.getItemAt(0).toString());
        myAgent.postGuiEvent(ev);
      } 
    } 
    finally {
      br.close();
    }*/ 
  } 

  private void saveHistory() throws IOException {
    /*BufferedWriter bw = new BufferedWriter(new FileWriter(getSettingsFile()));
    try {
      for (int i = 0; i < historyCB.getItemCount(); i++) {
        String testcase = historyCB.getItemAt(i).toString();
        bw.write(testcase, 0, testcase.length());
        bw.newLine();
      } 
    } 
    finally {
      bw.close();
    }*/ 
  } 

  private File getSettingsFile() {
    String home = System.getProperty("user.home");
    return new File(home, ".testsession");
  } 

  /**
   * Update the elapsed time at the end of a Run or RunAll execution
   */
  public void setElapsedTime(final long value) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        counterPanel.setElapsedTime(value, getStatus() == RUNALL_STATE);
      } 
    });
  } 

  /**
   * Start the progress bar progression
   */
  public void startProgressBar(final int length) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
      	progressBar.start(length);
      } 
    });
  } 

  /**
   * Let the progress bar evolve its status by one step
   */
  public void updateProgressBar(final boolean successful) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
      	progressBar.step(successful);
      } 
    });
  } 

  /**
   * Reset all values.
   * @param bTotal If true also the total number of test loaded is resetted.
   * @see test.common.testSuite.gui.CounterPanel#reset(boolean,boolean)
   * @see test.common.testSuite.gui.ProgressBar#reset()
   */
  public void resetAll(final boolean bTotal) {
    totalRuns = 0;
    totalPassed = 0;
    totalFailed = 0;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
      	boolean b = (getStatus() == RUNALL_STATE);
        counterPanel.reset(bTotal, b);
        if (!b) {
          progressBar.reset();
        } 
      } 
    });
  } 

  /**
   * Update the number of total tests to be executed in the counter pane
   */
  public void setTotal(final int value) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        counterPanel.setFTotalRuns(value);
        if (getStatus() != RUNALL_STATE) {
          startProgressBar(value);
        } 
      } 
    });
  } 
  
  public void showPassed(final String testName) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        counterPanel.setPassedCount(++totalPassed);
        counterPanel.setRunsCount(++totalRuns);
		    testsTree.fireTestChanged(testName, true);
		    if (status != TestSuiteGui.RUNALL_STATE) {
		    	updateProgressBar(true);
		    }
      }
    } );
  }
      	
  public void showFailed(final String testName) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        counterPanel.setFailedCount(++totalFailed);
        counterPanel.setRunsCount(++totalRuns);
		    testsTree.fireTestChanged(testName, true);
		    if (status != TestSuiteGui.RUNALL_STATE) {
		    	updateProgressBar(false);
		    }
      }
    } );
  }
      	
  public void showSkipped(final String testName) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
    		testsTree.fireTestChanged(testName, true);
		    if (status != TestSuiteGui.RUNALL_STATE) {
		    	updateProgressBar(true);
		    }
      }
    } );
  }
     	
  private void showExitForm() {
    ExitDialog dialog = new ExitDialog(this);
    dialog.setLocationRelativeTo(this);
    dialog.show();
    switch (dialog.getExitValue()) {
    case ExitDialog.CANCEL:
	    // The user clicked "Cancel". Just do nothing
      break;
    case ExitDialog.EXIT_TEST_SUITE:
	    // The user clicked "Exit test suite"
      exit();
      break;
    case ExitDialog.SHUT_DOWN:
	    // The user clicked "Shutdown". Not yet implemented
      break;
    }
  } 

  /**
   * Return the first item in the history combo box
   *
  public FunctionalityDescriptor getFirstTesterInHistory() {
    if (historyCB.getItemAt(0) != null) {
      return (FunctionalityDescriptor) historyCB.getItemAt(0);
    } 
    else {
    	return null;
    }
  }*/

  /**
   * Return the actual tests tree
   */
  public TestsTree getTestsTree() {
    return testsTree;
  } 

  /**
   * Print a message on the failed tests board
   */
  public void printFailedTestsMessage(String s) {
    failedTestsView.print(s);
  } 

  /**
   * Clear the failed tests board
   */
  public void clearFailedTestsMessage() {
    failedTestsView.clear();
  } 

  /**
   * Return the actual Status Label's instance
   */
  public JLabel getStatusLabel() {
    return jStatusLabel;
  } 

  /**
   * Return the actual Debug button's instance
   */
  public JButton getDebugButton() {
    return debugB;
  } 

  /**
   * Return the actual Step button's instance
   */
  public JButton getStepButton() {
    return stepB;
  } 

  /**
   * Return the actual Run button's instance
   */
  public JButton getRunButton() {
    return runB;
  } 

  /**
   * Return the actual Select button's instance
   */
  public JButton getSelectButton() {
    return selectB;
  } 

}
