package redcrawl.dstructs;

import redcrawl.constants.Constants;
import redcrawl.database.RawLink;

import java.sql.SQLException;
import java.util.LinkedHashSet;

public class CommitQueue {
	private LinkedHashSet<String> queue;
	private Integer length = Constants.DBQueueLength;
	private static int count = 0;
	private static CommitQueue cq;
	
	
	private CommitQueue(){
		this.queue = new LinkedHashSet<String>(length);
	}
	//singleton pattern static factory method
	public static CommitQueue getCommitQueue(){
		if( count == 0){
			cq = new CommitQueue();
			count +=1; 
		}
		return cq;
	}
	
	public LinkedHashSet<String> getQueue(){
		return this.queue;
	}
	
	public boolean addString(String str){
		if(this.queue.size() + 1 < length){
			queue.add(str);
			return true;
		}else{
			try{
				pushQueueToDB();
				queue.clear();
				return true;
			}catch(Exception sql){
				System.err.println("Error Writing to database");
				sql.printStackTrace();
				return false;
			}
		}	
	}
	
	
	private void pushQueueToDB() throws SQLException{
		RawLink rl = new RawLink();
		String[] stringArray = new String[queue.size()];
		queue.toArray(stringArray);
		rl.addList(stringArray);
	}
	
}
