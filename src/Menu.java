import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Menu extends JFrame {
	
	private ArrayList<Building> buildingArray;
	private JTextArea info = new JTextArea();
	private JButton editBuildings = new JButton("Edit Buildings");
	private JButton refresh = new JButton("Refresh");
	private ActionListener mapClicks = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			openBuilding(e);
		}
	};
	private JScrollPane list = new JScrollPane();
	private Interface activeInterface;
	
	public Menu() {
		// TODO Make Project Info Editable
		info.setEditable(false);
		info.setOpaque(false);
		info.setText(Loader.getProjectInfo());
		
		// Get the global buildingArray
		buildingArray = Loader.buildingArray;
		
		// Button to change details of buildings
		editBuildings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editBuildings();
			}
		});
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildingArray = Loader.buildingArray;
				drawMenu();
			}
		});
		
		drawMenu();
		
		this.pack();
		this.setVisible(true);
	}
	public void drawMenu() {
		this.remove(list);
		
		Box out = Box.createVerticalBox();
		out.add(info);
		out.add(refresh);
		out.add(editBuildings);
		out.add(Box.createVerticalStrut(25));
		for (int i = 0; i < buildingArray.size(); i++) {
			JButton inButt = new JButton(buildingArray.get(i).getName());
			inButt.addActionListener(mapClicks);
			out.add(inButt);
		}
		list = new JScrollPane(out);
		this.add(list);
		this.revalidate();
		list.repaint();
		this.repaint();
	}
	public void openBuilding(ActionEvent e) {
		String name = e.getActionCommand();
		
		ArrayList<Map> inMapArray = new ArrayList<Map>();
		ArrayList<Marker> inMarkerArray = new ArrayList<Marker>();
		for (int i = 0; i < Loader.mapArray.size(); i++) {
			if (Loader.mapArray.get(i).getBuilding().getName().equals(name))
				inMapArray.add(Loader.mapArray.get(i));
		}
		for (int i = 0; i < Loader.markerArray.size(); i++) {
			if (inMapArray.contains(Loader.markerArray.get(i).getMap()))
				inMarkerArray.add(Loader.markerArray.get(i));
		}
		activeInterface = new Interface(inMapArray, inMarkerArray);
		activeInterface.setName(name);
		activeInterface.setVisible(true);
	}
	public void editBuildings() {
		BuildingEditor edit = new BuildingEditor();
		
		drawMenu();
	}

	public ArrayList<Building> sortBuildings(ArrayList<Map> mapArray) {
		ArrayList<Building> out = new ArrayList<Building>();
		for (int i = 0; i < mapArray.size(); i++) {
			for (int j = 0; j < out.size(); j++) {
				if (!out.get(j).equals(mapArray.get(i).getBuilding())) {
					out.add(mapArray.get(i).getBuilding());
				}
			}
		}
		return out;
	}
}

