package DeepLearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class modelReaderWritter {
	public static void main(String[] args) throws IOException 
	{
		//DeepLearning.modelReaderWritter.reader("model.txt");
		//DeepLearning.modelReaderWritter.biasReader("bias.txt");
		//DeepLearning.modelReaderWritter.layerCounter("model.txt");
		//		DeepLearning.modelReaderWritter.currentLayerPreceptron("model.txt", 0);
		//		DeepLearning.modelReaderWritter.prevLayerPreceptron("model.txt", 0);
		//		DeepLearning.modelReaderWritter.getWeight("model.txt", 0, 1, 4);
		DeepLearning.modelReaderWritter.getBias("bias.txt", 1);
	}
	public static void writter(int hidden_layer_number, double[][] w, Formatter fmt) throws IOException
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
	public static void biasWritter(int hidden_layer_number, double[] b, Formatter biasfmt) throws IOException
	{
		//Close the output stream
		//output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
		biasfmt.format("%d\t%1.10f\n",hidden_layer_number,b[hidden_layer_number]);
		System.out.print(b[hidden_layer_number]+"\t");
	}
	public static void reader(String fileName)
	{
		BufferedReader br = null;
		try 
		{
			String sCurrentLine;
			Set<Integer> totalLayerNumber = new HashSet<Integer>();
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "(\\d+)\\t(\\d+)\\t(\\d+)\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					totalLayerNumber.add(layernumber);
					int nextlayer_perceptron=Integer.parseInt(m.group(2));
					int currentlayer_perceptron=Integer.parseInt(m.group(3));
					double weight=Double.parseDouble(m.group(4));
					System.out.println(layernumber+"\t"+nextlayer_perceptron+"\t"+currentlayer_perceptron+"\t"+weight);

				} else {
					System.out.println("Patterns are not maching the model files signature");
				}	

			}
			System.out.println("totalLayerNumber  == "+totalLayerNumber.size());

		} 
		catch (IOException e) 
		{
			System.out.println("Model file not found or unreadable");
			e.printStackTrace();
		}

	}
	public static int layerCounter(String fileName)
	{
		BufferedReader br = null;
		Set<Integer> totalLayerNumber = new HashSet<Integer>();
		try 
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "(\\d+)\\t(\\d+)\\t(\\d+)\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					totalLayerNumber.add(layernumber);

				} else {
					System.out.println("Patterns are not maching the model files signature");
				}	

			}
			System.out.println("totalLayerNumber  == "+totalLayerNumber.size());

		} 
		catch (IOException e) 
		{
			System.out.println("Model file not found or unreadable");
			e.printStackTrace();
		}

		return totalLayerNumber.size();

	}
	public static void biasReader(String fileName)
	{
		BufferedReader br = null;
		try 
		{
			String sCurrentLine;
			Set<Integer> totalLayerNumber = new HashSet<Integer>();
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "(\\d+)\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					totalLayerNumber.add(layernumber);
					double bias=Double.parseDouble(m.group(2));
					System.out.println(layernumber+"\t"+bias);

				} else {
					System.out.println("Patterns are not maching the bias files signature");
				}	

			}
			System.out.println("totalLayerNumber  == "+totalLayerNumber.size());

		} 
		catch (IOException e) 
		{
			System.out.println("Patterns are not maching the bias files signature");
			e.printStackTrace();
		}

	}
	public static int currentLayerPreceptron(String fileName,int LayerNumber)
	{
		BufferedReader br = null;
		Set<Integer> currentLayerPerceptron = new HashSet<Integer>();
		try 
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "("+LayerNumber+")\\t(\\d+)\\t(\\d+)\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					int currentlayer_perceptron=Integer.parseInt(m.group(2));	
					currentLayerPerceptron.add(currentlayer_perceptron);

				} 
			}
			System.out.println("perceptron in current layer  == "+currentLayerPerceptron.size());

		} 
		catch (IOException e) 
		{
			System.out.println("failed to read current layer perceptron from the file");
			e.printStackTrace();
		}
		return currentLayerPerceptron.size();
	}
	public static int prevLayerPreceptron(String fileName,int LayerNumber)
	{
		BufferedReader br = null;
		Set<Integer> prevLayerPerceptron = new HashSet<Integer>();
		try 
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "("+LayerNumber+")\\t(\\d+)\\t(\\d+)\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					int layernumber=Integer.parseInt(m.group(1));
					int prevlayer_perceptron=Integer.parseInt(m.group(3));	
					prevLayerPerceptron.add(prevlayer_perceptron);

				} 
			}
			System.out.println("perceptron in previous layer  == "+prevLayerPerceptron.size());

		} 
		catch (IOException e) 
		{
			System.out.println("failed to read previous layer perceptron from the file");
			e.printStackTrace();
		}
		return prevLayerPerceptron.size();
	}
	public static double getWeight(String fileName,int LayerNumber,int currentLayer, int prevLayer)
	{
		BufferedReader br = null;
		double weightFromFile = 0;
		try 
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "("+LayerNumber+")\\t("+currentLayer+")\\t("+prevLayer+")\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) 
				{
					weightFromFile=Double.parseDouble(m.group(4));	
				}
			}
			System.out.println("perceptron in previous layer  == "+ weightFromFile);

		} 
		catch (IOException e) 
		{
			System.out.println("failed to read previous layer perceptron from the file");
			e.printStackTrace();
		}
		return weightFromFile;
	}
	public static double getBias(String fileName,int LayerNumber)
	{
		BufferedReader br = null;
		double bias = 0;
		try 
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				////output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
				//System.out.println(sCurrentLine);
				String pattern = "("+LayerNumber+")\\t([+-]*\\d+.\\d+)";
				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = r.matcher(sCurrentLine);
				if (m.find( )) {
					bias=Double.parseDouble(m.group(2));
					System.out.println(LayerNumber+" bias output\t"+bias);
				}
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Patterns are not maching the bias files signature");
			e.printStackTrace();
		}

		return bias;
	}
}
