package net.pyraetos;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.pyraetos.util.Sys;

public class PFCFrame extends JFrame{

	private static JPanel panel;
	private static JPanel filePanel;
	private static JTextArea textArea;
	private static JTextField localTextField;
	private static JTextField remoteTextField;
	private static JTextField passwordTextField;
	private static JButton browseButton;
	private static JButton sendButton;
	public static final String NULL = "Choose a file!";
	
	public PFCFrame(){
		initWindow();
		initTextArea();
		initFilePanel();
		initLocalTextField();
		initBrowseButton();
		initRemoteTextField();
		initPasswordTextField();
		initSendButton();
		pack();
		setVisible(true);
		addLine("Ready!");
	}
	
	private void initWindow(){
		this.setLocation(200, 100);
		this.setResizable(false);
		this.setTitle("Pyraetos File Client");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel = new JPanel();
		this.setContentPane(panel);
	}
	
	private void initTextArea(){
		textArea = new JTextArea("Loading...", 12, 24);
		textArea.setEditable(false);
		panel.add(new JScrollPane(textArea));
	}
    
	private void initFilePanel(){
		filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		panel.add(filePanel);
	}
	
	private void initLocalTextField(){
		localTextField = new JTextField(NULL, 42);
		localTextField.setEditable(false);
		localTextField.setBackground(Color.WHITE);
		filePanel.add(localTextField);
		filePanel.add(Sys.space());
	}
	
	private void initRemoteTextField(){
		remoteTextField = new JTextField("C:\\Program Files (x86)\\Apache Software Foundation\\Apache2.2\\htdocs\\FILENAME", 42);
		remoteTextField.setBackground(Color.WHITE);
		filePanel.add(remoteTextField);
		filePanel.add(Sys.space());
	}
	
	private void initPasswordTextField(){
		passwordTextField = new JTextField("Enter password!", 42);
		passwordTextField.setBackground(Color.WHITE);
		filePanel.add(passwordTextField);
		filePanel.add(Sys.space());
	}
	
	private void initBrowseButton(){
		browseButton = new JButton("Browse");
		browseButton.addActionListener((e) -> {
			PFileChooser fileChooser = new PFileChooser();
			fileChooser.showOpenDialog(this);
		});
		filePanel.add(browseButton);
		filePanel.add(Sys.space());
	}
	
	private void initSendButton(){
		sendButton = new JButton("Send");
		sendButton.addActionListener((e) -> {
			new FileSender(localTextField.getText(), remoteTextField.getText(), passwordTextField.getText());
		});
		filePanel.add(sendButton);
	}
	
	public static synchronized void addLine(String string){
		textArea.setText(textArea.getText() + "\n" + string);
	}
	
	public static void main(String[] args){
		new PFCFrame();
	}

	public class PFileChooser extends JFileChooser{
		
		@Override
		public void approveSelection(){
			localTextField.setText(this.getSelectedFile().getAbsolutePath());
			super.approveSelection();
		}
		
	}
}
