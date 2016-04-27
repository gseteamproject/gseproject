package examples.bookTrading;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookSellerGUI extends JFrame
{
	private static final long serialVersionUID = 6961985506989858777L;
	
	private BookSellerAgent myAgent;
	
	private JTextField titleField, priceField;
	
	public BookSellerGUI(BookSellerAgent p_agent)
	{
		super(p_agent.getLocalName());
		myAgent = p_agent;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Book title:"));
		titleField = new JTextField(15);
		p.add(titleField);
		p.add(new JLabel("Price:"));
		priceField = new JTextField(15);
		p.add(priceField);
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					try
					{
						String title = titleField.getText().trim();
						String price = priceField.getText().trim();
						myAgent.registerBook(title, Integer.parseInt(price));
						titleField.setText("");
						priceField.setText("");
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(BookSellerGUI.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
					}
				}
			});
		p = new JPanel();
		p.add(addButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		addWindowListener(
			new	WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					myAgent.doDelete();
				}
			});
		
		setResizable(false);
	}
	
	public void showGUI()
	{
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
}
