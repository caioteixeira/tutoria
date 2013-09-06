import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class Classificador {
	Classifier classificador;
	
	
	Classificador(){
		try {
			//Carrega modelo gerado no Weka
			classificador = (Classifier) weka.core.SerializationHelper.read("classificador.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean underspecify(int contextsize, int samevpos, String hpos, int samehpos, int sametypelandmark, Boolean abs)
	{
		Instance inst = new Instance(6);
		
		
		
		
		return abs;	
	}
	
	public void testa(){
		Instances teste = inicializaInstancias("training.arff");
		teste.setClassIndex(teste.numAttributes() - 1);
		
		try {
			Evaluation eTest = new Evaluation(teste);
			eTest.evaluateModel(classificador, teste);
			
			System.out.println(eTest.toSummaryString("\nResultados\n======\n", false));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			double a = classificador.classifyInstance(teste.instance(7));
			System.out.println(teste.classAttribute().value((int) a));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static Instances inicializaInstancias(String a)
	{
		BufferedReader br;
		Instances instancia;
		try {
			br = new BufferedReader(new FileReader(a));
			instancia = new Instances(br);
			br.close();
			
		} catch (IOException e) {
			//Retorna null caso n�o encontre arquivos de base, avisa em caso de erro
			System.out.println("Arquivo(s) de base n�o foram encontrados/Erro de leitura de arquivos");
			e.printStackTrace();
			
			return null;
		}
		return instancia;
		
	}
	
}
