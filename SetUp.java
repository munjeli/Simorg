/* The point of this class is to check whether 
 * or not there is a folder and database existing
 * in the system to support the data import. 
 * If not, the SetUp object will create them. 
 */

import java.util.*;
import java.io.*;

public class SetUp {
	
	/*set up a directory for the temp files to download*/
	public void makeHDir(){
					
		File hData = new File(idS.hoopoeData);
		
		try
		{
			if (hData.exists())
				System.out.println("Tempfile directory exists.");
			else if(!hData.exists()){
				hData.mkdir();
				System.out.println("Tempfile directory created.");
			}
			else
				System.out.println("Directory cannot be created.");
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*set up a database with the rootuser's name for the data*/
	public void makeDB(){
		String dbName = idS.rootUser + "_db";
	}

}
