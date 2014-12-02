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
	public HtmlUtility(boolean chkRobotsFile){
		if(chkRobotsFile){
			this.rules = new RobotsTxtParser(new HttpRetriever().getHTML(Constants.baseURL+"/robots.txt")).getRules();
			this.rules.addAll(Constants.getExtraRules());
		}
		else
			this.rules = Constants.getExtraRules();
	}
	public void setLink(String link){
		this.link = link;
	}
	public void setDoc(String html){
		this.d = Jsoup.parse(html);
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
			Element anchor = it.next();
			String link = anchor.attr("href");
			//if we try all comments then we only filter out parent links, 
			//if we don't try all we don't take any of the permalink links
			if(!anchor.text().equals("parent") && (Constants.tryAllComments)? true:!anchor.text().equals("permalink")){
				if(link.startsWith(base) && isLegal(link)){
					RawLink rl = new RawLink(link);
					linkList.add(rl);
				}
				else if(!Constants.subReddit.isEmpty() && link.startsWith(Constants.subReddit) && isLegal(link)){
					RawLink rl = new RawLink(Constants.baseURL+link);
					linkList.add(rl);
				}
			}
		}
//		Elements mores = d.select("span.morecomments");
//		Iterator<Element> it2 = mores.iterator();
//		while(it2.hasNext()){
//			Elements parents = it.next().parents();
//			Iterator<Element> pit = parents.iterator();
//			while(pit.hasNext()){
//				if(pit.next().hasClass("entry unvoted")){
//					
//				}
//			}
//			
//		}
		
		return linkList;
	}
	
	public String getId(String Url){
		int index = Url.indexOf("comments")+"comments/".length();
		int end = Url.indexOf('/',index);
		if(end < 0)
			end = Url.length();
		String reddit_id = Url.substring(index, end );
		return reddit_id;
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
		//int index = link.indexOf("comments")+"comments/".length();
		String reddit_id = "t3_"+getId(link);//.substring(index, link.indexOf('/', index) );
		pt.setRedditID(reddit_id);
		return pt;
	}
	
	public ArrayList<PostComment> getComments(String title_id){
		ArrayList<PostComment> commentList = new ArrayList<PostComment>();
		Elements els = d.select("form.usertext");
		Iterator<Element> it = els.iterator();
		while(it.hasNext()){
			Element e = it.next();
			if(e.text()==null || e.text().isEmpty())
				continue;
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
