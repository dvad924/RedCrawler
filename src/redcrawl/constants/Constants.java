package redcrawl.constants;

import java.util.ArrayList;

public class Constants {
	public static final String user_agent = "DV-MG-IR-csi550 http://www.albany.edu/~dv222552";
	public static final String baseURL = "http://www.reddit.com";
	public static final Integer MemoryQueueLength = 2;
	public static final Integer DBQueueLength = 5;
	public static final String subReddit = "/r/getralphlaid/";
	private static ArrayList<String> extraRules = null;
	public static ArrayList<String> getExtraRules(){
		setRules();
		return extraRules;
	}
	
	private static void setRules(){
		if(extraRules != null) 
			return; 
		extraRules.add("/related/");
	}
}
