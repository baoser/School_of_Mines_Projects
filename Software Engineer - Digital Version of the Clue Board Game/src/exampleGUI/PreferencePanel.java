package exampleGUI;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PreferencePanel extends JPanel {
	 private JRadioButton musicButton, noMusicButton;
	 
	 public PreferencePanel() {
		 musicButton = new JRadioButton("Music");
		 noMusicButton = new JRadioButton("No Music");
		 
		 noMusicButton.setSelected(true);
		 
		 add(musicButton);
		 add(noMusicButton);
		 
		 ButtonGroup group = new ButtonGroup();
		 group.add(musicButton);
		 group.add(noMusicButton);
		 setBorder(new TitledBorder (new EtchedBorder(), "Preferences"));
		 setLayout(new GridLayout(2, 1));
		 
	 }
	
	
}
