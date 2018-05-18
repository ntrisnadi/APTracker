import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Building extends JPanel{

	private Dimension panelSize = new Dimension(300,80);
	private Dimension fieldSize = new Dimension(100,40);
	private JPanel panel = new JPanel();
	private JTextArea name = new JTextArea();
	private JTextArea alias = new JTextArea();
	private Boolean selected = false; 
	
	private MouseListener clicker = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			panel.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			if (selected)
				panel.setBackground(Color.LIGHT_GRAY);
			else
				panel.setBackground(Color.WHITE);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			panel.setBackground(Color.GRAY);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			toggleSelected();
		}
		
	};
	
	public Building() {
		name.setPreferredSize(fieldSize);
		name.setBorder(BorderFactory.createTitledBorder("Name"));
		alias.setPreferredSize(fieldSize);
		alias.setBorder(BorderFactory.createTitledBorder("Alias"));
		Box layout = Box.createHorizontalBox();
		layout.add(name);
		layout.add(Box.createHorizontalStrut(10));
		layout.add(alias);
		panel.setPreferredSize(panelSize);
		panel.setBorder(BorderFactory.createTitledBorder("BUILDING"));
		panel.add(layout);
		this.addMouseListener(clicker);
		this.add(panel);
		this.setVisible(true);
	}
	public Building(String name, String alias) {
		this.name.setText(name);
		this.name.setPreferredSize(fieldSize);
		this.name.setBorder(BorderFactory.createTitledBorder("Name"));
		this.alias.setText(alias);
		this.alias.setPreferredSize(fieldSize);
		this.alias.setBorder(BorderFactory.createTitledBorder("Alias"));
		Box layout = Box.createHorizontalBox();
		layout.add(this.name);
		layout.add(Box.createHorizontalStrut(10));
		layout.add(this.alias);
		panel.setPreferredSize(panelSize);
		panel.setBorder(BorderFactory.createTitledBorder("BUILDING"));
		panel.add(layout);
		this.addMouseListener(clicker);
		this.add(panel);
		this.setVisible(true);
	}
	public String getName() {return name.getText(); }
	public String getAlias() {return alias.getText();}
	public JTextArea getNameField() {return name;}
	public JTextArea getAliasField() {return alias;}
	public Boolean isSelected() {return selected;}
	
	public void setName(String name) { this.name.setText(name);}
	public void setAlias(String alias) { this.alias.setText(alias);}
	public Boolean toggleSelected () {
		setSelected(!selected);
		return selected;
	}
	public void setSelected(boolean inSelect) {
		selected = inSelect;
		if (inSelect) {
			panel.setBackground(Color.GRAY);
		}
		else {
			panel.setBackground(Color.WHITE);
		}
	}
}
