import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MarkerList extends JPanel {
	private ArrayList<Marker> markerArray;
	private ArrayList<Map> mapArray;
	private JScrollPane pane = new JScrollPane();
	private Box controls = Box.createHorizontalBox();
	private Dimension size = new Dimension();
	
	
	public MarkerList(ArrayList<Marker> markerArray, ArrayList<Map> mapArray) {
		this.mapArray = mapArray;
		this.markerArray = markerArray;

		Box frame = Box.createVerticalBox();
		pane.setPreferredSize(size);
		frame.add(pane);
		frame.add(controls);
		this.add(frame);
		frame.setBorder(BorderFactory.createTitledBorder("Marker(s)"));
		
		this.setVisible(true);
	}
	public JScrollPane getPane() { return pane;}
	public ArrayList<Marker> getMarkerArray() {return markerArray;}
	public void refresh() {
		int position = pane.getVerticalScrollBar().getValue();
		for (int i = 0; i < markerArray.size(); i++) {
			markerArray.get(i).getEntry().setTargetList(this);
			markerArray.get(i).getEntry().refresh();
		}
		
		Box out = Box.createVerticalBox();
		int count = 0;
		for (int j = 0; j < mapArray.size();j++) {
			JLabel textDivide = new JLabel();
			textDivide.setText("===" + mapArray.get(j).getName() + "===");
			
			out.add(textDivide);
			for (int i = 0; i < markerArray.size(); i++) {
				Marker mark = markerArray.get(i);
				if (mark.isVisible() && mark.getMap().equals(mapArray.get(j))) {
					mark.getEntry().setVisible(true);
					mark.getEntry().setPreferredSize(new Dimension((int)this.getSize().getWidth()-25,(int)this.getSize().getHeight()/10));
					if (mark.equals(Interface.activeMarker))
						mark.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
					else
						mark.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
					mark.getEntry().repaint();
					out.add(mark.getEntry());
					out.add(Box.createVerticalStrut(10));
					count++;
				}
			}
		}
		pane.setViewportView(out);
		pane.getVerticalScrollBar().setValue(position);
		this.revalidate();
		this.getParent().repaint(); 
	}
	public void findActive() {

		for (int i = 0; i < markerArray.size(); i++) {
			if (markerArray.get(i).equals(Interface.activeMarker))
				pane.getVerticalScrollBar().setValue(markerArray.get(i).getEntry().getParent().getLocation().y +
						markerArray.get(i).getEntry().getLocation().y);
		}
	}
}
