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
	 
     String https_url = "https://twitter.com/statuses/user_timeline/" + idS.rootUser + ".xml?count=100&page=[1-32]";
     URL url;
     try {	  
	     url = new URL(https_url);
	     HttpsURLConnection con = (HttpsURLConnection)url.openConnection(); 
	     con.setRequestMethod("GET");
	     con.setReadTimeout(15*1000);
	     
	 	 //dump all the content into an xml file
	 	 print_content(con);
  
     } 
     catch (MalformedURLException e) {
 	     e.printStackTrace();
     } 
     catch (IOException e) {
 	     e.printStackTrace();
     }
       
     System.out.println("Finished downloading user status history.");
     
  }

	private void print_content(HttpsURLConnection con){
		if(con!=null){
	
		try {			
		   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			  
		      File userHx = new File("/" + idS.rootUser + "Hx.xml");
		      PrintWriter out = new PrintWriter(idS.hoopoeData + userHx);

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
   
	

