import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;

public class APTracker {
	private static final Dimension BUTTON_SIZE = new Dimension(200,100);

	public static String projectFileName;
	public static String corePath;
	private static Loader mLoader;
	private static Menu activeMapList;
	
	
	public static void main(String[] args) {
		
		// Creates the main menu window
		JFrame menu = new JFrame();
		JButton load = new JButton("Load Project");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Reinitializes the list of maps, markers, and buildings
				Loader.mapArray = new ArrayList<Map>();
				Loader.markerArray = new ArrayList<Marker>();
				Loader.buildingArray = new ArrayList<Building>();
				
				// Begin the loading process
				final JFileChooser fc = new JFileChooser();
				int returnValue = fc.showOpenDialog(menu);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					// Find the necessary paths
					projectFileName = fc.getSelectedFile().getName();
					corePath = fc.getSelectedFile().getPath().replaceAll(projectFileName, "");
					// Start the loading process
					mLoader = new Loader(fc.getSelectedFile(), corePath);
					
					// Open up the next menu for selecting buildings
					activeMapList = new Menu();
				}
			}
		});
		load.setPreferredSize(BUTTON_SIZE);
		
		JButton save = new JButton("Save Project");
		save.setPreferredSize(BUTTON_SIZE);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Save the information to a text file
				final JFileChooser fc = new JFileChooser();
				int returnValue = fc.showSaveDialog(menu);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					Saver save = new Saver(fc.getSelectedFile(),fc.getSelectedFile().getName());
				}
			}
		});

		JButton export = new JButton("Export Whitelist");
		export.setPreferredSize(BUTTON_SIZE);
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Exporter out = new Exporter();
			}
		});
		
		JButton close = new JButton("Quit");
		close.setPreferredSize(BUTTON_SIZE);
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				menu.dispose();
			}
		});
		Box buttons = Box.createVerticalBox();
		buttons.add(load);
		buttons.add(save);
		buttons.add(export);
		buttons.add(close);

		menu.add(buttons);


		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.pack();
		menu.setVisible(true);

	}
	
}
