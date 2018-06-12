import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import javafx.scene.input.KeyCode;

public class MarkerEditor extends JFrame {
	
	private static Interface target;
	private ArrayList<Map> targetMapList;
	private ArrayList<Marker> targetMarkerList;
	
	Box fields = Box.createVerticalBox();
	Dimension windowSize = new Dimension(250,400);
	
	public boolean addingNew = false;
	private JComboBox options = new JComboBox();
	private JTextArea name = new JTextArea("AP01");
	private JTextArea room = new JTextArea("#");
	private JComboBox floors = new JComboBox();
	private JTextArea descript = new JTextArea();
	private JTextArea preview = new JTextArea();
	private JButton accept = new JButton("Accept");
	private JButton cancel = new JButton("Cancel");
	private ActionListener clicking = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			execute(arg0);
		}
	};
	private KeyListener typing = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			refresh();
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				execute(new ActionEvent(accept, getDefaultCloseOperation(), "Accept"));
		}

		@Override
		public void keyReleased(KeyEvent e) {
			refresh();			
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	};
	public JComboBox getOptionsField() { return options;}
	public JTextArea getNameField() { return name;}
	public JTextArea getRoomField() { return room; }
	public JComboBox getFloorsField() { return floors; }
	public JTextArea getDescriptField() {return descript;}
	
	public MarkerEditor(Interface targetInterface) {
		addingNew = true;
		target = targetInterface;
		targetMapList = targetInterface.getMapList();
		targetMarkerList = targetInterface.getMarkerList();
		String[] types = new String[Loader.markerTypes.size()];
		for (int i = 0; i < Loader.markerTypes.size();i++) {
			types[i] = Loader.markerTypes.get(i).getType();
		}
		options = new JComboBox(types);
		String [] floorStrings = new String[Loader.mapArray.size()];
		for (int i = 0; i < targetMapList.size(); i++)
			floorStrings[i] = targetMapList.get(i).getFloor();
		floors = new JComboBox(floorStrings);
		floors.setSelectedItem(Interface.activeMap.getFloor());
				
		options.setBorder(BorderFactory.createTitledBorder("Marker Type"));
		setTraversalKey(options);
		name.setBorder(BorderFactory.createTitledBorder("Name"));
		setTraversalKey(name);
		room.setBorder(BorderFactory.createTitledBorder("Room ID"));
		setTraversalKey(room);
		floors.setBorder(BorderFactory.createTitledBorder("Floor"));
		setTraversalKey(floors);
		descript.setBorder(BorderFactory.createTitledBorder("Description"));
		setTraversalKey(descript);
		preview.setBorder(BorderFactory.createTitledBorder("Preview"));
		setTraversalKey(preview);
		preview.setEditable(false);
		preview.setOpaque(false);
		
		Box butts = Box.createHorizontalBox();
		setTraversalKey(butts);
		accept.addActionListener(clicking);
		cancel.addActionListener(clicking);
		butts.add(accept);
		butts.add(cancel);
		
		fields.add(options);
		fields.add(name);
		fields.add(room);
		fields.add(floors);
		fields.add(descript);
		fields.add(preview);
		fields.add(butts);
		
		
		for (int i = 0; i < fields.getComponentCount(); i++) 
			fields.getComponent(i).addKeyListener(typing);
		
		refresh();
		this.add(fields);
		this.setPreferredSize(windowSize);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.pack();
	}
	public static MarkerEditor createMarkerEditor(Interface targetInterface, Marker src) {
		MarkerEditor out = new MarkerEditor(targetInterface);
		out.addingNew = false;
		out.getOptionsField().setSelectedItem(src.getType());
		out.getNameField().setText(src.getName());
		out.getRoomField().setText(src.getRoom());
		out.getFloorsField().setSelectedItem(src.getMap().getFloor());
		out.getDescriptField().setText(src.getDescription());
		target.findMarker(src);
		out.refresh();
		return out;
	}
	public void execute(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Accept")) {
			Marker out = new Marker();
			if (!addingNew) {
				out = Interface.activeMarker;
			}

			out.setLabel(compile());
			out = Loader.findMarker(out, (String) options.getSelectedItem());
			out.setInterface(target);
			
			out.setName(name.getText());
			out.setRoom(room.getText());
			out.setDescription(descript.getText());
			Point coord = target.getActiveDisplay().unscaleCursor();
			coord.translate(out.getImage().getWidth()/2 - 1, out.getImage().getHeight()/2 - 1);
			out.setCoord(coord);
			out.setDisplay(target.getActiveDisplay());
			for (int i = 0; i < targetMapList.size(); i++)
				if (targetMapList.get(i).getFloor().equals((String) floors.getSelectedItem())) {
					out.setMap(targetMapList.get(i));
				}
			if (addingNew) {
				targetMarkerList.add(out);
				out.getEntry().updateActionListeners();
			}
			target.activeMarker = out;
			target.refresh();
			this.dispose();
		}
		if (arg0.getActionCommand().equals("Cancel"))
			this.dispose();
	}
	public String compile() {
		String out = name.getText() + "." + room.getText() + "."
					+ (String) floors.getSelectedItem() + "." + Interface.activeMap.getBuilding().getAlias()
					+ "." + Loader.clientAlias + "." + Loader.stateName;
		return out;
	}
	public static String compile(Marker src) {
		String out = src.getName() + "." + src.getRoom() + "."
					+ src.getMap().getFloor() + "." + src.getMap().getBuilding().getAlias()
					+ "." + Loader.clientAlias + "." + Loader.stateName;
		return out;
	}
	public void refresh() {
		preview.setText(compile());
	}
	public void setTarget(Interface src) {
		target = src;
	}
	public void setTraversalKey(JComponent src) {
		Set set = new HashSet(src.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		src.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);
	}
}
