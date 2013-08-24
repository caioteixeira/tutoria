import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class Classificador {
	public static void main(String[] args) {
		//Cria instancias de treino e teste.
		
		Instances treino = inicializaInstancias("Arffs/BaseAprendizado.arff");
		Instances teste = inicializaInstancias("Arffs/BaseTeste.arff");
		if(treino == null||teste == null )
		{
			System.out.println("Instancias n�o foram inicializadas, abortando programa");
			return;
		}
		//Remove atributo ReferenceType
		treino.deleteAttributeAt(16);
		teste.deleteAttributeAt(16);
		
		//Define atributo minimal como Index
		treino.setClassIndex(treino.numAttributes() - 1);
		teste.setClassIndex(teste.numAttributes() - 1);
		
		//Define Classificador J48
		J48 classificador = new J48();
		
		try {
			classificador.buildClassifier(treino);
			// evaluate classifier and print some statistics
			 Evaluation eval = new Evaluation(treino);
			 
			 eval.evaluateModel(classificador, teste);
			 System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
			
			
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
			//Encerra o programa caso n�o encontre arquivos de base
			System.out.println("Arquivo(s) de base n�o foram encontrados/Erro de leitura de arquivos");
			e.printStackTrace();
			
			return null;
		}
		return instancia;
		
	}

}
