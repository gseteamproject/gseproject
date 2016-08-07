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

package test.common.testSuite.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.util.leap.List;
import jade.core.Agent;
import test.common.xml.TestDescriptor;

/**
   @author Giovanni Caire - TILAB
 */
public class SelectTestsDlg extends JDialog {
	private JCheckBox[] names;
	private JButton okB, cancelB;
 	
        private SelectTestsDlg(Frame parent, final TestDescriptor[] tests) {
		
		setModal(true);
		setTitle("Tests");
		setResizable(false);
		
		// Text fields for inserting values in the CENTER part 
		final int nTests = tests.length;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(nTests, 1));
		
		names = new JCheckBox[nTests];
		for (int i = 0; i < nTests; ++i) {
			names[i] = new JCheckBox(tests[i].getName(), !(tests[i].getSkip()));
			p.add(names[i]);
		}
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		// OK and Cancel Buttons in the SOUTH part
		p = new JPanel();
		
		okB = new JButton("OK");
		okB.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
                        int runCnt=0;
    			for (int i = 0; i < tests.length; ++i) {
    				if (names[i].isSelected()) {
    					tests[i].setSkip(false);
                                        runCnt++;
    				}
    				else {
    					tests[i].setSkip(true);
    				}
    			}
    			SelectTestsDlg.this.dispose();
    		}
		} );
		p.add(okB);

		cancelB = new JButton("Cancel");
		cancelB.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			SelectTestsDlg.this.dispose();
    		}
		} );
		p.add(cancelB);

		// Adjust the buttons so that they have the same size
		okB.setPreferredSize(cancelB.getPreferredSize());
		
		getContentPane().add(p, BorderLayout.SOUTH);
	}
	
        /** Draw the select dialog box */
	public static void selectTests(TestDescriptor[] tests) {
                SelectTestsDlg dlg = new SelectTestsDlg(null, tests);
		dlg.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		dlg.setLocation(centerX - dlg.getWidth() / 2, centerY - dlg.getHeight() / 2);
		dlg.show();
	}
}
