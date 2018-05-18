import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Map extends JPanel{

	private Dimension fieldSize = new Dimension(90,50);
	private JPanel panel = new JPanel();
	private JTextArea name = new JTextArea();
	private JTextArea path = new JTextArea();
	private Building building = new Building();
	private BufferedImage image;
	private JTextArea floor = new JTextArea();
	private JButton findFile = new JButton("Find Image");
	private boolean selected = false;
	private boolean visible = true;
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

	public Map() {
		name.setPreferredSize(fieldSize);
		name.setBorder(BorderFactory.createTitledBorder("Name"));
		floor.setPreferredSize(fieldSize);
		floor.setBorder(BorderFactory.createTitledBorder("Floor"));
		Box layout = Box.createVerticalBox();
		Box fields = Box.createHorizontalBox();
		fields.add(Box.createHorizontalStrut(30));
		fields.add(name);
		fields.add(Box.createHorizontalStrut(10));
		fields.add(floor);
		fields.add(Box.createHorizontalStrut(10));
		fields.add(findFile);
		layout.add(fields);
		layout.add(path);
		
		findFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnValue = fc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION)
					setPath(fc.getSelectedFile().getAbsolutePath());
			}
		});
		
		panel.add(layout);
		this.add(panel);
		this.setVisible(true);
		this.addMouseListener(clicker);
		
	}
	
	public String getName() { return name.getText(); }
	public BufferedImage getImage() { return image;}
	public String getPath() { return path.getText();}
	public Building getBuilding() { return building; }
	public String getFloor() { return floor.getText(); }
	public boolean isSelected() {return selected;}
	public boolean isVisible() {return visible;}

	public void setName(String inName) { name.setText(inName);}
	public void setImage(BufferedImage src) {image = src;}
	public void setPath(String inPath) { path.setText(inPath);}
	public void setBuilding(Building inBuilding) { building = inBuilding; }
	public void setFloor(String inFloor) { floor.setText(inFloor); }
	public void setVisible(Boolean inVisible) { visible = inVisible;}
	public void setSelected(Boolean inSelect) {
		 selected = inSelect;
		if (selected) 
			panel.setBackground(Color.GRAY);
		else 
			panel.setBackground(Color.WHITE);
	}
	public boolean toggleSelected () {
		selected = !selected;
		if (selected) 
			panel.setBackground(Color.GRAY);
		else 
			panel.setBackground(Color.WHITE);
		return selected;
	}
	public boolean isDefined() {
		if (name == null || path == null || image == null)
			return false;
		return true;
	}
	public void reload() {
		try {
			this.setImage(ImageIO.read(new File(getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
