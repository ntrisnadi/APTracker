import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Entry extends JPanel{
	private Marker target;
	private MarkerList targetList;
	private Interface targetInterface;
	private JPanel panel = new JPanel();
	private JButton find = new JButton("Find");
	private JButton duplicate = new JButton("Duplicate");
	private JLabel textLabel = new JLabel();
	private MouseListener activator = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if(!target.equals(Interface.activeMarker))
				target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
			else
				target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(!target.equals(Interface.activeMarker))
				target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
			else
				target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Interface.activeMarker = target;
			targetInterface.refresh();
			target.getEntry().setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
		}
	};
	private MouseListener selector = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			panel.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			if (target.isSelected())
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
			target.toggleSelected();
			setActive();
		}
		
	};
	public Entry() {
		panel.add(textLabel);
		panel.addMouseListener(selector);
		duplicate.setFocusable(false);
		find.setFocusable(false);
		
		Box butts = Box.createVerticalBox();
		butts.add(find);
		butts.add(duplicate);
		
		Box layout = Box.createHorizontalBox();
		layout.add(panel);
		layout.add(butts);
		this.add(layout);
	}
	
	public JPanel getPanel() { return panel;}
	public JLabel getTextLabel() { return textLabel;}
	public Interface getTargetInterface() { return targetInterface;}
	
	public void setTextLabel(String text) { textLabel.setText(text);}
	public void setTarget(Marker target) {this.target = target;}
	public void setTargetList(MarkerList targetList) { this.targetList = targetList; }
	public void setTargetInterface(Interface src) { this.targetInterface = src; }
	public void updateActionListeners() {
		find.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setActive();
				targetInterface.findMarker(target);
			}
		});
		duplicate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Marker out = new Marker(target);
				targetList.getMarkerArray().add(out);
				targetInterface.activeMarker = out;
				targetInterface.refresh();
				out.getEntry().setTargetInterface(targetInterface);
				out.getEntry().updateActionListeners();
				targetList.findActive();
			}
		});		
		this.addMouseListener(activator);
	}
	public void setActive() {
		Interface.activeMarker = target;
		refresh();  targetList.refresh();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(target.getImage(),(int)Marker.offset.getX(),(int)Marker.offset.getY(),target.getWidth(),target.getHeight(),null);
	}
	
	public void refresh() {
		StringBuilder buff = new StringBuilder();
		buff.append("<html>");
		buff.append(String.format("%s<tr>%s<tr>%s", target.getType(), MarkerEditor.compile(target), target.getDescription()));
		buff.append("</html>");
		textLabel.setText(buff.toString());
	}

}
