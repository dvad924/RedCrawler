package redcrawl.utils;

import redcrawl.constants.Constants;
import redcrawl.database.RawLink;
import redcrawl.dstructs.CurrentQueue;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Main {

	private CrawlRequest cr;
	private Thread t;
	
	public Main(){
		cr = new CrawlRequest(Constants.baseURL+Constants.subReddit);
		t = new Thread(cr);
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		Main runner = new Main();
		runner.begin();
		Scanner sc = new Scanner(System.in);
		int input = 1;
		while(input != 0 && runner.isAlive()){
			input = sc.nextInt();
		} //wait in prompt for a zero
		sc.close();
		runner.end();	
		
	}
	
	public void begin(){
		t.start();
	}
	
	public boolean isAlive(){
		return t.isAlive();
	}
	
	public void end(){
		cr.toggleCont();
		while(t.isAlive()){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void testPrint(CurrentQueue cq){
		Iterator<RawLink> it = cq.getQueue().iterator();
		while(it.hasNext()){
			System.out.println(it.next().getLink());
		}
		System.out.println();
	}

}
