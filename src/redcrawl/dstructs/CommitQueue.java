package redcrawl.dstructs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import redcrawl.constants.Constants;
import redcrawl.database.RawLink;

public class CommitQueue {
	private LinkedHashSet<RawLink> queue;				//The queue to be commited to the database
	private Integer length = Constants.DBQueueLength;	//The length of the queue base on config
	private static int count = 0;						//Singleton check 
	private static CommitQueue cq;						//Singleton instance
	private boolean hasCommitted;						//yes/no has contacted database
	
	
	//private for singleton pattern
	private CommitQueue(){					
		this.queue = new LinkedHashSet<RawLink>(length);
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}
	
	/**
	 * singleton pattern static factory method
	 * @return
	 */
	public static CommitQueue getCommitQueue(){
		if( count == 0){
			cq = new CommitQueue();
			count +=1; 
		}
		return cq;
	}
	
	/**
	 * Get this queues hashSet 
	 * @return
	 */
	public LinkedHashSet<RawLink> getQueue(){
		return this.queue;
	}
	
	/**
	 * This will add a string to the queue.
	 * If the queue is full, it will commit its contents to the database
	 * then add the string
	 * @param str
	 * @return
	 */
	public boolean addString(RawLink rl){
		try{
			if(rl.linkExists()){			//Check if this link exists in the database
				return false;				//if it does end
			}
		}catch(SQLException sql){
			System.err.println("Error Checking for link existance");
			sql.printStackTrace();
		}
		if(this.queue.size() + 1 < length){	//if it will fit in the queue
			queue.add(rl);					//simply add it
			return true;
		}else{								//otherwise
			try{
				pushQueueToDB();			//Commit current queue to database
				this.hasCommitted = true;	//mark that it did
				queue.clear();				//remove all elements from the queue;
				queue.add(rl);				//add the new one;
				return true;				
			}catch(Exception sql){
				System.err.println("Error Writing to database");
				sql.printStackTrace();
				return false;
			}
		}	
	}
	
	public boolean containsString(String str){
		RawLink rl = new RawLink(str);
		return queue.contains(rl);
	}
	
	private void pushQueueToDB() throws SQLException{
		RawLink operator = new RawLink();
		ArrayList<RawLink> removals = operator.checkList(queue);
		for(RawLink rl : removals){
			queue.remove(rl);
		}
		operator.addList(queue);
	}
	
	public boolean hasCommitted(){
		return this.hasCommitted;
	}
	
	public void save(){
		try{
			pushQueueToDB();
			queue.clear();
		}catch(SQLException sql){
			try{
				FileWriter f = new FileWriter("/home/dvad924/proj/java/workspace/RedCrawler/DBsavefail.txt");
				BufferedWriter bfw = new BufferedWriter(f);
				for(RawLink rl : queue){
					bfw.write(rl.toString());
				}
				bfw.close();
			}catch(IOException io){
				io.printStackTrace();
			}
		}
	}
	
}
