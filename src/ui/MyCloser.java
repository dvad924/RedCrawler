package ui;
import redcrawl.utils.Main;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MyCloser implements WindowListener {

	private Main runner;
	
	public MyCloser(){
		runner = null;
	}
	
	public void setMain(Main run){
		this.runner = run;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(runner != null){
			runner.end();
		}
		System.exit(0);

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
