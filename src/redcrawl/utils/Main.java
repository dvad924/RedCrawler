package redcrawl.utils;


public class Main {

	public static void main(String[] args) {
		HttpRetriever ret = new HttpRetriever();
		String output = ret.getHTML("/robots.txt");
		RobotsTxtParser rp = new RobotsTxtParser(output);
		System.out.println(output);
		for(String rule: rp.getRestrictions()){
			System.out.println(rule);
		}
	}

}
