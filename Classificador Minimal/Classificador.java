import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import java.io.BufferedReader;
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
		treino.deleteAttributeAt(15);
		teste.deleteAttributeAt(15);
		
		//Define atributo minimal como Index
		treino.setClassIndex(treino.numAttributes() - 1);
		teste.setClassIndex(teste.numAttributes() - 1);
		
		//Define Classificador J48
		J48 classificador = new J48();
		
		
		try {
			//Treina classificador com base no arquivo Arff de treino
			classificador.buildClassifier(treino);
			
			
			//Avalia o classificador com base no arquivo Arff de teste e imprime estatisticas
			Evaluation eval = new Evaluation(treino);
			eval.evaluateModel(classificador, teste);
			System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	//Inicializa instancias com base em arquivos .ARFF
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
