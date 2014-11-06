package redcrawl.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redcrawl.constants.Constants;
import redcrawl.database.*;
public class HtmlUtility {
	private String link;
	private Document d;
	private ArrayList<String> rules;
	public HtmlUtility(String html,String link){
		this.d = Jsoup.parse(html);
		this.link = link;
		this.rules = new RobotsTxtParser(new HttpRetriever().getHTML(Constants.baseURL+"/robots.txt")).getRules();
		this.rules.addAll(Constants.getExtraRules());
	}
	
	public boolean isLegal(String href){
		Iterator<String> it = rules.iterator();
		while(it.hasNext()){
			String cur = it.next().trim();
			if(href.indexOf(cur) >=0)
				return false;
		}
		return true;
	}
	
	public ArrayList<RawLink> getLinks(){
		String base = Constants.baseURL + Constants.subReddit;
		ArrayList<RawLink> linkList = new ArrayList<RawLink>();
		Elements els = d.select("a");
		Iterator<Element> it = els.iterator();
		while(it.hasNext()){
			String link = it.next().attr("href");
			if(link.startsWith(base) && isLegal(link)){
				RawLink rl = new RawLink(link);
				linkList.add(rl);
			}
			else if(link.startsWith(Constants.subReddit) && isLegal(link)){
				RawLink rl = new RawLink(Constants.baseURL+link);
				linkList.add(rl);
			}
		}
		return linkList;
	}
	
	public PostTitle getTitle(String url){
		Elements title = this.d.select(".title a.title"); //get the title element on the page 
		Iterator<Element> it = title.iterator();
		if(!it.hasNext())
			return null;
		Element e = it.next();
		PostTitle pt = new PostTitle();						//create database object
		pt.setContent(e.text());
		pt.setLink(url);
		int index = link.indexOf("comments")+"comments/".length();
		String reddit_id = "t3_"+link.substring(index, link.indexOf('/', index) );
		pt.setRedditID(reddit_id);
		return pt;
	}
	
	public ArrayList<PostComment> getComments(String title_id){
		ArrayList<PostComment> commentList = new ArrayList<PostComment>();
		Elements els = d.select("form.usertext");
		Iterator<Element> it = els.iterator();
		while(it.hasNext()){
			Element e = it.next();
			String reddit_id = e.select("input").attr("value").toString();
			if(reddit_id != null && !reddit_id.isEmpty()){
				String type = reddit_id.substring(0,2);
				if(type.equals("t1") || type.equals("t3")){
					PostComment pc = new PostComment();
					pc.setLink(link);
					pc.setContent(e.text());
					pc.setRedditID(reddit_id);
					pc.setParentID(title_id);
					commentList.add(pc);
				}
			}
		}
		return commentList;
	}
	
}
