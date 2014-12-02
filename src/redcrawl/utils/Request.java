package redcrawl.utils;

import org.joda.time.DateTime;

public abstract class Request implements Runnable{
	private TimeKeeper tk = TimeKeeper.getTimeKeeper();
	private Boolean notInterrupted = true;
	
	public synchronized void toggleCont(){
		this.notInterrupted = !this.notInterrupted;
	}
	public void run(){
		boolean canContinue = true;
		while(notInterrupted && canContinue){
		if(tk.timeAvailable()){
			canContinue  = executeRequest();
			if(!canContinue)
				break;
			tk.noteLastRequest();
			processRequest();
		} else{
			try {
				
					Thread.sleep(tk.timeRemaining());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		safeSave();
		if(!canContinue)
			System.out.println("Crawling finished, No more links");
	}
	
	public abstract void safeSave();
	public abstract boolean executeRequest();
	public abstract void processRequest();
}
