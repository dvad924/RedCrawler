package tests;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import redcrawl.database.PostComment;
import redcrawl.utils.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class commentPullerTest {
	@Test
	public void test() throws IOException {
		String url = "http://redd.it/1vhjag";
		HttpRetriever hr = new HttpRetriever();
		String html = hr.getHTML(url);
		FileWriter fw = new FileWriter(new File("C:\\Users\\david\\Desktop\\test.html"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(html);
		HtmlUtility htmlutil = new HtmlUtility(false);
		htmlutil.setDoc(html);
		htmlutil.setLink(url);
		ArrayList<PostComment> list = htmlutil.getComments(url);
		assertTrue(list.size() == 0);
	}

	public void test2(){
		String s = "http://www.reddit.com/r/listentothis/comments/2lzj9r/rsigma_o_mito_do_insubstituível_world_indie_rock/";
		System.out.println(s);
		assertTrue(true);
	}
	
}
