import org.w3c.dom.Element;
import org.w3c.dom.Node;


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

}
