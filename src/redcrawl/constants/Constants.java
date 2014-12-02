package redcrawl.constants;

import java.util.ArrayList;

public class Constants {
	public static final String user_agent = "DV-MG-IR-csi550 http://www.albany.edu/~dv222552";
	public static final String baseURL = "http://www.reddit.com";
	public static final Integer MemoryQueueLength = 50;
	public static final Integer DBQueueLength = 200;
	public static final String subReddit = "/r/askscience/";
	public static final boolean tryAllComments = false;
	private static ArrayList<String> extraRules = null;
	public static final int numRequests = 3;
	public static final int minumumTimeout = 5000;
	public static ArrayList<String> getExtraRules(){
		setRules();
		return extraRules;
	}
	
	private static void setRules(){
		if(extraRules != null) 
			return; 
		else{
			extraRules = new ArrayList<String>();
			extraRules.add("/related/");
			extraRules.add("/user/");
		}
	}
}
