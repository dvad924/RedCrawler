package redcrawl.dstructs;

import redcrawl.constants.Constants;

import java.util.LinkedHashSet;
import java.util.Iterator;

/*Need to think of a good queue with uniqueness and hopefully concurrent*/

public class CurrentQueue {
	private LinkedHashSet<String> queue;
	private Integer length = Constants.MemoryQueueLength;
	private static int count = 0;
	private static CurrentQueue cq;
	
	
	private CurrentQueue(){
		this.queue = new LinkedHashSet<String>(length);
	}
	
	public static CurrentQueue getCurrentQueue(){
		if(count == 0){
			cq = new CurrentQueue();
			count += 1;
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
		}else
			return false;
	}	
	public String popTopString(){
		Iterator<String> it = queue.iterator();
		if(!it.hasNext())
			return null;
		String top = it.next(); //get the top element from the queue
		it.remove();            //then remove it
		return top;
	}
	public String peekTopString(){
		Iterator<String> it = queue.iterator();
		if(!it.hasNext())
			return null;
		return it.next();
	}
	
}
