import java.net.MalformedURLException;
import java.net.URL;
import java.io.*; 
import javax.net.ssl.HttpsURLConnection;

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
     String https_url = "https://twitter.com/statuses/user_timeline/" + idS.rootUser + ".xml?count=100&page=[1-32]";
     makeConnection(https_url, filex);
     System.out.println("Finished downloading user status history.");
     
  }

	public void getUserInfo(String usr){
		 System.out.println("Getting user information...");
		 String filex = usr + "Info.xml";
	     String https_url = "https://api.twitter.com/1/users/show.xml?screen_name=" + usr;
	     makeConnection(https_url, filex);
	     System.out.println("Finished downloading user information.");
	}
	
	public void getFollowers(String usr){
		 System.out.println("Getting followers...");
		 String filex = usr + "TempFollowers.xml";
	     String https_url = "https://api.twitter.com/1/followers/ids.xml?cursor=-1&screen_name=" + usr;	 
	     makeConnection(https_url, filex);  
	     System.out.println("Finished downloading follower ids.");	
	}
	
	public void getFriends(String usr){
		 System.out.println("Getting friends...");
		 String filex = usr + "TempFriends.xml";	 
	     String https_url = "https://api.twitter.com/1/friends/ids.xml?cursor=-1&screen_name=" + usr;
	     makeConnection(https_url, filex);       
	     System.out.println("Finished downloading friend ids.");
	}

public void getFriendships(){}

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
   
	

