package ui;

import redcrawl.database.MyConnection;
import redcrawl.utils.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;


public class UserInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel displayPanel;
	private JPanel sidePanel;
	private JPanel buttonPanel;
	private JPanel inputPanel;
	private JButton toggleButton;
	private JTextField subRedditField;
	private JTextArea output;
	private JScrollPane scrollPane;
	private JLabel subRedditLabel;
	private boolean started = false;
	private Main runner;
	private MyCloser closer;
	
	
	public UserInterface(){
		closer = new MyCloser();
		setTitle("Reddit Webcrawler");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(closer);
		setLayout(new GridLayout(1,2));
		
		buildPanels();
		add(displayPanel);
		add(sidePanel);
		
		pack();
		setVisible(true);
		redirectSystemOutput(); //set so messages are appended to the textArea
	}
	
	private void buildPanels(){
		displayPanel = new JPanel();
		sidePanel = new JPanel(new GridLayout(2,1));
		buttonPanel = new JPanel();
		inputPanel = new JPanel();
		toggleButton = new JButton("Start");
		toggleButton.addActionListener(new ToggleListener());
		subRedditField = new JTextField(20);
		output = new JTextArea(40, 45);
		output.getDocument().addDocumentListener(new LimitLinesDocumentListener(1000));
		scrollPane = new JScrollPane(output);
		output.setEnabled(false);
		output.setBackground(Color.BLACK);
		subRedditLabel = new JLabel("Enter the subreddit: ");
		displayPanel.add(scrollPane);
		buttonPanel.add(toggleButton);
		inputPanel.add(subRedditLabel);
		inputPanel.add(subRedditField);
		sidePanel.add(buttonPanel);
		sidePanel.add(inputPanel);
	}
	
	class ToggleListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			toggleButton.setEnabled(false);
			if(!started){
				started = !started;
				System.out.println("Starting To Crawl");
				runner = new Main();
				runner.begin();
				closer.setMain(runner);
				toggleButton.setToolTipText("press to end");
				toggleButton.setText("End");	
			}
			else{
				started = !started;
				runner.end();
				try {
					MyConnection.reset();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.err.print("Error reseting Connection");
					e1.printStackTrace();
				}
				closer.setMain(null);
				toggleButton.setToolTipText("press to begin crawling");
				toggleButton.setText("Start");
			}
			toggleButton.setEnabled(true);
		}
		
	}
	
	
	private void updateTextArea(final String text){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				output.append(text);
			}
		});
	}
	
	private void redirectSystemOutput(){
		OutputStream out = new OutputStream(){

			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf(b));
			}
			
			public void write(byte[] b, int off, int len){
				updateTextArea(new String(b,off,len));
			}
			
			public void write(byte[] b){
				write(b,0,b.length);
			}
			
		};
		System.setOut(new PrintStream(out,true));
		System.setErr(new PrintStream(out,true));
	}
	
	public static void main(String[] args){
		new UserInterface();
	}
	
	
}
