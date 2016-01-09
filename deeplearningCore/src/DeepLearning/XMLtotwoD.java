package DeepLearning;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLtotwoD {

	public static void main(String args[]) {
		try {

			File stocks = new File("file.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());
			NodeList DBN = doc.getElementsByTagName("weight");
			System.out.println("==========================");
			System.out.println(DBN.getLength());
			for (int i = 0; i < DBN.getLength(); i++) 
			{
				NodeList weightNode = DBN.item(0).getChildNodes();
				for (int j = 0; j < weightNode.getLength(); j++) 
				{
					Node layerno = weightNode.item(i);
					//System.out.println(layerno.getNodeName());
					if (layerno.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element element = (Element) layerno;
						
						System.out.println(element.getElementsByTagName("nextlayer").getLength());
						//System.out.println("Stock Symbol: " + getValue("layernumber", element));

					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
}
