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
import jade.util.leap.List;

/**
   @author Giovanni Caire - TILAB
 */
class TSDaemonConnectionDlg extends JDialog {

	private JButton okB, cancelB;
	private JTextField hostF;
	private JCheckBox connectCB;
	
	private TSDaemonConnectionDlg(Frame parent, TSDaemonConnectionConfiguration conf) {
		super(parent);
		setModal(true);
		setTitle("Teset Suite Daemon connection");
		setResizable(false);
		
		final TSDaemonConnectionConfiguration c = conf;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2));
		p.add(new JLabel("Connect to Test Suite Daemon"));
		connectCB = new JCheckBox();
		connectCB.setSelected(c.getConnect());
		connectCB.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			hostF.setEditable(connectCB.isSelected());    				
    		}
		} );
		p.add(connectCB);
		p.add(new JLabel("Test Suite Daemon host"));
		hostF = new JTextField();
		hostF.setEditable(c.getConnect());
		if (c.getConnect()) {
			hostF.setText(c.getHostName());
		}
		p.add(hostF);

		getContentPane().add(p, BorderLayout.CENTER);
		
		// OK and Cancel Button in the SOUTH part
		p = new JPanel();
		
		okB = new JButton("OK");
		okB.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			c.setConnect(connectCB.isSelected());
    			c.setHostName(hostF.getText());
    			c.setChanged(true);
    			TSDaemonConnectionDlg.this.dispose();
    		}
		} );
		p.add(okB);

		cancelB = new JButton("Cancel");
		cancelB.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			c.setChanged(false);
    			TSDaemonConnectionDlg.this.dispose();
    		}
		} );
		p.add(cancelB);

		// Adjust the buttons so that they have the same size
		okB.setPreferredSize(cancelB.getPreferredSize());
		
		getContentPane().add(p, BorderLayout.SOUTH);
	}
	
	public static void configure(TSDaemonConnectionConfiguration c) {
		TSDaemonConnectionDlg dlg = new TSDaemonConnectionDlg(null, c);
		dlg.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		dlg.setLocation(centerX - dlg.getWidth() / 2, centerY - dlg.getHeight() / 2);
		dlg.show();
	}
}
