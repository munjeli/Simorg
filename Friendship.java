import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* The methods for aggregating data for friendship in the DataGrabber 
 * get so messy that I've moved them to a separate class.
 */
public class Friendship {
	
	//Friendships are a boolean value describing two-way follows
	public void getFriendships(String filename, String usr) throws ParserConfigurationException, SAXException, IOException, InterruptedException{
		System.out.println("Assembling friendship file...");
		
		//the user id needs to be generalized so I can use the method for
		//any user passed, but I need to figure out when to parse it from userInfo
		String usrid = MyIds.rootUser_id;		
		File friendsrc = new File(filename);
		DocumentBuilderFactory friendfac = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder friendbdr = friendfac.newDocumentBuilder();
		Document frienddoc = friendbdr.parse(friendsrc);
		
		ArrayList<String> friendships = new ArrayList<String>();
		
		frienddoc.getDocumentElement().normalize();
		
		NodeList nodes = frienddoc.getElementsByTagName("id");
		int fcount = nodes.getLength();	
		System.out.print(fcount);
				
			for(int i = 0; i < fcount; i++){
				
				String fid = null;
				Element ele = (Element)nodes.item(i);
				fid = HooUtil.nodeCheck(ele);
		        	
		        friendships.add(fid);
		        ele.removeChild(ele);
	
			}
				
		String filex = usr + "TempFriendship.xml";		
		Iterator<String> ffit = friendships.iterator(); 
		
			while(ffit.hasNext()){
				String ffid = ffit.next();	
			    String https_url = "https://api.twitter.com/1/friendships/exists.xml?user_id_a=" + usrid + "&user_id_b=" + ffid;
			    		    
			    makeFFConnection(https_url, filex, ffid);
			
		}
		
	     System.out.println("Evaluating for friendships...");
	}
		
	//connect with the input query
	public void makeFFConnection(String https_url, String filex, String id){
	    URL url;
	    File destFile = new File(MyIds.hoopoeData + "/" + filex);
	    
	    try {	  
		     url = new URL(https_url);
		     HttpsURLConnection con = (HttpsURLConnection)url.openConnection(); 	     
		 	 //dump all the content into an xml file
		 	 print_plus(con, destFile, id);
	 
	    } 
	    catch (MalformedURLException e) {
		     e.printStackTrace();
	    } 
	    catch (IOException e) {
		     e.printStackTrace();
	    }
		
	}
	
	//printing the results of each query with its id attached into a 
	//parsable xml document
	private void print_plus(HttpsURLConnection con, File destFile, String id){
		if(con!=null){
			
			try {			
			   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				  
			      PrintWriter out = new PrintWriter(new FileWriter(destFile, true));	
			   	  String input;		
				  while ((input = br.readLine()) != null){
				  out.println("<fq>\n" + input + "\n" + "<id>" + id + "</id>\n</fq>");
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
	
	//building an xml file for two-way results: 'true' friends
	public void trueFriends(){
		
	}
}
