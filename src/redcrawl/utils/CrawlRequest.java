package redcrawl.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import org.joda.time.DateTime;

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
	private HtmlUtility htmlutil = new HtmlUtility(true);
	/**
	 * Constructor takes the root url filter to be used in crawling
	 * @param root
	 */
	public CrawlRequest(String root){
		this.httpr = new HttpRetriever();
		this.queue = new URLQueue();
		if(this.queue.isEmpty()){
			queue.add(new RawLink(root));
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
		System.out.println(DateTime.now().toString());
		output = httpr.getHTML(this.curlink);  	  //store the html
		return true;
	}
	/**
	 * Process data received from the request
	 */
	public void processRequest() {
//		// TODO Auto-generated method stub
		
		if((output == null) || output.isEmpty()){		//if nothing came of the last request do nothing
			queue.popAndDelete();
			return;								
		}
		System.out.println("Request received properly");
		htmlutil.setLink(curlink);
		htmlutil.setDoc(output);
		ArrayList<RawLink> links = htmlutil.getLinks();
		queue.addURLs(links);
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
		if(!queue.isEmpty()){
			System.out.println("Request Processed");
			if(queue.currentEmpty()){
				queue.save();
				queue.refresh();
			}
		}
	}
	
	public void safeSave(){
		queue.save();
	}
	
}
