import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import com.rapidminer.RapidMiner;
import com.rapidminer.RepositoryProcessLocation;
import com.rapidminer.belt.table.Table;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.ResultSetDataRowReader;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.ToolbarGUIStartupListener;
import com.rapidminer.gui.renderer.Renderer;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.operator.ExecutionMode;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.tools.PlatformUtilities;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.tree.DecisionTreeLeafCreator;
import com.rapidminer.report.Renderable;
import com.rapidminer.report.Reportable;
import com.rapidminer.repository.IOObjectEntry;
import com.rapidminer.repository.MalformedRepositoryLocationException;
import com.rapidminer.repository.ProcessEntry;
import com.rapidminer.repository.Repository;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.tools.XMLException;
import com.rapidminer.tools.plugin.Plugin;
import com.rapidminer.operator.concurrency.*;
import com.rapidminer.operator.concurrency.internal.*;


import java.io.File;
import java.io.IOException;
import java.lang.Object;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class GUILauncher implements ActionListener{

	JFrame mainFrame = new JFrame("App");
	JFrame imageFrame = new JFrame("Image");
	JTable tData = new JTable();
	JScrollPane sPane = new JScrollPane(tData);
	DefaultTableModel dtm = new DefaultTableModel();
	JLabel image1 = new JLabel();
	JLabel image2 = new JLabel();
	JLabel image3 = new JLabel();
	JLabel image4 = new JLabel();
	JLabel image5 = new JLabel("Gender");
	JLabel image6 = new JLabel("Income");
	JLabel image7 = new JLabel("Married");
	JLabel image8 = new JLabel("Age");
	JPanel bPanel = new JPanel(new FlowLayout());
	JButton b25 = new JButton("25%");
	JButton b30 = new JButton("30%");
	JButton b50 = new JButton("50%");
	JFileChooser files = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
	JPanel textPanel = new JPanel(new GridLayout(1,3));
	JPanel filePanel = new JPanel(new FlowLayout());
	JPanel imagePanel = new JPanel(new GridLayout(1,4));
	JPanel imgLabelPanel = new JPanel(new GridLayout(1,4));
	JTextArea aText = new JTextArea(25,5);
	JTextArea bText = new JTextArea(25,5);
	JTextArea cText = new JTextArea(25,5);
	public GUILauncher(){
		mainFrame.setVisible(true);
		mainFrame.setSize(720, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		textPanel.add(aText);
		textPanel.add(bText);
		textPanel.add(cText);
		imagePanel.add(image1);
		imagePanel.add(image2);
		imagePanel.add(image3);
		imagePanel.add(image4);
		imgLabelPanel.add(image5);
		imgLabelPanel.add(image6);
		imgLabelPanel.add(image7);
		imgLabelPanel.add(image8);
		bPanel.add(b25);
		bPanel.add(b30);
		bPanel.add(b50);
		aText.setLineWrap(true);
		bText.setLineWrap(true);
		cText.setLineWrap(true);
		filePanel.add(files);

		mainFrame.add(bPanel, BorderLayout.CENTER);
		mainFrame.add(textPanel, BorderLayout.NORTH);
		
		imageFrame.add(imagePanel, BorderLayout.NORTH);
		imageFrame.add(imgLabelPanel, BorderLayout.CENTER);

		b25.addActionListener(this);
		b30.addActionListener(this);
		b50.addActionListener(this);

		System.setProperty(PlatformUtilities.PROPERTY_RAPIDMINER_HOME, Paths.get("").toAbsolutePath().toString());
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RapidMiner.init();
		Plugin.setPluginLocation("D:/RapidMiner Studio/lib/plugins");
		Plugin.addAdditionalExtensionDir("D:/RapidMiner Studio/lib/plugins");
		Plugin.setInitPlugins(true);
	}

	public static void main(String args[]) throws Exception {
		new GUILauncher();

	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == b25){
			Repository repo;
			Process process = null;
			RepositoryProcessLocation processLocation;
			IOContainer ioResult = null;
			try {
				repo = RepositoryManager.getInstance(null).getRepository("Projects");
				processLocation = new RepositoryProcessLocation(new RepositoryLocation(repo.getLocation(), "Process_25%"));
				process = processLocation.load(null);
				ioResult = process.run();
			} catch (RepositoryException | IOException | XMLException | OperatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Vector<BufferedImage> images = new Vector<>();
			for(int i=0;i<4;i++){

				IOObject result = ioResult.getElementAt((i+2));
				
				String name = RendererService.getName(result.getClass());
				java.util.List<Renderer> renderers = RendererService.getRenderers(name);

				imageFrame.setVisible(true);
				imageFrame.setSize(1280, 500);
				imageFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				imageFrame.setLocationRelativeTo(null);
				imageFrame.setResizable(false);
				
				for(Renderer renderer:renderers){
					IOContainer dummy = new IOContainer();
					int imgWidth = 320;
					int imgHeight = 420;
					Reportable reportable = renderer.createReportable(result, dummy, imgWidth, imgHeight);
					if(reportable instanceof Renderable){
						Renderable renderable = (Renderable) reportable;
						renderable.prepareRendering();
						int preferredWidth = renderable.getRenderWidth(600);
						int preferredHeight = renderable.getRenderHeight(800);
						final BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
						images.add(img);
						Graphics2D graphics = (Graphics2D) img.getGraphics();
						graphics.setColor(Color.WHITE);
						graphics.fillRect(0, 0, imgWidth, imgHeight);
						double scale = Math.min((double) imgWidth / (double) preferredWidth, (double) imgHeight / (double) preferredHeight);
						graphics.scale(scale, scale);
						renderable.render(graphics, preferredWidth, preferredHeight);
					}
				}
			}
			image1.setIcon(new ImageIcon(images.get(0)));
			image2.setIcon(new ImageIcon(images.get(1)));
			image3.setIcon(new ImageIcon(images.get(2)));
			image4.setIcon(new ImageIcon(images.get(3)));
			String result0 = ioResult.getElementAt(0).toString();
			String result1 = ioResult.getElementAt(1).toString();
			String result2 = ioResult.getElementAt(2).toString();

			aText.setText(result0);
			bText.setText(result1);
			cText.setText(result2);
		}
		else if(arg0.getSource() == b30){
			Repository repo;
			Process process = null;
			RepositoryProcessLocation processLocation;
			IOContainer ioResult = null;
			try {
				repo = RepositoryManager.getInstance(null).getRepository("Projects");
				processLocation = new RepositoryProcessLocation(new RepositoryLocation(repo.getLocation(), "Process_30%"));
				process = processLocation.load(null);
				ioResult = process.run();
			} catch (RepositoryException | IOException | XMLException | OperatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Vector<BufferedImage> images = new Vector<>();
			for(int i=0;i<4;i++){

				IOObject result = ioResult.getElementAt((i+2));
				
				String name = RendererService.getName(result.getClass());
				java.util.List<Renderer> renderers = RendererService.getRenderers(name);

				imageFrame.setVisible(true);
				imageFrame.setSize(1280, 500);
				imageFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				imageFrame.setLocationRelativeTo(null);
				imageFrame.setResizable(false);
				
				for(Renderer renderer:renderers){
					IOContainer dummy = new IOContainer();
					int imgWidth = 320;
					int imgHeight = 420;
					Reportable reportable = renderer.createReportable(result, dummy, imgWidth, imgHeight);
					if(reportable instanceof Renderable){
						Renderable renderable = (Renderable) reportable;
						renderable.prepareRendering();
						int preferredWidth = renderable.getRenderWidth(600);
						int preferredHeight = renderable.getRenderHeight(800);
						final BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
						images.add(img);
						Graphics2D graphics = (Graphics2D) img.getGraphics();
						graphics.setColor(Color.WHITE);
						graphics.fillRect(0, 0, imgWidth, imgHeight);
						double scale = Math.min((double) imgWidth / (double) preferredWidth, (double) imgHeight / (double) preferredHeight);
						graphics.scale(scale, scale);
						renderable.render(graphics, preferredWidth, preferredHeight);
					}
				}
			}
			image1.setIcon(new ImageIcon(images.get(0)));
			image2.setIcon(new ImageIcon(images.get(1)));
			image3.setIcon(new ImageIcon(images.get(2)));
			image4.setIcon(new ImageIcon(images.get(3)));
			String result0 = ioResult.getElementAt(0).toString();
			String result1 = ioResult.getElementAt(1).toString();
			String result2 = ioResult.getElementAt(2).toString();

			aText.setText(result0);
			bText.setText(result1);
			cText.setText(result2);
		}
		else if(arg0.getSource() == b50){
			Repository repo;
			Process process = null;
			RepositoryProcessLocation processLocation;
			IOContainer ioResult = null;
			try {
				repo = RepositoryManager.getInstance(null).getRepository("Projects");
				processLocation = new RepositoryProcessLocation(new RepositoryLocation(repo.getLocation(), "Process_50%"));
				process = processLocation.load(null);
				ioResult = process.run();
			} catch (RepositoryException | IOException | XMLException | OperatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Vector<BufferedImage> images = new Vector<>();
			for(int i=0;i<4;i++){

				IOObject result = ioResult.getElementAt((i+2));
				
				String name = RendererService.getName(result.getClass());
				java.util.List<Renderer> renderers = RendererService.getRenderers(name);

				imageFrame.setVisible(true);
				imageFrame.setSize(1280, 500);
				imageFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				imageFrame.setLocationRelativeTo(null);
				imageFrame.setResizable(false);
				
				for(Renderer renderer:renderers){
					IOContainer dummy = new IOContainer();
					int imgWidth = 320;
					int imgHeight = 420;
					Reportable reportable = renderer.createReportable(result, dummy, imgWidth, imgHeight);
					if(reportable instanceof Renderable){
						Renderable renderable = (Renderable) reportable;
						renderable.prepareRendering();
						int preferredWidth = renderable.getRenderWidth(600);
						int preferredHeight = renderable.getRenderHeight(800);
						final BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
						images.add(img);
						Graphics2D graphics = (Graphics2D) img.getGraphics();
						graphics.setColor(Color.WHITE);
						graphics.fillRect(0, 0, imgWidth, imgHeight);
						double scale = Math.min((double) imgWidth / (double) preferredWidth, (double) imgHeight / (double) preferredHeight);
						graphics.scale(scale, scale);
						renderable.render(graphics, preferredWidth, preferredHeight);
					}
				}
			}
			image1.setIcon(new ImageIcon(images.get(0)));
			image2.setIcon(new ImageIcon(images.get(1)));
			image3.setIcon(new ImageIcon(images.get(2)));
			image4.setIcon(new ImageIcon(images.get(3)));
			String result0 = ioResult.getElementAt(0).toString();
			String result1 = ioResult.getElementAt(1).toString();
			String result2 = ioResult.getElementAt(2).toString();

			aText.setText(result0);
			bText.setText(result1);
			cText.setText(result2);
		}

	}
}
