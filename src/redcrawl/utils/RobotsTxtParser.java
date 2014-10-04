package redcrawl.utils;

import java.util.ArrayList;

import redcrawl.constants.Constants;

public class RobotsTxtParser {
	
	private String robotsText;
	private String user_agent; 
	private ArrayList<String> rules;
	
	public RobotsTxtParser(String rbtx){
		this.robotsText = rbtx;
		this.user_agent = Constants.user_agent;
		this.rules = getRestrictions();
	}
	/**
	 * Checks the robots.txt file for our user agent and the wild card agent
	 * */
	public ArrayList<String> getRestrictions(){
		String data = this.robotsText;
		String wildcard = "User-Agent: *\n";
		int index;
		ArrayList<String> newRules = new ArrayList<String>();
		if( (index = data.indexOf(user_agent)) >= 0){
			ArrayList<String> disallows = getDisallows(index);
			newRules.addAll(disallows);
		}
		if( (index = data.indexOf(wildcard)) >= 0){
			index += wildcard.length();
			ArrayList<String> disallows = getDisallows(index);
			newRules.addAll(disallows);
		}
		return newRules;
	}
	/**
	 * This function retrieves all disallow rules starting at the index
	 * @param index
	 * @return
	 */
	public ArrayList<String> getDisallows(int index){
		String goodToken = "Disallow:";
		String badToken  = "User-Agent:";
		ArrayList<String> newRules = new ArrayList<String>();
		while(contin(index,goodToken,badToken)){
			int start = this.robotsText.indexOf(goodToken,index) + goodToken.length();
			int end = this.robotsText.indexOf('\n',index);
			String newRule = (String) this.robotsText.subSequence(start,end);
			newRules.add(newRule);
			index = end+1;
		}
		return newRules;
	}
	
	/**
	 * This function determines if a goodToken preceeds a bad token ,
	 * or if there is no bad token, that another goodToken exists.
	 * @param index
	 * @param goodToken
	 * @param badToken
	 * @return
	 */
	public boolean contin(int index,String goodToken, String badToken){
		int nextUserAgent = this.robotsText.indexOf(badToken,index);
		int nextDisallow = this.robotsText.indexOf(goodToken,index);
		if(nextUserAgent > 0)
			return nextUserAgent > nextDisallow;
		else if(nextUserAgent < 0)
			return nextDisallow > 0;
		else
			return false;
	}
	public ArrayList<String> getRules() {
		return rules;
	}
	public void setRules(ArrayList<String> rules) {
		this.rules = rules;
	}	
	
}
