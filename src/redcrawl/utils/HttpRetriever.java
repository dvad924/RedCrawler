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
					urlc.getInputStream()));
			String line;
			while( (line = in.readLine()) != null  ){
				sb.append(line+"\n");
			}
			in.close();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
