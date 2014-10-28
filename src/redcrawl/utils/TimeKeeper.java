package redcrawl.utils;

import org.joda.time.*;

public class TimeKeeper {
	private static TimeKeeper tk;
	private DateTime lastRequest;
	private final int timeGap = 5000;
	
	private TimeKeeper(){
		lastRequest = new DateTime(0); //set  last request to begining of epoch
	}
	
	public static TimeKeeper getTimeKeeper(){
		if(tk == null){
			tk = new TimeKeeper();
		}
		return tk;
	}
	
	public synchronized boolean timeAvailable(){
		DateTime now = DateTime.now();
		if( now.getMillis() - lastRequest.getMillis() >= timeGap )
			return true;
		else 
			return false;
	}
	
	public synchronized void noteLastRequest(){
		this.lastRequest = DateTime.now();
	}
	
	public String toString(){
		return lastRequest.toString();
	}

}
