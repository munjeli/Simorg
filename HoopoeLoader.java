import java.util.*;
import java.io.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
/*  Hoopoe is a utility I'm building to work with a data visualization 
 *  application for twitter. This utility takes a single twitter user and scrapes 
 *  a tree of relational content with the user as the root node.  
 */

public class HoopoeLoader {
		
	public static void main(String[] args) {
			
	}
	
	public void setUp(){		
		SetUp s = new SetUp();
		s.makeHDir();	
	}
	
	//Get the name of the user who will be the root of the tree
	public void chooseUser(String userName){		
		MyIds.rootUser = userName;		
	}
	
	public void choose_ruid(String user_id){
		MyIds.rootUser_id = user_id;
	}
	
	public void userData(String rootUser) throws ParserConfigurationException, SAXException, IOException, InterruptedException{
		
		DataGrabber h = new DataGrabber();
		//user status history from twitter
		h.getRuserHx();
		/*h.getUserInfo(rootUser);
		h.getFollowers(rootUser);
		h.getFriends(rootUser);*/
		//h.followers_F();		
	}
	
	public String getUser(){			
		return MyIds.rootUser;
	}
	
	/*public void parseData() throws ParserConfigurationException, SAXException, IOException{
		String ruserInfo = "Info.xml";
		
		DataParser s = new DataParser();
		s.statusParser();
		//parse the rootUser info
		s.userParser(MyIds.rootUser, ruserInfo);
	}*/
	

}


