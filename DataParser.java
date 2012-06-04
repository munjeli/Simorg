/* This class receives data from text files which are
 * supplied by the DataGrabber. The files are parsed
 * and formatted as csv to be loaded as records into the database
 */

import java.util.*;
import java.io.*;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

	public void statusParser() throws ParserConfigurationException, SAXException, IOException{
		System.out.println("Beginning status history parse...");
		
		Element item;
		
		ArrayList<String> tagNames = new ArrayList<String>();
		tagNames.add("created_at");
		tagNames.add("id");
		tagNames.add("text");
		tagNames.add("retweeted");
		tagNames.add("retweet_count");
		tagNames.add("in_reply_to_status_id");
		tagNames.add("in_reply_to_user_id");
		tagNames.add("in_reply_to_screen_name");
		tagNames.add("favorited");
		tagNames.add("geo");
		tagNames.add("place");
		tagNames.add("coordinates");
		tagNames.add("source");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		PrintWriter out = new PrintWriter(MyIds.hoopoeData+ "/" +  MyIds.rootUser + "Status.csv");		
		String fileName = (MyIds.hoopoeData + "/" + MyIds.rootUser + "Hx.xml");
		File f = new File(fileName);
		Document doc = builder.parse(f);
		doc.getDocumentElement().normalize();
			
		//work within each status update; get the tags return the values
		NodeList nodes = doc.getElementsByTagName("status");
		int statusCount = nodes.getLength();
		
				
		        for (int i = 0; i< statusCount; i++) {
		        	Node statusNode = nodes.item(i);
		        	item = (Element) statusNode;
		        	
		        	ArrayList<String> statusVals = parseTags(tagNames, item);
		        	
		        	//clean the tweetText
		        	String clnTweet = HooUtil.perfText(statusVals.get(2));
		        	//scrape the links if there are any
		        	String linktxt = linkScrape(clnTweet);
		        	//return the cleaned tweet, insert the link column
		        	statusVals.set(2, clnTweet);
		        	statusVals.add(3, linktxt);	
		        	
					StringBuilder statusStr = new StringBuilder();
			            for(String s : statusVals){
			            	statusStr.append(s);
			            	statusStr.append("\t");
			            }
					
			        out.println(statusStr.toString());	
	        
		        }
		        
			    out.close();
		        System.out.println("Status parse complete.");
				
	}
	
	public void ruserParse(ArrayList<String> uservals) throws ParserConfigurationException, SAXException, IOException{
		PrintWriter out = new PrintWriter(MyIds.hoopoeData + "/" +  MyIds.rootUser + "Info.csv");
        String userfile =  (MyIds.hoopoeData + "/" + MyIds.rootUser + "Info.xml");
		ArrayList<String> ruvals = userParser(MyIds.rootUser, userfile);
		
			StringBuilder userStr = new StringBuilder();
            for(String s : ruvals){
            	userStr.append(s);
            	userStr.append("\t");          			
            }
                  	
       	 out.println(userStr.toString());
       	 out.close();  
	}
            
	public void friendParse(){
		
	}
	
	//method to parse user details 		
	public ArrayList<String> userParser(String usr, String userfile) throws ParserConfigurationException, SAXException, IOException{
			System.out.println("Beginning user history parse...");
			
			ArrayList<String> tagNames = new ArrayList<String>();			
			tagNames.add("id");
			tagNames.add("name");
			tagNames.add("screen_name");
			tagNames.add("location");
			tagNames.add("description");
			tagNames.add("profile_image_url");
			tagNames.add("url");
			tagNames.add("protected");
			tagNames.add("followers_count");
			tagNames.add("friends_count");
			tagNames.add("created_at");
			tagNames.add("favourites_count");
			tagNames.add("time_zone");
			tagNames.add("geo_enabled");
			tagNames.add("verified");
			tagNames.add("statuses_count");
			tagNames.add("lang");
			tagNames.add("listed_count");
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder();
					
			String fileName = (MyIds.hoopoeData + "/" + usr + userfile);
			File f = new File(fileName);
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			
			//work within each status update; get the tags return the values
			NodeList nodes = doc.getElementsByTagName("user");
			int userCount = nodes.getLength();
			ArrayList<String> userVals = null;
					
			        for (int i = 0; i< userCount; i++) {
			        	Node userNode = nodes.item(i);
			        	Element item = (Element) userNode;
			        	
			        	userVals = parseTags(tagNames, item);
			        	
			        	//clean the user description
			        	String clnDesc = HooUtil.perfText(userVals.get(4));
			        	//return the user's description
			        	userVals.set(4, clnDesc);

			            }
			        
					return userVals;			           
	}

	//parsing the friendship file into csv for loading
	public void parseTempFF(){
		
	}
	
	 //all this method does is take an array of tags and return an array of values
	 public ArrayList<String> parseTags(ArrayList<String> tags, Element item){
		
		ArrayList<String> nodeVals = new ArrayList<String>();
		
		for(String tag : tags){				
			//for each tag in the tags array
			Node toParse = HooUtil.elemCheck(tag, item);
			String nodeVal = HooUtil.nodeCheck(toParse);	
			nodeVals.add(nodeVal);
		}
		
		return nodeVals;
				
	}
	
	//pulling RT @ from the tweet text
	//(\bRT\s@\w+\b) this is the regex for rt so far
	
	//using a regular expression to pull links from the tweets
	public String linkScrape(String txt){
		
		ArrayList<String> links = new ArrayList<String>();
		String link = null;
		 
			String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(txt);
			
				while(m.find()) {
					String urlStr = m.group();
						if (urlStr.startsWith("(") && urlStr.endsWith(")"))
							{
								urlStr = urlStr.substring(1, urlStr.length() - 1);
							}
						link = urlStr;						
					}
				//the links go to a listarray
				if (link != null)
					links.add(link);  
				else 
					link = ("null");
				
				//if there's more than one, they'll concat into a string
				if(links.size() < 1){
					StringBuilder linkStr = new StringBuilder();
						for( String s : links){
							linkStr.append(s);
							linkStr.append(" ");
						}
					return linkStr.toString();
				}
				
				else 
					return link;
	}
	
}
