package DeepLearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class modelReaderWritter {
	public static void main(String[] args) throws IOException 
	{
		DeepLearning.modelReaderWritter.reader("model.txt");

	}
	public static void writter(String fileName,int hidden_layer_number, double[][] w, Formatter fmt) throws IOException
	{
		//Close the output stream
		for(int rows=0; rows<w.length; rows++)
		{
			for(int cols=0; cols<w[0].length;cols++)
			{
				//output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				fmt.format("%d\t%d\t%d\t%1.16f\n",hidden_layer_number,rows,cols,w[rows][cols]);
				//System.out.print(w[rows][cols]+"\t");
			}
			System.out.println();

		}
		System.out.println("=======================================================");
	}
	public static void reader(String fileName)
	{
		BufferedReader br = null;
		try 
		{
			String sCurrentLine;

			br = new BufferedReader(new FileReader(fileName));

			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "(\\d)+\\t(\\d)+\\t(\\d)+\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					int nextlayer_perceptron=Integer.parseInt(m.group(2));
					int currentlayer_perceptron=Integer.parseInt(m.group(3));
					double weight=Double.parseDouble(m.group(4));
					System.out.println(layernumber+"\t"+nextlayer_perceptron+"\t"+currentlayer_perceptron+"\t"+weight);
					
				} else {
					System.out.println("NO MATCH");
				}	

			}

		} 
		catch (IOException e) 
		{
			System.out.println("Model file not found or unreadable");
			e.printStackTrace();
		}

	}
}
