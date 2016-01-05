package DeepLearning;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class twoDtoXML {

	public static void main(String[] args) 
	{

	}


	public static void XMLify(String fileName, int hidden_layer_number, double[][] w, Document doc, Element layernumber) throws ParserConfigurationException, TransformerException 
	{
		for(int rows=0; rows<w.length; rows++)
		{
			Element nextlayer = doc.createElement("nextlayer");
			nextlayer.setAttribute("No",Integer.toString(rows));
			layernumber.appendChild(nextlayer);
			for(int cols=0; cols<w[0].length;cols++)
			{
				Element prevlayer = doc.createElement("prevlayer");
				prevlayer.appendChild(doc.createTextNode(Double.toString(w[rows][cols])));
				prevlayer.setAttribute("No",Integer.toString(cols));
				nextlayer.appendChild(prevlayer);
				System.out.print(w[rows][cols]+"\t");
			}
			System.out.println();

		}
		System.out.println("=======================================================");
		
	}

}

