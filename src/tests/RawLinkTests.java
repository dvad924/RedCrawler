package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import redcrawl.database.RawLink;

public class RawLinkTests {

	private ArrayList<RawLink> arrs;
	
	@Before
	public void initialize(){
		arrs = new ArrayList<RawLink>();
		RawLink rl = new RawLink("http://www.reddit.com/r/haskell/");
		RawLink rl2= new RawLink("http://www.reddit.com/r/haskell/new/");
		RawLink rl3= new RawLink("http://www.reddit.com/r/haskell/adds/");
		arrs.add(rl);
		arrs.add(rl2);
		arrs.add(rl3);
	}
	
	@Test
	public void test() {
		try{
			ArrayList<RawLink> delarrs= new RawLink().checkList(arrs);
			System.out.println(delarrs.toString());
			for(RawLink rl : delarrs){
				if(rl.getLink().equals("http://www.reddit.com/r/haskell/") || rl.getLink().equals("http://www.reddit.com/r/haskell/new/")){
					assertTrue(true);
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
			
	}

}
