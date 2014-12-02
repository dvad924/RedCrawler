package redcrawl.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import redcrawl.constants.Constants;
public class HttpRetriever {
	
	
	//Here we retrieve the html from the specified base url
	//path specifies the relative path to the file on the server
	public String getHTML(String path){
		String html = getHtml(path,0);
		if(html == null && path.contains("comments"))
		{
			//if regular url fails try the short url once and then return;
			html = getHtml("http://redd.it/"+getId(path),3);
		}
		return html;
		
	}
	private String getId(String Url){
		int index = Url.indexOf("comments")+"comments/".length();
		String reddit_id = Url.substring(index, Url.indexOf('/', index) );
		return reddit_id;
	}
	public String getHtml(String path,int count){
		if(path == null || path.isEmpty())
			path = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader in;
		try {
			URI uri = new URI(path);
			URL url = uri.toURL();
			URLConnection urlc = url.openConnection();
			urlc.setRequestProperty("User-Agent", Constants.user_agent);
			in = new BufferedReader(new InputStreamReader(
					urlc.getInputStream(),"UTF-8"));
			String line;
			while( (line = in.readLine()) != null  ){
				sb.append(line+"\n");
			}
			in.close();
		} catch (IOException io) {
			io.printStackTrace();
			if(count < Constants.numRequests){
				try {
					Thread.sleep(Constants.minumumTimeout);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
