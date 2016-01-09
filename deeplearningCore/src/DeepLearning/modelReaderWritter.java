package DeepLearning;

import java.io.IOException;
import java.util.Formatter;

public class modelReaderWritter {
	public static void writter(String fileName,int hidden_layer_number, double[][] w, Formatter fmt) throws IOException
	{
			  //Close the output stream
			  for(int rows=0; rows<w.length; rows++)
				{
					for(int cols=0; cols<w[0].length;cols++)
					{
						//output is generated as layernumber, nextlayer_perceptron, currentlayer_perceptron, weight
						fmt.format("%3d\t%3d\t%3d\t%3f\n",hidden_layer_number,rows,cols,w[rows][cols]);
						System.out.print(w[rows][cols]+"\t");
					}
					System.out.println();

				}
				System.out.println("=======================================================");
	}

}
