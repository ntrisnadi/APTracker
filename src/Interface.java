import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interface extends JFrame implements KeyListener{
	private double resolution = 0.70;
	
	private Dimension SIZE = new Dimension(1200,800);
	private Dimension buttSize = new Dimension();
	public JTabbedPane window = new JTabbedPane();
	private static Controller control;
	private static MarkerList list;
	public static Building activeBuilding;
	public static Map activeMap;
	public static Marker activeMarker = null;
	private ArrayList<Map> mapArray;
	private ArrayList<Marker> markerArray;
	private JButton add = new JButton("ADD");
	private JButton edit = new JButton("EDIT");
	private JButton delete = new JButton("DELETE");
	private JButton search = new JButton("Search");
	private JButton save = new JButton("SAVE");
	private JButton home = new JButton("HOME");
	private ActionListener toolClicks = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toolClick(arg0.getActionCommand());			
		}
	};
	
	public Interface(ArrayList<Map> maps, ArrayList<Marker> markers) {
		mapArray = maps;
		markerArray = markers;
		
		activeMap = mapArray.get(0);
		activeBuilding = activeMap.getBuilding();

		list = new MarkerList(markerArray, mapArray);
		list.setPreferredSize(new Dimension((int) (SIZE.getWidth()*(1-resolution)-10), (int) SIZE.getHeight()-100));
		list.setSize(list.getPreferredSize());
		list.getPane().setPreferredSize(list.getPreferredSize());
		
		for (int i = 0; i < mapArray.size();i++) {
			Display out = Display.createDisplay(this, mapArray.get(i), resolution);
			
			if (i == 0)
				control = new Controller(out);
			out.setController(control);
			out.setMarkerList(list);
			
			window.addTab(mapArray.get(i).getName(),out);
		}
		for (int i = 0; i < markerArray.size(); i++) {
			markerArray.get(i).getEntry().setTargetInterface(this);
			markerArray.get(i).getEntry().updateActionListeners();
		}
		
		window.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				activeMap = getActiveDisplay().getDisplayMap();
				control.setTargetDisplay(getActiveDisplay());
				getActiveDisplay().refresh();
			}
			
		});
		window.setPreferredSize(new Dimension((int) (SIZE.getWidth()*resolution), (int)SIZE.getHeight()));
		control.setFocusable(false);
		window.setFocusable(false);
		
		Box toolbox = Box.createVerticalBox();
		Box tools1 = Box.createVerticalBox();
		Box tools2 = Box.createVerticalBox();
		Box tools = Box.createHorizontalBox();
		
		add.setFocusable(false);
		add.addActionListener(toolClicks);
		edit.setFocusable(false);
		edit.addActionListener(toolClicks);
		delete.setFocusable(false);
		delete.addActionListener(toolClicks);
		search.setFocusable(false);
		search.addActionListener(toolClicks);
		save.setFocusable(false);
		save.addActionListener(toolClicks);
		home.setFocusable(false);
		home.addActionListener(toolClicks);
		
		tools1.add(add);
		tools1.add(edit);
		tools1.add(delete);
		tools2.add(search);
		tools2.add(save);
		tools2.add(home);
		
		tools.add(tools1);
		tools.add(tools2);
		toolbox.add(list);
		toolbox.add(tools);
		
		Box frame = Box.createHorizontalBox();
		frame.add(window);
		frame.add(toolbox);
		this.add(frame);
		
		this.setPreferredSize(SIZE);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
		getActiveDisplay().refresh();
		list.refresh();

		addKeyListener(this);
	}
	public static Controller getController() { return control; }
	public Display getActiveDisplay() { return (Display) window.getSelectedComponent();}
	public ArrayList<Marker> getMarkerList() { return markerArray;}
	public ArrayList<Map> getMapList() { return mapArray;}
	
	public void refresh() {
		((Display)window.getSelectedComponent()).refresh();
		list.refresh();
	}
	public void findMarker(Marker target) {
		Display targetDisplay = target.targetDisplay;

		Point destination = new Point((int) (targetDisplay.windowSize.width/2 - target.getX() * targetDisplay.getImageSize().width / targetDisplay.windowSize.getWidth()),
				targetDisplay.windowSize.height/2 - target.getY() * targetDisplay.getImageSize().height / targetDisplay.windowSize.height);
		targetDisplay.setLocation(destination);
		
		window.setSelectedComponent(targetDisplay);
		targetDisplay.setCursor((int)(target.getRelX()+target.getWidth()/2), (int)(target.getRelY()+target.getHeight()/2));
		refresh();
	}
	public void toolClick(String command) {
		if (command.equals(add.getText())) {
			MarkerEditor prompt = new MarkerEditor(this);
		} else if (command.equals(edit.getText())) {
			if (activeMarker != null) {
				MarkerEditor prompt = MarkerEditor.createMarkerEditor(this, activeMarker);
			}
		} else if (command.equals(delete.getText())) {
			for (int i = markerArray.size() -1; i >= 0; i--) {
				if (markerArray.get(i).isSelected())
					markerArray.remove(i);
			}
			refresh();
		} else if (command.equals(search.getText())) {
			Search prompt = new Search(this);
		} else if (command.equals(save.getText())) {
			for (int i = 0; i < markerArray.size(); i++) {
				if (!Loader.markerArray.contains(markerArray.get(i)))
					Loader.markerArray.add(markerArray.get(i));
			}
			
			for (int i = Loader.markerArray.size()-1; i >= 0; i--) {
				if (Loader.markerArray.get(i).getMap().getBuilding().equals(activeBuilding) &&
						!markerArray.contains(Loader.markerArray.get(i)))
					Loader.markerArray.remove(i);					
			}
		} else if (command.equals(home.getText())) {
			getActiveDisplay().home();
		}
	}
	
	public ArrayList<Marker> filterMarkers(Map target) {
		ArrayList<Marker> out = new ArrayList<Marker>();
		for (int i = 0; i < markerArray.size(); i++) 
			if (markerArray.get(i).getMap().equals(target)) 
				out.add(markerArray.get(i));
		return out;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		int initSpeed = 10;
		for (int i = 0; i < markerArray.size(); i++) {
			markerArray.get(i).popdown();
		}
		switch (key) {
		case(KeyEvent.VK_A) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.RIGHT);
			control.translate(Controller.DIR.RIGHT);
			break;
		case(KeyEvent.VK_S) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.UP);
			control.translate(Controller.DIR.UP);
			break;
		case(KeyEvent.VK_D) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.LEFT);
			control.translate(Controller.DIR.LEFT);
			break;
		case(KeyEvent.VK_W) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.DOWN);
			control.translate(Controller.DIR.DOWN);
			break;
		case(KeyEvent.VK_E) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.OUT);
			control.scale(Controller.DIR.OUT);
			break;
		case(KeyEvent.VK_Q) :
			if(control.isStatic())
				control.setVelocity(Controller.DIR.IN);
			control.scale(Controller.DIR.IN);
			break;
		case(KeyEvent.VK_UP) :
			control.translateCursor(Controller.DIR.UP);
			break;
		case(KeyEvent.VK_LEFT) :
			control.translateCursor(Controller.DIR.LEFT);
			break;
		case(KeyEvent.VK_DOWN) :
			control.translateCursor(Controller.DIR.DOWN);
			break;
		case(KeyEvent.VK_RIGHT) :
			control.translateCursor(Controller.DIR.RIGHT);
		}
		getActiveDisplay().refresh();
		list.refresh();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int timeStep = 10;
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT)
			while (!control.isCursorStatic()) {
				try {
					Thread.sleep(timeStep);
				} catch (InterruptedException el) {
					el.printStackTrace();
				}
				getActiveDisplay().refresh();
				control.slowCursor();
			}
		else
			while (!control.isStatic()) {
				try {
					getActiveDisplay().refresh();
					Thread.sleep(timeStep);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				getActiveDisplay().refresh();
				control.slow();
			}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
