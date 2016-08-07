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
import jade.util.leap.List;

import test.common.ArgumentSpec;

/**
   @author Giovanni Caire - TILAB
 */
public class InsertArgumentsDlg extends JDialog {

	private List argSpecs;
	private JButton okB;
	private JLabel[] labels;
	private JTextField[] values;
	private JTextField[] defaults;
	
	private InsertArgumentsDlg(Frame parent, List l) {
		super(parent);
		setModal(true);
		setTitle("Test group arguments");
		setResizable(false);
		
		// Text fields for inserting values in the CENTER part 
		argSpecs = l;
		final int nArgs = argSpecs.size();
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(nArgs+1, 3));
		p.add(new JLabel(""));
		p.add(new JLabel("Value"));
		p.add(new JLabel("Default"));
		
		labels = new JLabel[nArgs];
		values = new JTextField[nArgs];
		defaults = new JTextField[nArgs];
		for (int i = 0; i < nArgs; ++i) {
			ArgumentSpec a = (ArgumentSpec) argSpecs.get(i);
			labels[i] = new JLabel(a.getLabel());
			values[i] = new JTextField();
			values[i].setText(a.getValue());
			defaults[i] = new JTextField();
			defaults[i].setText(a.getDefaultValue());
			defaults[i].setEnabled(false);
			p.add(labels[i]);
			p.add(values[i]);
			p.add(defaults[i]);
		}
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		// OK Button in the SOUTH part
		p = new JPanel();
		
		okB = new JButton("OK");
		okB.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			for (int i = 0; i < nArgs; ++i) {
    				ArgumentSpec a = (ArgumentSpec) argSpecs.get(i);
    				String v = values[i].getText();
    				if (v != null && !v.equals("")) {
    					a.setValue(v);
    				}
    				else {
    					if (a.isMandatory()) {
    						return;
    					}
    					else {
    						a.setValue(a.getDefaultValue());
    					}
    				}
    			}
    			
    			//okFlag = true;
    			InsertArgumentsDlg.this.dispose();
    		}
		} );
		p.add(okB);

		getContentPane().add(p, BorderLayout.SOUTH);
	}
	
	public static void insertValues(List argSpecs) {
		InsertArgumentsDlg dlg = new InsertArgumentsDlg(null, argSpecs);
		dlg.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		dlg.setLocation(centerX - dlg.getWidth() / 2, centerY - dlg.getHeight() / 2);
		dlg.show();
	}
}
