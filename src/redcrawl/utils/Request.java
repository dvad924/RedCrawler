package redcrawl.utils;

public abstract class Request {
	private TimeKeeper tk = TimeKeeper.getTimeKeeper();
	
	public void makeRequest(){
		if(tk.timeAvailable()){
			executeRequest();
			tk.noteLastRequest();
			processRequest();
		}
	}
	
	
	public abstract void executeRequest();
	public abstract void processRequest();
}
