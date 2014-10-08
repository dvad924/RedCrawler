package redcrawl.utils;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.*;
import org.jsoup.select.Elements;

import redcrawl.constants.Constants;
public class Main {

	
	public static void main(String[] args) {
		HttpRetriever ret = new HttpRetriever();
		String output = ret.getHTML("/r/programming");
		
		Document doc = Jsoup.parse(output);
		Elements els = doc.select("a");
		Iterator<Element> it = els.iterator();
		while(it.hasNext()){
			String href = it.next().attr("href");
			if(href.startsWith(Constants.baseURL+"/r/programming"))
				System.out.println(href);
		}
		//RobotsTxtParser rp = new RobotsTxtParser(output);
		/*System.out.println(output);
		for(String rule: rp.getRestrictions()){
			System.out.println(rule);
		}*/
	}

}
