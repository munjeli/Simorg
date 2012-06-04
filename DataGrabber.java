import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
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
	File destFile;
	int qcount;
	
	public void getRuserHx() throws ParserConfigurationException, SAXException, IOException{
		
		
			 System.out.println("Getting user status history...");
			 String filex = MyIds.rootUser + "Hx.xml";
		     String https_url = "https://twitter.com/statuses/user_timeline.xml?include_entities=true&include_rt=true&screen_name=" + MyIds.rootUser + "&count=100";
		     makeConnection(https_url, filex);
		     System.out.println("Finished downloading user status history.");
		     
		     File filename = new File(MyIds.hoopoeData + "/" + MyIds.rootUser + "Hx.xml");
		     
		     String check = HooUtil.nodeValue(filename, "status", "id", 0);
		     System.out.println(check);
	     
	  }
	
	//this is an all-purpose method to get general information about users50
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
	     System.out.println("Finished downloading followers.");	
	}
	
	//Friends are the people the root is following...
	public void getFriends(String usr){
		 System.out.println("Getting friends...");
		 String filex = usr + "TempFriends.xml";	 
	     String https_url = "https://api.twitter.com/1/friends/ids.xml?cursor=-1&screen_name=" + usr;
	     makeConnection(https_url, filex);       
	     System.out.println("Finished downloading friends.");
	}
	
	//get the boolean friendship value from the followers file first
	public void followers_F() throws ParserConfigurationException, SAXException, IOException {
		String tempFile = MyIds.hoopoeData + "/" + MyIds.rootUser; 
		
			try {
				Friendship FF = new Friendship();
				FF.getFriendships(tempFile + "TempFollowers.xml", MyIds.rootUser);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		//File F_Followers = new File(tempFile + "FFollowers.xml");
		//destFile.renameTo(F_Followers);		
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
				  
			      destFile = new File("/" + filex);
			      PrintWriter out = new PrintWriter(new FileWriter(MyIds.hoopoeData + destFile, true));	
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
	
	
	  	

   
	

