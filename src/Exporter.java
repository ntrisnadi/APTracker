import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Exporter extends JPanel {
	
	private JFrame window = new JFrame("Export Whitelist");
	private Dimension listSize = new Dimension(800,500);
	
	private ArrayList<Map> mapArray;
	private ArrayList<Marker> markerArray;
	private ArrayList<Marker> markerTypes;
	private ArrayList<Building> buildingArray;

	private JScrollPane buildingList = new JScrollPane();
	private JScrollPane mapList = new JScrollPane();
	private JScrollPane markerTypeList = new JScrollPane();
	private JButton export = new JButton("Export");
	
	private MouseListener mouseClicks = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			refresh();
			
		}
		
	};
	private ActionListener toolClicks = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toolClick(arg0.getActionCommand());			
		}
	};
	
	public Exporter() {
		mapArray = Loader.mapArray;
		markerArray = Loader.markerArray;
		markerTypes = Loader.markerTypes;
		buildingArray = Loader.buildingArray;
		
		for (int i = 0; i < mapArray.size(); i++) {
			mapArray.get(i).setVisible(true);
			mapArray.get(i).setSelected(true);
		}
		for (int i = 0; i < markerTypes.size(); i++) {
			markerTypes.get(i).setSelected(true);
		}
		Box outBuildings = Box.createVerticalBox();
		for (int i = 0; i < buildingArray.size(); i++) {
			buildingArray.get(i).setSelected(true);
			outBuildings.add(buildingArray.get(i));
		}
		buildingList.setViewportView(outBuildings);
		for (int i = 0; i < markerArray.size(); i++) {
			markerArray.get(i).relabel();
		}
		
		export.addActionListener(toolClicks);
		
		Box lists = Box.createHorizontalBox();
		lists.add(buildingList);
		lists.add(mapList);
		lists.add(markerTypeList);
		refresh();
		
		Box butts = Box.createHorizontalBox();
		butts.add(export);
		Box layout = Box.createVerticalBox();
		layout.add(lists);
		layout.add(butts);
		
		window.setPreferredSize(listSize);
		window.add(layout);
		window.pack();
		window.setVisible(true);

	}
	public void refresh() {
		Box outMaps = Box.createVerticalBox();
		for (int i = 0; i < buildingArray.size(); i++) {
			JTextArea label = new JTextArea("========" + buildingArray.get(i).getName() +
					" (" + buildingArray.get(i).getAlias() + ")" + "========");
			label.setEditable(false); label.setOpaque(false); label.setBorder(BorderFactory.createEmptyBorder());
			outMaps.add(label);
			if (buildingArray.get(i).isSelected())
				for (int j = 0; j < mapArray.size(); j++) {
					if (buildingArray.get(i).equals(mapArray.get(j).getBuilding()))
						outMaps.add(mapArray.get(j));
				}
		}
		Box outTypes = Box.createVerticalBox();
		for (int i = 0; i < markerTypes.size(); i++) {
			outTypes.add(markerTypes.get(i).getEntry());
		}
		mapList.setViewportView(outMaps);
		markerTypeList.setViewportView(outTypes);
		window.repaint();
	}
	public void toolClick(String command) {
		if (command.equals(export.getText()));
			export();
	}
	public void export() {

		// Sort and save details to a text file
		final JFileChooser fc = new JFileChooser();
		int returnValue = fc.showSaveDialog(window);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			File src = fc.getSelectedFile();
			String fileName = fc.getSelectedFile().getName();
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(src));
				
				Object[] choices = {"Labels Only", "Table(Tabs)", "Table(Commas)"};
				Object selectedValue = JOptionPane.showInputDialog(this,"Specify export behavior","Export",
						JOptionPane.QUESTION_MESSAGE,null,choices,choices[1]);
				
				if (selectedValue == choices[1] || selectedValue == choices[2]) {
					bw.write("Type\tName\tRoom\tBuilding\tSchool\tState\tDescription\tLabel");
					bw.newLine();
				}
				for (int i = 0; i < buildingArray.size(); i++) {
					for (int j = 0; j < mapArray.size(); j++) {
						if (mapArray.get(j).isSelected() && mapArray.get(j).getBuilding().equals(buildingArray.get(i))) {
							for (int k = 0; k < markerArray.size(); k++) {
								if (markerArray.get(k).getMap().equals(mapArray.get(j))) {
									for (int L = 0; L < markerTypes.size(); L++) {
										if (markerArray.get(k).getType().equals(markerTypes.get(L).getType()) &&
												markerTypes.get(L).isSelected()) {

											if (selectedValue == choices[0])
												bw.write(markerArray.get(k).getLabel());
											else {
												String delim;
												if(selectedValue == choices[1])
													delim = "\t";
												else
													delim = ",";
												bw.write(markerArray.get(k).getType() + delim +
														markerArray.get(k).getName() + delim +
														markerArray.get(k).getRoom() + delim +
														markerArray.get(k).getMap().getBuilding().getName() + delim + 
														Loader.clientAlias + delim + 
														Loader.stateName + delim +
														markerArray.get(k).getDescription() + delim +
														markerArray.get(k).getLabel());
											}
												
											bw.newLine();
											System.out.println(markerArray.get(k).getLabel());
											break;
										}
									}
								}
							}
							bw.newLine();
						}
					}
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
