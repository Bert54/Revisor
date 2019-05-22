package fr.loria.orpailleur.revisor.update;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import fr.loria.orpailleur.revisor.engine.core.utils.swing.Layout;
import fr.loria.orpailleur.revisor.engine.core.utils.swing.Panel;

/**
 * @author William Philbert
 */
public abstract class ScriptFrame extends JFrame {
	
	// FIXME - ScriptFrame
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	protected static final String SLASH = System.getProperty("file.separator");
	
	// Fields :
	
	private final JPanel mainPanel;
	private final JPanel configPanel;
	private final JPanel executionPanel;
	protected final JPanel parametersPanel;
	
	private final JTextArea textArea;
	private final JScrollPane scrollPane;
	private final JButton startButton;
	private final JButton closeButton;
	
	private boolean started = false;
	private boolean done = false;
	private int exitValue = 0;
	
	// Constructors :
	
	public ScriptFrame(String title) {
		super(title);
		
		this.mainPanel = new Panel();
		this.mainPanel.setOpaque(true);
		this.mainPanel.setBackground(new Color(0xD3E3F1));
		this.setContentPane(this.mainPanel);
		
		this.configPanel = new Panel();
		Layout.add(this.mainPanel, this.configPanel, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
		
		this.parametersPanel = new Panel();
		Layout.add(this.configPanel, this.parametersPanel, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		this.startButton = new JButton(this.startButtonText());
		Layout.add(this.configPanel, this.startButton, 0, 1, 1, 0, Layout.CENTER, Layout.BOTH, 2);
		
		this.executionPanel = new Panel();
		this.executionPanel.setVisible(false);
		Layout.add(this.mainPanel, this.executionPanel, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 0);
		
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.scrollPane = new JScrollPane(this.textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		Layout.add(this.executionPanel, this.scrollPane, 0, 0, 1, 1, Layout.CENTER, Layout.BOTH, 2);
		
		this.closeButton = new JButton(this.closeButtonText());
		this.closeButton.setEnabled(false);
		Layout.add(this.executionPanel, this.closeButton, 0, 1, 1, 0, Layout.CENTER, Layout.BOTH, 2);
		
		this.startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ScriptFrame.this.start();
			}
			
		});
		
		this.closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ScriptFrame.this.exit();
			}
			
		});
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent pwet) {
				ScriptFrame.this.exit();
			}
			
		});
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.4), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.4)));
		this.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.4), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.4)));
		this.pack();
		this.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));
		this.setVisible(true);
	}
	
	// Methods :
	
	protected abstract String startButtonText();
	
	protected abstract String closeButtonText();
	
	protected abstract String successMessage();
	
	protected abstract String failureMessage();
	
	protected abstract boolean checkParameters();
	
	protected abstract void initValues();
	
	protected abstract void execute() throws Exception;
	
	protected void exit() {
		if(!this.started || this.done) {
			System.exit(this.exitValue);
		}
	}
	
	protected void start() {
		if(!this.started && this.checkParameters()) {
			this.started = true;
			this.initValues();
			
			try {
				this.execute();
				this.addInfoMessage(this.successMessage());
			}
			catch(Exception argh) {
				this.exitValue = 1;
				this.addErrorMessage(argh);
				this.addErrorMessage(this.failureMessage());
			}
			
			this.done = true;
			this.closeButton.setEnabled(true);
		}
	}
	
	protected void updateScroll() {
		JScrollBar scrollBar = this.scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
	protected void addMessage(String message) {
		if(!this.textArea.getText().isEmpty()) {
			this.textArea.append("\n");
		}
		
		this.textArea.append(message);
		this.updateScroll();
	}
	
	protected void addInfoMessage(String message) {
		this.addMessage("INFO: " + message);
	}
	
	protected void addErrorMessage(String message) {
		this.addMessage("ERROR: " + message);
	}
	
	protected void addErrorMessage(Exception argh) {
		this.addErrorMessage(this.getStackTrace(argh));
	}
	
	protected String getStackTrace(Exception argh) {
		StringWriter sw = new StringWriter();
		argh.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	public static void initLocalAndLookAndFeel() {
		try {
			Locale.setDefault(Locale.US);
		}
		catch(Exception argh) {
			argh.printStackTrace();
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception argh) {
			argh.printStackTrace();
		}
	}
	
}
