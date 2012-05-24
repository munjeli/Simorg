/* The point of this class is to check whether 
 * or not there is a folder and database existing
 * in the system to support the data import. 
 * If not, the SetUp object will create them. 
 */

import java.util.*;
import java.io.*;

public class SetUp {
	
	public void makeHDir(String myHome){
					
		File hData = new File(myHome + "/Documents/HoopoeData");
		
		try
		{
			if(hData.mkdir())
			System.out.println("Directory Created");		
			else
			System.out.println("Directory already exists or cannot be created");
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
