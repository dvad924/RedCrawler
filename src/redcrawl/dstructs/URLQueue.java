package redcrawl.dstructs;

//Each time a page is rendered a database call should be made to determine unique links


public class URLQueue {
	private CurrentQueue queue = CurrentQueue.getCurrentQueue();
	private CommitQueue dbQueue = CommitQueue.getCommitQueue();
	private boolean canAddToCurrent = true;
	
	public void addURL(String url){
		//add to the current queue until its full, then never again
		if(canAddToCurrent)
			canAddToCurrent = queue.addString(url);
		//no matter what add to the dbQueue
		dbQueue.addString(url);
	}
	
	public String useNextURL(){
		return queue.popTopString();
	}
	
	public String peekNextUrl(){
		return queue.peekTopString();
	}
		
}
