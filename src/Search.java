import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Search extends JFrame {
	
	private Dimension fieldSize = new Dimension(200,50);
		
	private Interface target;
	private ArrayList<Marker> markerList;
	private JTextField field = new JTextField();
	
	public Search(Interface target) {
		this.target = target;
		this.markerList = target.getMarkerList();
		
		field.setBorder(BorderFactory.createTitledBorder("Search"));
		field.setPreferredSize(fieldSize);
		
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				refresh();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				refresh();
				
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				refresh();
			}
		});
		
		Box searchBar = Box.createHorizontalBox();
		searchBar.add(field);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener( new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        JFrame frame = (JFrame)e.getSource();
		        for (int i = 0; i < markerList.size(); i++)
					markerList.get(i).setVisible(true);
		        target.refresh();
		        frame.dispose();
		    }
		});
		
		this.add(searchBar);
		this.pack();
		this.setVisible(true);
	}
	public void setTargetInterface(Interface target) {
		this.target = target;
		markerList = target.getMarkerList();
	}
	public void refresh() {
		if (field.getText().isEmpty())
			for (int i = 0; i < markerList.size(); i++)
				markerList.get(i).setVisible(true);
		else {
			for (int i = 0; i < markerList.size(); i++)
				markerList.get(i).setVisible(false);
			String[] array = field.getText().split(",");
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < markerList.size(); j++) {
					if (queryMarker(markerList.get(j), array[i].toUpperCase()))
						markerList.get(j).setVisible(true);
				}
			}
		}
		target.refresh();
	}
	public boolean queryMarker(Marker src, String in) {
		return src.getType().toUpperCase().contains(in) ||
				src.getName().toUpperCase().contains(in) ||
				src.getRoom().toUpperCase().contains(in) ||
				src.getSide().toUpperCase().contains(in) ||
				src.getMap().getName().toUpperCase().contains(in) ||
				src.getMap().getFloor().toUpperCase().contains(in) ||
				src.getDescription().toUpperCase().contains(in);
				
	}
}