import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.*; 

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//This class is for the queries to twitter's servers
//Each query produces a temp file for the parser to format
public class DataGrabber {
	
/* The first call is to twitter's servers to download
 * the user's tweets as xml. The xml comes in messy: twitter
 * only allows a page of tweets at a time, so there are sequential calls
 * and lots of redundant headers. Twitter only caches 3200 tweets for any given
 * user. 
 */
	
	public void getRuserHx(){
		 System.out.println("Getting user status history...");
		 String filex = idS.rootUser + "Hx.xml";
	     String https_url = "https://twitter.com/statuses/user_timeline.xml?include_entities=true&include_rt=true&screen_name=" + idS.rootUser + "&count=5";
	     makeConnection(https_url, filex);
	     System.out.println("Finished downloading user status history.");
	     
	  }
	
	//this is an all-purpose method to get general information about users
	public void getUserInfo(String usr){
		 System.out.println("Getting user information...");
		 String filex = usr + "Info.xml";
	     String https_url = "https://api.twitter.com/1/users/show.xml?screen_name=" + usr;
	     makeConnection(https_url, filex);
	     System.out.println("Finished downloading user information.");
	}
	
	//the id_number of people who follow the root
	public void getFollowers(String usr){
		 System.out.println("Getting followers...");
		 String filex = usr + "TempFollowers.xml";
	     String https_url = "https://api.twitter.com/1/followers/ids.xml?cursor=-1&screen_name=" + usr;	 
	     makeConnection(https_url, filex);  
	     System.out.println("Finished downloading follower ids.");	
	}
	
	//Friends are the people the root is following...
	public void getFriends(String usr){
		 System.out.println("Getting friends...");
		 String filex = usr + "TempFriends.xml";	 
	     String https_url = "https://api.twitter.com/1/friends/ids.xml?cursor=-1&screen_name=" + usr;
	     makeConnection(https_url, filex);       
	     System.out.println("Finished downloading friend ids.");
	}
	
	//Friendships are a boolean value describing two-way follows
	//up to 100 ids can be queried at a time
	public void getFriendships(String filename, String usr) throws ParserConfigurationException, SAXException, IOException{
		
		File friendsrc = new File(filename);
		DocumentBuilderFactory friendfac = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder friendbdr = friendfac.newDocumentBuilder();
		Document frienddoc = friendbdr.parse(friendsrc);
		ArrayList<String> friends = new ArrayList<String>(); 
		
		frienddoc.getDocumentElement().normalize();
		
		NodeList nodes = frienddoc.getElementsByTagName("ids");
		int fcount = nodes.getLength();
		int floop = fcount/100;
		
		//you can only load 100 names at a time to the friendship query
		for (int i = 0; i< 100; i++) {
        	Node id_node = nodes.item(i);
        	DataParser.item = (Element) id_node;
        	
        	Node id = DataParser.elemCheck("id");
        	String fid = DataParser.nodeCheck(id);
        	
        	friends.add(fid);
        	id.removeChild(id);
		}
		
		idParse(friends);
  	
		}
		
		private String idParse(ArrayList<String> friends){
		
			StringBuilder friendStr = new StringBuilder();
				for(String f : friends){
					friendStr.append(f);					
					friendStr.append(", ");
				}
			return friendStr.toString(); 
		}
		

		
	
	
	//connect with the input query
	public void makeConnection(String https_url, String filex){
	    URL url;
	    try {	  
		     url = new URL(https_url);
		     HttpsURLConnection con = (HttpsURLConnection)url.openConnection(); 	     
		 	 //dump all the content into an xml file
		 	 print_content(con, filex);
	 
	    } 
	    catch (MalformedURLException e) {
		     e.printStackTrace();
	    } 
	    catch (IOException e) {
		     e.printStackTrace();
	    }
		
	}
	
	//this method prints the incoming streams to xml temp files for parsing
	private void print_content(HttpsURLConnection con, String filex){
		if(con!=null){
		
			try {			
			   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				  
			      File destFile = new File("/" + filex);
			      PrintWriter out = new PrintWriter(idS.hoopoeData + destFile);
	
			   	   String input;		
				   while ((input = br.readLine()) != null){
				   out.println(input);
				   }
				
			   out.flush();
			   out.close();
			   br.close();	
			   
			} 
			catch (IOException e) {
			   e.printStackTrace();
			}	
	    }	
	}
	
	
	  	
}
   
	

