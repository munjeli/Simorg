import java.util.*;
import java.io.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
/*  Hoopoe is a utility I'm building to work with a data visualization 
 *  application for twitter. This utility takes a single twitter user and scrapes 
 *  a tree of relational content with the user as the root node. 
 *  My first iteration of Hoopoe is not pure Java; rather I'm using Java to connect
 *  to the Unix cURL command to get data along with the twitter api. I will build 
 *  a pure java version of Hoopoe eventually, and even integrate it with the data viz 
 *  app, but for now, I just need automation to handle the data and build a model for the 
 *  the viz app. Java is used for the parser and I/O between MySQL and the data sources.  
 * 
 */

public class HoopoeLoader {
	
	
	public static void main(String[] args) {
		
		SetUp s = new SetUp();
		s.makeHDir(idS.myHome);
				
	}
	
	//Get the name of the user who will be the root of the tree
	public void chooseUser(String userName){
		
		idS.rootUser = userName;		
	}
	
	public void userData(String rootUser){
		
		DataGrabber h = new DataGrabber();
		//user status history from twitter
		h.getRuserHx();
	}
	
	public String getUser(){	
		
		return idS.rootUser;
	}
	
	public void parseData() throws ParserConfigurationException, SAXException, IOException{
		DataParser s = new DataParser();
		s.statusParser();
	}
	

}


