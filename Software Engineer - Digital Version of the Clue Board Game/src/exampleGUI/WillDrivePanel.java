package exampleGUI;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WillDrivePanel extends JPanel {
	private JTextField myName;
	private JCheckBox willDriveCB;
	
	public WillDrivePanel() {
		JLabel label = new JLabel("Name");
		myName = new JTextField(20);
		myName.setFont(new Font("SansSerif", Font.BOLD, 12));
		add(label);
		add(myName);
		willDriveCB = new JCheckBox("Will drive");
		add(willDriveCB);
		
	}
	
	
}
