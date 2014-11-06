package redcrawl.dstructs;

import java.sql.SQLException;

import redcrawl.database.RawLink;

public class URLQueue {
	private CurrentQueue queue;
	private CommitQueue dbQueue;
	
	public URLQueue(){
		queue = CurrentQueue.getCurrentQueue(); //queue for urls to be crawled
		dbQueue = CommitQueue.getCommitQueue();//queue for urls that are to be added to db
	}
	
	/**
	 * Add a URL to the URLqueue
	 * @param url
	 */
	public void addURL(RawLink url){
		//no matter what add to the dbQueue
		addDbQueueURL(url);
		//add this url to the current queue if possible
		addQueueURL(url);
	}
	
	/**
	 * Add a URL to the Current Queue
	 * @param url
	 */
	private void addQueueURL(RawLink url){
		try {
			if(!dbQueue.hasCommitted() && !url.linkExists()) //if nothing has been committed to the database 
				queue.addString(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   //simply add it to the current queue
		
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
	
	public void save(){
		dbQueue.save();
	}
}
