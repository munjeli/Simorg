import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class HooUtil {
		
	//handling null reference in xml
	public static Node elemCheck(String tag, Element item){
		if(item.getElementsByTagName(tag).item(0) != null){
			return item.getElementsByTagName(tag).item(0);
		}
		else return null;	
	}
	
	//handling null reference in xml
	public static String nodeCheck(Node ele){
		if(ele.getFirstChild() != null){
			return ele.getFirstChild().getNodeValue();
		}
		else return "null";
	}
	
	public static String nodeNoChCheck(Node ele){
		if(ele != null)
			return ele.getNodeValue();
		else return "null";
					
	}
		
	//fixing the line breaks in the text string
	public static String perfText(String inputStr) {		
		String adjusted = inputStr.replaceAll("\\r?\\n?\\t?", "");
		return adjusted;
	}
	
	public static int nodeCount(File filename, String tag) throws SAXException, IOException, ParserConfigurationException{
		int nodecnt = 0;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(filename);
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName(tag);
		
		nodecnt = nodes.getLength();
		return nodecnt;
		
	}
	
	//call the value of the last instance of a nodelist to grab a tag
	//value for working the timeline
	public static String nodeValue(File filename, String tag, String childtag, int i) throws ParserConfigurationException, SAXException, IOException{
				
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(filename);
		doc.getDocumentElement().normalize();
		
		NodeList nodes = doc.getElementsByTagName(tag);
		Element toparse = (Element)nodes.item(i);
		Node parsednode = elemCheck(childtag, toparse);
		String parsedval = nodeCheck(parsednode);
					
		return parsedval;
		
	}

}
