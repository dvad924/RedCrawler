package redcrawl.dstructs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import redcrawl.database.RawLink;

public class URLQueue {
	private CurrentQueue queue;
	private CommitQueue dbQueue;
	
	public URLQueue(){
		queue = CurrentQueue.getCurrentQueue(); //queue for urls to be crawled
		dbQueue = CommitQueue.getCommitQueue();//queue for urls that are to be added to db
		if(queue.size() > 0) //if there was something already in the database , then commit queue committed on previous round
			dbQueue.setCommitted(true);
	}
	
	/**
	 * Add a URL to the URLqueue
	 * @param url
	 */
	private void addURL(RawLink url){
		//no matter what add to the dbQueue
		addDbQueueURL(url);
		//add this url to the current queue if possible
		//addQueueURL(url); stack the database queue and refresh from database when empty
	}
	
	public void add(RawLink link){
		try {
			if(link.linkExists())
				return;
			else{
				addDbQueueURL(link);
				addQueueURL(link);
			}
		} catch (SQLException e) {
			System.err.println("Error Checking for Link Existance");
			e.printStackTrace();
		}
	}
	
	public void addURLs(List<RawLink> urls){
		ArrayList<RawLink> alreadySeen = null;
		try {
			alreadySeen = new RawLink().listCheck(urls); //check the list we got from reddit against our database
			for(RawLink rl : alreadySeen){    //remove any links that we have already seen
				urls.remove(rl);
			}
			for(RawLink link : urls){        //then add whatever remains to the queue
				addURL(link);
			}
		} catch (SQLException e) {
			System.err.println("Error Checking list against db");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a URL to the Current Queue
	 * @param url
	 */
	private void addQueueURL(RawLink url){
		/**this config needs to change or there is risk of infinite looping*/
		if(!dbQueue.hasCommitted()) //if nothing has been committed to the database 
			queue.addString(url);
	}
	
	public boolean isEmpty(){
		return queue.isEmpty() && dbQueue.isEmpty();
	}
	
	public boolean  currentEmpty(){
		return queue.isEmpty();
	}
	
	/**
	 * Add a URL to the Database queue
	 * @param url
	 */
	private void addDbQueueURL(RawLink url){
		dbQueue.addString(url);
	}
	
	/**
	 * Remove the First available URL from the Queue
	 * @return URL as String
	 */
	public String popNextURL(){
		return queue.popTopString();
	}
	
	/**
	 * Get the String URL that is at the beginning of the queue
	 * @return URL
	 */
	public String peekNextUrl(){
		return queue.peekTopString();
	}
	
	public void refresh(){
		try {
			queue.refresh();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.print("Error refreshing Queue");
			e.printStackTrace();
		}
	}
	
	public void save(){
		dbQueue.save();
	}

	public void popAndDelete() {
		queue.popAndDelete();
		try {
			queue.refresh();
		} catch (SQLException e) {
			System.err.println("Error refreshing Queue after deleting bad link");
			e.printStackTrace();
		}
		
	}
}
