import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class BuildingEditor extends JFrame{
	
	Dimension windowSize = new Dimension(900,600);
	Dimension fieldSize = new Dimension(300,30);
	Dimension listSize = new Dimension(300,300);
	
	private ArrayList<Building> buildingArray;
	private ArrayList<Map> mapArray;
	private ArrayList<Marker> markerArray;
	private JScrollPane buildingList = new JScrollPane();
	private JScrollPane mapList = new JScrollPane();
	
	private JButton addBuilding = new JButton("Add Building");
	private JButton addMap = new JButton("Add Map");
	private JButton importMaps = new JButton("Import Maps");
	private JButton delete = new JButton("DeleteSelected");
	private JButton reset = new JButton("Reset");
	private JButton sortBuilding = new JButton("Sort Building(s)");
	private JButton link = new JButton("Link Map(s)");
	private JButton save = new JButton("Save");

	private ActionListener toolClicks = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toolClick(arg0.getActionCommand());			
		}
	};

	public BuildingEditor () {
		// Get the global object arrays
		buildingArray = Loader.buildingArray;
		mapArray = Loader.mapArray;
		markerArray = Loader.markerArray;
		
		// Build interface
		addBuilding.addActionListener(toolClicks);
		addMap.addActionListener(toolClicks);
		importMaps.addActionListener(toolClicks);
		delete.addActionListener(toolClicks);
		reset.addActionListener(toolClicks);
		sortBuilding.addActionListener(toolClicks);
		link.addActionListener(toolClicks);
		save.addActionListener(toolClicks);
		
		Box layout = Box.createVerticalBox();
		Box lists = Box.createHorizontalBox();
		lists.add(buildingList);
		lists.add(mapList);
		layout.add(lists);
		Box butts = Box.createHorizontalBox();
		butts.add(addBuilding);
		butts.add(addMap);
		butts.add(importMaps);
		butts.add(delete);
		butts.add(reset);
		butts.add(sortBuilding);
		butts.add(link);
		butts.add(save);
		layout.add(butts);

		this.add(layout);
		this.setPreferredSize(windowSize);
		this.setVisible(true);
		this.pack();
		reset();
	}
	public void refresh() {
		// Remember original scroll position
		int buildingValue = buildingList.getVerticalScrollBar().getValue();
		int mapValue = mapList.getVerticalScrollBar().getValue();
		// Draw all buildings
		Box outBuilding = Box.createVerticalBox();
		for (int i = 0; i < buildingArray.size(); i++) {
			outBuilding.add(buildingArray.get(i));
		}
		
		Box outMap = Box.createVerticalBox();
		// Sorts and draws all maps
		for (int i = 0; i < buildingArray.size(); i++) {
			JTextArea label = new JTextArea("========" + buildingArray.get(i).getName() +
											" (" + buildingArray.get(i).getAlias() + ")" + "========");
			label.setEditable(false); label.setOpaque(false); label.setBorder(BorderFactory.createEmptyBorder());
			outMap.add(label);
			for (int j = 0; j < mapArray.size(); j++) {
				if (mapArray.get(j).getBuilding().equals(buildingArray.get(i)) && mapArray.get(j).isVisible()) {
					outMap.add(mapArray.get(j));
				}
			}
		}
		buildingList.setViewportView(outBuilding);
		buildingList.getVerticalScrollBar().setValue(buildingValue);
		mapList.setViewportView(outMap);
		mapList.getVerticalScrollBar().setValue(mapValue);
	}
	public void toolClick(String command) {
		if (command.equals(addBuilding.getText()))
			addBuilding();
		else if (command.equals(addMap.getText()))
			addMap();
		else if (command.equals(importMaps.getText()))
			importMaps();
		else if (command.equals(delete.getText()))
			delete();
		else if (command.equals(reset.getText()))
			reset();
		else if (command.equals(sortBuilding.getText()))
			sortBuilding();
		else if (command.equals(link.getText()))
			linkMaps();
		else if (command.equals(save.getText()))
			save();
		
	}
	public void addBuilding() {
		Building out = new Building();
		buildingArray.add(out);
		refresh();
	}
	public void addMap() {
		Map out = new Map();
		out.setBuilding(buildingArray.get(0));
		mapArray.add(out);
		refresh();
	}
	public void importMaps() {

		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			Object[] choices = {"File Only", "Name from File", "Name and Floor from File"};
			Object selectedValue = JOptionPane.showInputDialog(this,"Specify import behavior","Import",
					JOptionPane.QUESTION_MESSAGE,null,choices,choices[1]);
			System.out.println(selectedValue);
			
			File[] files = fc.getSelectedFiles();
			for (int i = 0; i < files.length; i++) {
				Map out = new Map();
				out.setBuilding(buildingArray.get(0));
				out.setPath(files[i].getPath());
				if(selectedValue == choices[1] || selectedValue == choices[2])
					out.setName(files[i].getName().substring(0, files[i].getName().length()-4));
				if(selectedValue == choices[2])
					System.out.println("Feature Not Implemented Yet");	// TODO Find Floor ID
				mapArray.add(out);
				refresh();
			}
		}
	}
	public void delete() {
		// TODO List the map deletion hierarchy
		for (int i = mapArray.size()-1; i >= 0; i--) {
			if (mapArray.get(i).getBuilding().isSelected() || mapArray.get(i).isSelected()) {
				mapArray.remove(i);
			}
		}
		for (int i = buildingArray.size()-1; i >= 0; i--) 
			if (buildingArray.get(i).isSelected())
				buildingArray.remove(i);
		refresh();
	}
	public void reset() {
		for (int i = 0; i < buildingArray.size(); i++) {
			buildingArray.get(i).setSelected(false);
		}
		for (int i = 0; i < mapArray.size(); i++) {
			mapArray.get(i).setVisible(true);
			mapArray.get(i).setSelected(false);
		}
		refresh();
	}
	public void sortBuilding() {
		// Go through buildings that are selected and find maps
		Box outMap = Box.createVerticalBox();
		for (int i = 0; i < buildingArray.size(); i++) {
			JTextArea label = new JTextArea(buildingArray.get(i).getName() + " (" + buildingArray.get(i).getAlias() + ")");
			label.setEditable(false); label.setOpaque(false); label.setBorder(BorderFactory.createEmptyBorder());
			outMap.add(label);
			for (int j = 0; j < mapArray.size(); j++) {
				if (mapArray.get(j).getBuilding().equals(buildingArray.get(i))) {
					if (buildingArray.get(i).isSelected())
						outMap.add(mapArray.get(j));
					else
						mapArray.get(j).setSelected(false);
					mapArray.get(j).setVisible(buildingArray.get(i).isSelected());
				}
			}
		}
		refresh();
	}
	public void linkMaps() {
		int selectedCount = 0;
		int index = -1;
		for (int i = 0; i < buildingArray.size(); i++) {
			if (selectedCount == 0 && buildingArray.get(i).isSelected())
				index = i;
			else
				if (selectedCount > 0 && buildingArray.get(i).isSelected()) {
				// TODO Create error message for multiple selected
				}
		}
		if (index == -1) {
			// TODO Create error message for no selected
		}
		
		for (int j = 0; j < mapArray.size(); j++) {
			if (mapArray.get(j).isVisible() && mapArray.get(j).isSelected()) {
				mapArray.get(j).setBuilding(buildingArray.get(index));
				mapArray.get(j).setSelected(false);
			}
		}
		
		refresh();
	}
	public void save() {
		for (int i = 0; i < mapArray.size(); i++) {
			mapArray.get(i).reload();
		}
		
		Loader.mapArray = mapArray;
		Loader.buildingArray = buildingArray;
		/*activeBuilding.setName(name.getText());
		activeBuilding.setAlias(alias.getText());
		for (int i = 0; i < mapEntries.size(); i++) {
			if (i < activeMapArray.size()) {
				MapEntry entry = mapEntries.get(i);
				activeMapArray.get(i).setBuilding(activeBuilding);
				activeMapArray.get(i).setName(entry.name.getText());
				activeMapArray.get(i).setFloor(entry.floor.getText());

				activeMapArray.get(i).setPath(entry.path.getText());
				try {
					activeMapArray.get(i).setImage(ImageIO.read(new File(activeMapArray.get(i).getPath())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				Map out = mapEntries.get(i).compileMap();
				out.setBuilding(activeBuilding);
				mapArray.add(out);
				System.out.println(out.getPath());
			}
		}
		
		Loader.buildingArray = buildingArray;
		Loader.mapArray = mapArray;
		return true;*/
	}
	public void setTraversalKey(JComponent src) {
		Set set = new HashSet(src.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		src.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);
	}
}
