package exampleGUI;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ToFromPanel extends JPanel {
	private JComboBox<String> toCity, fromCity;
	public ToFromPanel() {
		
		JLabel fromLabel = new JLabel("From");
		JLabel toLabel = new JLabel("To");
		setLayout(new GridLayout(2, 2));
		add(fromLabel);
		add(toLabel);
		
		toCity = createCityCombo();
		add(toCity);
		fromCity = createCityCombo();
		add(fromCity);
		setBorder(new TitledBorder (new EtchedBorder(), "Location"));
		
		
	}
	
	public JComboBox<String> createCityCombo() {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("Golden");
		combo.addItem("Boulder");
		combo.addItem("Denver");
		return combo;
	}

}
