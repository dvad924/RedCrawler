package redcrawl.dstructs;

import redcrawl.constants.Constants;
import redcrawl.database.*;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Iterator;
import org.joda.time.DateTime;

/*Need to think of a good queue with uniqueness and hopefully concurrent*/
public class CurrentQueue {
	private LinkedHashSet<RawLink> queue;					//queue for url to be used next
	private Integer length = Constants.MemoryQueueLength;	//length of the queue
	//private static int count = 0;							//Singleton check
	private static CurrentQueue cq;							//Singleton instance

	private CurrentQueue(){
		this.queue = new LinkedHashSet<RawLink>(length);
	}
	/**
	 * static factory method to return and/or create instance of queue 
	 * @return queue instance
	 */
	public static CurrentQueue getCurrentQueue(){
			cq = new CurrentQueue();
			try{
				cq.refresh();
			}catch(SQLException sql){
				System.err.println("Error initializing Queue from  database");
				System.exit(1);
			}
			return cq;
	}
	
	public void refresh() throws SQLException{
		RawLink rl = new RawLink();		
		queue.addAll(rl.getUnseen(Constants.MemoryQueueLength)); //get as many unseen urls as will fit
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}
	
	public LinkedHashSet<RawLink> getQueue(){
		return this.queue;
	}
	
	/**
	 * Add a string to the queue
	 * @param str
	 * @return true => success, false => failure
	 */
	public boolean addString(RawLink rl){
		
		if(this.queue.size() + 1 < length){		//if it will fit
			queue.add(rl);						//add it to the queue
			return true;
		}else
			return false;						//otherwise do nothing
	}	
	
	/**
	 * Remove the first element from queue
	 * @return the string in that element
	 */
	public String popTopString(){		
		Iterator<RawLink> it = queue.iterator();		
		if(!it.hasNext())							//if queue is empty return null
			return null;
		RawLink top = it.next(); //get the top element from the queue
		it.remove();             //then remove it
		try{
			if(top.getId() == null){
				top.checkID();
			}
			top.addAndMarkLink(DateTime.now()); //mark the link in the database as seen
			if(queue.size() == 0){				//if queue is empty
				RawLink rl = new RawLink();		
				queue.addAll(rl.getUnseen(Constants.MemoryQueueLength)); //get as many unseen urls as will fit
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return top.getLink();  //return the link
	}
	
	/**
	 * Get the URL from the first element in the queue
	 * @return URL
	 */
	public String peekTopString(){
		Iterator<RawLink> it = queue.iterator();
		if(!it.hasNext())							//if queue is empty return null
			return null;
		return it.next().getLink();					//otherwise return the link
	}

	public int size(){
		return this.queue.size();
	}
	public void popAndDelete() {
		Iterator<RawLink> it = queue.iterator();
		if(!it.hasNext())
			return;
		RawLink rl = it.next();
		it.remove();
		rl.delete();
		
		
	}
}
