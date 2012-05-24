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
	
	Element item;
	
	public void statusParser() throws ParserConfigurationException, SAXException, IOException{
		System.out.println("Beginning status history parse...");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		String fileName = (idS.hoopoeData + "/" + idS.rootUser + "Hx.xml");
		File f = new File(fileName);
		Document doc = builder.parse(f);
		doc.getDocumentElement().normalize();
		
		PrintWriter out = new PrintWriter(idS.myHome + "/Documents/HoopoeData/" +  idS.rootUser + "StatusHx.csv");
		
		NodeList nodes = doc.getElementsByTagName("status");
		int statusCount = nodes.getLength();
				
		        for (int i = 0; i< statusCount; i++) {
		        	Node statusNode = nodes.item(i);
		        	item = (Element) statusNode;
	        	
		        	Node createdAtNode = elemCheck("created_at");
		        	String createdAt = nodeCheck(createdAtNode);
		        	
		        	Node idNode = elemCheck("id");
		        	String idTwee = nodeCheck(idNode);
		        	
		        	Node textNode = elemCheck("text");
		        	String text = perfText(nodeCheck(textNode));
		        			        	
		        	String linkTxt = linkScrape(text);
		        	
		        	Node retweetedNode = elemCheck("retweeted");
		        	String retweeted = nodeCheck(retweetedNode);
		        	
		        	Node retweetCountNode = elemCheck("retweet_count");
		        	String retweetCount = nodeCheck(retweetCountNode);
		        	
		        	Node inReplyToStatusIdNode = elemCheck("in_reply_to_status_id");
		        	String inReplyToStatusId = nodeCheck(inReplyToStatusIdNode);
		        	
		        	Node replyToUserIdNode = elemCheck("in_reply_to_user_id");
		        	String replyToUserId = nodeCheck(replyToUserIdNode);
		        	
		        	Node replyToScrNameNode = elemCheck("in_reply_to_screen_name");
		        	String replyToScrName = nodeCheck(replyToScrNameNode);
		        	
		        	Node favoritedNode = elemCheck("favorited");
		        	String favorited = nodeCheck(favoritedNode);
		        	
		        	Node geoNode = elemCheck("geo");
		        	String geo = nodeCheck(geoNode);
		        	
		        	Node placeNode = elemCheck("place");
		        	String place = nodeCheck(placeNode);
		        	
		        	Node coordinatesNode = elemCheck("coordinates");
		        	String coordinates = nodeCheck(coordinatesNode);
		        	
		        	Node sourceNode = elemCheck("source");
		        	String source = nodeCheck(sourceNode);
		        	
		        	out.println(createdAt + "\t" + idTwee + "\t" + text + "\t" + linkTxt + "\t" + retweeted + "\t" + retweetCount + "\t" + inReplyToStatusId + "\t" + replyToUserId + "\t" + replyToScrName + "\t" + favorited + "\t" + geo + "\t" + place + "\t" + coordinates + "\t" + source);

		        }
		        
		        out.close();
		        System.out.println("Parse Finished");
				
	}
	
	//handling null reference in xml
	public Node elemCheck(String tag){
		if(item.getElementsByTagName(tag).item(0) != null){
			return item.getElementsByTagName(tag).item(0);
		}
		else return null;	
	}
	
	//handling null reference in xml
	public String nodeCheck(Node ele){
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
				
				if (link != null)
					return link;
				else 
					return "null";
	}
		

}
