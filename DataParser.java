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
	
	static Element item;
	
	public void statusParser() throws ParserConfigurationException, SAXException, IOException{
		System.out.println("Beginning status history parse...");
		
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
		
		PrintWriter out = new PrintWriter(idS.myHome + "/Documents/HoopoeData/" +  idS.rootUser + "Status.csv");		
		String fileName = (idS.hoopoeData + "/" + idS.rootUser + "Hx.xml");
		File f = new File(fileName);
		Document doc = builder.parse(f);
		doc.getDocumentElement().normalize();
		
		//work within each status update; get the tags return the values
		NodeList nodes = doc.getElementsByTagName("status");
		int statusCount = nodes.getLength();
				
		        for (int i = 0; i< statusCount; i++) {
		        	Node statusNode = nodes.item(i);
		        	item = (Element) statusNode;
		        	
		        	ArrayList<String> statusVals = parseTags(tagNames);
		        	
		        	//clean the tweetText
		        	String clnTweet = perfText(statusVals.get(2));
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
		        System.out.println("Parse Finished");
				
	}
		
	public void parseUserInfo(){}
	
	public void parseFFF(){}
	
	 //all this method does is take an array of tags and return an array of values
	 public ArrayList<String> parseTags(ArrayList<String> tags){
		
		ArrayList<String> nodeVals = new ArrayList<String>();
		
		for(String tag : tags){				
			//for each tag in the tags array
			Node toParse = elemCheck(tag);
			String nodeVal = nodeCheck(toParse);	
			nodeVals.add(nodeVal);
		}
		
		return nodeVals;
				
	}
		
	//handling null reference in xml
	public static Node elemCheck(String tag){
		if(item.getElementsByTagName(tag).item(0) != null){
			return item.getElementsByTagName(tag).item(0);
		}
		else return null;	
	}
	
	//handling null reference in xml
	public static String nodeCheck(Node ele){
		if(ele.getFirstChild() != null){
			return ele.getFirstChild().getNodeValue();
		}
		else return "null";
	}
	
	//fixing the line breaks in the text string
	public static String perfText(String inputStr) {		
		String adjusted = inputStr.replaceAll("\\r?\\n", "");
		return adjusted;
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
