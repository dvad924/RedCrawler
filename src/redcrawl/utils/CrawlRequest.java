package redcrawl.utils;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.*;
import org.jsoup.select.Elements;

import redcrawl.database.*;
import redcrawl.constants.Constants;
import redcrawl.dstructs.URLQueue;

public class CrawlRequest extends Request {
	private HttpRetriever httpr;
	private URLQueue queue;
	private String root;
	private String output;
	
	public CrawlRequest(String root){
		this.httpr = new HttpRetriever();
		this.queue = new URLQueue();
		this.root = root;
	}
	@Override
	public void executeRequest() {
		// TODO Auto-generated method stub
		output = httpr.getHTML(queue.peekNextUrl());
	}
	@Override
	public void processRequest() {
		// TODO Auto-generated method stub
		if((output == null) && output.isEmpty())
			return;
		Document doc = Jsoup.parse(output);
		Elements els = doc.select("a");
		Iterator<Element> it = els.iterator();
		while(it.hasNext()){
			String href = it.next().attr("href");
			if(href.startsWith(Constants.baseURL+root))
				queue.addURL(href);
		}
		queue.useNextURL(); //clears this url from the queue;
	}
	
	
}
