package exampleGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame {
	
	private JTextField myName; 
	private GUI gui;
	
	public GUI() {
		gui = this;
		setTitle("GUI");
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createLayout();
		ToFromPanel tfPanel = new ToFromPanel();
		add(tfPanel, BorderLayout.CENTER);
		PreferencePanel pPanel = new PreferencePanel();
		add(pPanel, BorderLayout.EAST);
		WillDrivePanel wdPanel = new WillDrivePanel();
		add(wdPanel, BorderLayout.NORTH);
	}
	
	private void createLayout() {
		JPanel panel = new JPanel();
		JLabel nameLabel = new JLabel("Name");
		myName = new JTextField(20);
		panel.add(nameLabel);
		panel.add(myName);
		add(panel, BorderLayout.CENTER);
		JButton nameButton = new JButton("OK");
		add(nameButton, BorderLayout.SOUTH);
		nameButton.addActionListener(new ButtonListener());
		
	}
	
	private class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			int ready = JOptionPane.showConfirmDialog(null, "Are you ready to continue?");
			if(ready == JOptionPane.YES_OPTION)
				JOptionPane.showMessageDialog(gui, "Here we go!");
			else
				JOptionPane.showMessageDialog(gui, "OK, we'll wait");
			
		}
		
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);
	}

}
