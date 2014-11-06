package redcrawl.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import redcrawl.database.*;
import redcrawl.dstructs.URLQueue;
/**
 * This class will handle crawling the links by sending requests,
 * gathering the list of available links from that request
 * and titles of appropriate links
 * @author dvad924
 *
 */
public class CrawlRequest extends Request {
	private HttpRetriever httpr;   	//Retriever object for sending requests
	private URLQueue queue;			//URLQueue to enqueue the links found in requests
	private String output;			//The output html of the latest request
	private String curlink;
	/**
	 * Constructor takes the root url filter to be used in crawling
	 * @param root
	 */
	public CrawlRequest(String root){
		this.httpr = new HttpRetriever();
		this.queue = new URLQueue();
		if(this.queue.isEmpty()){
			queue.addURL(new RawLink(root));
		}
	}
	
	
	/**
	 * Simply request the next page
	 * and store the html in this object
	 */
	public boolean executeRequest() {
		this.curlink = queue.peekNextUrl(); //request the next available url
		if(this.curlink == null)
			return false;
		System.out.println("Requesting: "+this.curlink);
		output = httpr.getHTML(this.curlink);  	  //store the html
		return true;
	}
	/**
	 * Process data received from the request
	 */
	public void processRequest() {
//		// TODO Auto-generated method stub
		System.out.println("Request received properly");
		if((output == null) && output.isEmpty())		//if nothing came of the last request do nothing
			return;									
		HtmlUtility htmlutil = new HtmlUtility(output,curlink);
		ArrayList<RawLink> links = htmlutil.getLinks();
		for(RawLink rl : links){
			queue.addURL(rl);
		}
		if(this.curlink.indexOf("comments") >0){
			PostTitle pt = htmlutil.getTitle(curlink);
			if(pt != null){
				ArrayList<PostComment> comments = new ArrayList<PostComment>();
				if( !(pt.getRedditID() == null || pt.getRedditID().isEmpty()) ){
					comments = htmlutil.getComments(pt.getRedditID());
				}
				PostComment pc = new PostComment();
				try {
					
					int id = pt.titleExists(); //if the title exists get its id
					if(id < 0)
						id = pt.addTitle();         //if it doesn't exist add title
					pc.sendList(comments,id);   //add the comment to that title
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		queue.popNextURL(); 							//clears this url from the queue;
		
		
		System.out.println("Request Processed");
	}
	
	public void safeSave(){
		queue.save();
	}
	
}
