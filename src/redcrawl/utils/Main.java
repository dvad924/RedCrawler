package redcrawl.utils;

import redcrawl.constants.Constants;
import redcrawl.database.RawLink;
import redcrawl.dstructs.CurrentQueue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
public class Main {

	
	public static void main(String[] args) throws SQLException {
		CrawlRequest cr = new CrawlRequest(Constants.baseURL+Constants.subReddit);
		Thread t = new Thread(cr);
		t.start();
		Scanner sc = new Scanner(System.in);
		int input = 1;
		while(input != 0){
			input = sc.nextInt();
		} //wait in prompt for a zero
		sc.close();
		cr.toggleCont();
		
		
		
		
	}
	
	public static void testPrint(CurrentQueue cq){
		Iterator<RawLink> it = cq.getQueue().iterator();
		while(it.hasNext()){
			System.out.println(it.next().getLink());
		}
		System.out.println();
	}

}
