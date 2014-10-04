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
	private String baseURL = Constants.baseURL;
	
	public String getBaseURL(){
		return this.baseURL;
	}
	public void setBaseURL(String url){
		this.baseURL = url;
	}
	public void resetBaseURL(){
		this.baseURL = Constants.baseURL;
	}
	
	//Here we retrieve the html from the specified base url
	//path specifies the relative path to the file on the server
	public String getHTML(String path){
		if(path == null || path.isEmpty())
			path = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader in;
		try {
			URI uri = new URI(baseURL+path);
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
