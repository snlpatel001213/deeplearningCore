package DeepLearning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static DeepLearning.utils.*;

public class DBN_modified {
	public int N;
	public int n_ins;
	public int[] hidden_layer_sizes;
	public int n_outs;
	public int n_layers;
	public HiddenLayerDiscrete[] sigmoid_layers;
	public RBM[] rbm_layers;
	public LogisticRegressionDiscrete log_layer;
	public Random rng;


	public DBN_modified(int N, int n_ins, int[] hidden_layer_sizes, int n_outs, int n_layers, Random rng) {
		int input_size;

		this.N = N;
		this.n_ins = n_ins;
		this.hidden_layer_sizes = hidden_layer_sizes;
		this.n_outs = n_outs;
		this.n_layers = n_layers;

		this.sigmoid_layers = new HiddenLayerDiscrete[n_layers];
		this.rbm_layers = new RBM[n_layers];

		if(rng == null)	this.rng = new Random(1234);
		else this.rng = rng;

		// construct multi-layer
		for(int i=0; i<this.n_layers; i++) {
			if(i == 0) {
				input_size = this.n_ins;
			} else {
				input_size = this.hidden_layer_sizes[i-1];
			}

			// construct sigmoid_layer
			this.sigmoid_layers[i] = new HiddenLayerDiscrete(this.N, input_size, this.hidden_layer_sizes[i], null, null, rng);

			// construct rbm_layer
			this.rbm_layers[i] = new RBM(this.N, input_size, this.hidden_layer_sizes[i], this.sigmoid_layers[i].W, this.sigmoid_layers[i].b, null, rng);
		}

		// layer for output using Logistic Regression
		this.log_layer = new LogisticRegressionDiscrete(this.N, this.hidden_layer_sizes[this.n_layers-1], this.n_outs);
	}

	public void pretrain(int[][] train_X, double lr, int k, int epochs) {
		int[] layer_input = new int[0];
		int prev_layer_input_size;
		int[] prev_layer_input;

		for(int i=0; i<n_layers; i++) {  // layer-wise
			for(int epoch=0; epoch<epochs; epoch++) {  // training epochs
				for(int n=0; n<N; n++) {  // input x1...xN
					// layer input
					for(int l=0; l<=i; l++) {

						if(l == 0) {
							layer_input = new int[n_ins];
							for(int j=0; j<n_ins; j++) layer_input[j] = train_X[n][j];
						} else {
							if(l == 1) prev_layer_input_size = n_ins;
							else prev_layer_input_size = hidden_layer_sizes[l-2];

							prev_layer_input = new int[prev_layer_input_size];
							for(int j=0; j<prev_layer_input_size; j++) prev_layer_input[j] = layer_input[j];

							layer_input = new int[hidden_layer_sizes[l-1]];

							sigmoid_layers[l-1].sample_h_given_v(prev_layer_input, layer_input);
						}
					}

					rbm_layers[i].contrastive_divergence(layer_input, lr, k);
				}
			}
		}
	}

	public void finetune(int[][] train_X, int[][] train_Y, double lr, int epochs) throws ParserConfigurationException, TransformerException {
		int[] layer_input = new int[0];
		// int prev_layer_input_size;
		int[] prev_layer_input = new int[0];
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("DeepBoltzmannNetwork");
		doc.appendChild(rootElement);

		for(int epoch=0; epoch<epochs; epoch++) {
			for(int n=0; n<N; n++) {

				// layer input
				for(int i=0; i<n_layers; i++) {
					if(i == 0) {
						prev_layer_input = new int[n_ins];
						for(int j=0; j<n_ins; j++) prev_layer_input[j] = train_X[n][j];
					} else {
						prev_layer_input = new int[hidden_layer_sizes[i-1]];
						for(int j=0; j<hidden_layer_sizes[i-1]; j++) prev_layer_input[j] = layer_input[j];
					}

					layer_input = new int[hidden_layer_sizes[i]];
					sigmoid_layers[i].sample_h_given_v(prev_layer_input, layer_input);
				}

				log_layer.train(layer_input, train_Y[n], lr);
			}
			// lr *= 0.95;


			//			//prints weights
			//			for(int hidden_layer_number=0;hidden_layer_number<sigmoid_layers.length;hidden_layer_number++)
			//			{
			//				System.out.println("sigmoid_layers[i] = " + sigmoid_layers[hidden_layer_number] );
			//				for(int rows=0; rows<sigmoid_layers[hidden_layer_number].W.length; rows++)
			//				{
			//					for(int cols=0; cols<sigmoid_layers[hidden_layer_number].W[0].length;cols++)
			//					{
			//						System.out.print(sigmoid_layers[hidden_layer_number].W[rows][cols] + "\t");
			//					}
			//					System.out.println();
			//				}
			//				System.out.println();
			//			}
			//			System.out.println("==========================================================");
		}

		Element weight = doc.createElement("weight");
		rootElement.appendChild(weight);
		
		// passing entire weight data to convert to XML
		for(int hidden_layer_number=0;hidden_layer_number<sigmoid_layers.length;hidden_layer_number++)
		{
			Element layernumber = doc.createElement("layernumber");
			weight.appendChild(layernumber);
			layernumber.setAttribute("No",Integer.toString(hidden_layer_number));
			try {
				DeepLearning.twoDtoXML.XMLify("model.xml",hidden_layer_number,sigmoid_layers[hidden_layer_number].W,doc,layernumber);
			} catch (ParserConfigurationException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//exporting bias
		Element bias = doc.createElement("bias");
		for(int hidden_layer_number=0;hidden_layer_number<sigmoid_layers.length;hidden_layer_number++)
		{
			Element LayerNumber = doc.createElement("layernumber");
			LayerNumber.appendChild(doc.createTextNode(Double.toString(sigmoid_layers[hidden_layer_number].b[hidden_layer_number])));
			LayerNumber.setAttribute("No",Integer.toString(hidden_layer_number));
			bias.appendChild(LayerNumber);
			System.out.println("bias = "+sigmoid_layers[hidden_layer_number].b[hidden_layer_number]);
		}
		rootElement.appendChild(bias);

		//Writting XML to file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("file.xml"));
		transformer.transform(source, result);
		System.out.println("File saved!");

	}


	public void predict(int[] x, double[] y) {
		double[] layer_input = new double[0];
		// int prev_layer_input_size;
		double[] prev_layer_input = new double[n_ins]; //n_ins = 6 number of input 
		for(int j=0; j<n_ins; j++) 
		{
			//System.out.println(x[j]); //{1, 1, 0, 0, 0, 0} test row
			prev_layer_input[j] = x[j];
		}

		double linear_output;


		// layer activation
		for(int i=0; i<n_layers; i++) //n_layers = 4 (number of hidden layers) 
		{
			layer_input = new double[sigmoid_layers[i].n_out];//

			for(int k=0; k<sigmoid_layers[i].n_out; k++) //sigmoid_layers[i].n_out give number of perceptron in each layer
			{
				//n_out number of perceptron in  next layer e.g. = 2
				//System.out.print("input to = "+sigmoid_layers[i].n_out);
				linear_output = 0.0;

				for(int j=0; j<sigmoid_layers[i].n_in; j++) 
				{
					//n_in number of perceptron in  prev layer e.g. = 6 
					//System.out.println("   output from  =" +sigmoid_layers[i].n_in);
					linear_output += sigmoid_layers[i].W[k][j]* prev_layer_input[j]; 
				}
				linear_output += sigmoid_layers[i].b[k];
				layer_input[k] = sigmoid(linear_output);
			}

			if(i < n_layers-1) {
				prev_layer_input = new double[sigmoid_layers[i].n_out];
				for(int j=0; j<sigmoid_layers[i].n_out; j++) prev_layer_input[j] = layer_input[j];
			}
		}

		for(int i=0; i<log_layer.n_out; i++) {
			y[i] = 0;
			for(int j=0; j<log_layer.n_in; j++) {
				y[i] += log_layer.W[i][j] * layer_input[j];
			}
			y[i] += log_layer.b[i];
		}


		log_layer.softmax(y);
	}

	private static void test_dbn() {
		Random rng = new Random(123);

		double pretrain_lr = 0.1;
		int pretraining_epochs = 1000;
		int k = 1;
		double finetune_lr = 0.1;
		int finetune_epochs = 500;

		int train_N = 6;
		int test_N = 4;
		int n_ins = 6;
		int n_outs = 3;
		int[] hidden_layer_sizes = {2,3,4,6};
		int n_layers = hidden_layer_sizes.length;

		// training data
		int[][] train_X = {
				{1, 1, 1, 0, 0, 0},
				{1, 0, 1, 0, 0, 0},
				{1, 1, 1, 0, 0, 0},
				{0, 0, 1, 1, 1, 0},
				{0, 0, 1, 1, 0, 0},
				{0, 0, 1, 1, 1, 0}
		};

		int[][] train_Y = {
				{1, 0, 0},
				{1, 0, 0},
				{1, 0, 0},
				{0, 0, 2},
				{0, 1, 0},
				{0, 0, 2},
		};


		// construct DNN.DBN
		DBN_modified dbn = new DBN_modified(train_N, n_ins, hidden_layer_sizes, n_outs, n_layers, rng);
		// pretrain
		dbn.pretrain(train_X, pretrain_lr, k, pretraining_epochs);

		// finetune
		try {
			dbn.finetune(train_X, train_Y, finetune_lr, finetune_epochs);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// test data
		int[][] test_X = {
				{1, 1, 0, 0, 0, 0},
				{1, 1, 1, 1, 0, 0},
				{0, 0, 0, 1, 1, 0},
				{0, 0, 1, 1, 1, 0},
		};

		double[][] test_Y = new double[test_N][n_outs]; //test_N=4*n_outs=3q

		// test
		for(int i=0; i<test_N; i++) {
			//test_X[i] taking test row wise {1, 1, 0, 0, 0, 0}
			//test_Y Output matrix size
			dbn.predict(test_X[i], test_Y[i]);

			for(int j=0; j<n_outs; j++) {
				//System.out.print(test_Y[i][j] + " ");
			}
			//System.out.println();
		}
	}

	public static void main(String[] args) {
		test_dbn();
	}
}
