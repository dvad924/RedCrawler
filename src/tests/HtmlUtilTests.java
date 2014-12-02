package tests;
import redcrawl.utils.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class HtmlUtilTests {

	@Test
	public void test() {
		HtmlUtility util = new HtmlUtility(false);
		String id  = util.getId("http://www.reddit.com/r/listentothis/comments/2lzj9r/rsigma_o_mito_do_insubstituível_world_indie_rock/");
		assertTrue(id.equals("2lzj9r"));
	}

}
