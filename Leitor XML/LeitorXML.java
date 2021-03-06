import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LeitorXML {
	
	//"Lista" de contextos que devem ser considerados, exclui-se os fillers
	private static String[] contextosIncluidos = {"abs1","abs3","abs5","proj1","proj3","proj5"};
	
	/*Armazena a tabela com os valores fixos dos contextos
		Inicializa-se com o m�todo inicializaTabelaContextos, passando como parametro o array de contextos incluidos
		
		Ap�s inicializado, cada linha armazena:
		posi��o	conteudo
		0		id
		1		tipo do target
		2		quantidade de objetos do contexto
		3		quantidade de objetos com o mesmo tipo do target
		4		vPos do target
		5		Quantidade de objetos com o mesmo vPos do target
		6		hPos do target
		7		Quantidade de objetos com o mesmo hPos do target
		8		Quantidade de objetos com o mesmo tipo do Landmark	
		9		Define tipo do context: 0=proj e 1=abs
	
	*/
	private static String[][] tabelaContextos;
	
	public static void main(String[] args){
		
		//Inicializa tabela de contextos
		tabelaContextos = inicializaTabelaContextos(contextosIncluidos);
		
		//Abre arquivo XML
		String nomeDoArquivo = "Entrada";
		if(args.length >=1)
		{
			nomeDoArquivo = args[0];
		}
		//Inicializa elementos de escrita
		
		FileWriter fileWriter;
		File saidaArquivo = new File("saida.arff");
		File arquivoXML = new File(nomeDoArquivo);
		
		for(int i = 0; i<64; i++)
		{
			arquivoXML = new File(nomeDoArquivo+"/trial"+(i+1)+".xml");
			
			try {
				fileWriter = new FileWriter(saidaArquivo, true);
				
				//Processa o arquivo XML dos trials e escreve no arquivo de saida
				processaXML(arquivoXML, fileWriter);
				
				//Fecha arquivo de saida
				fileWriter.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/*
	 * Inicializa tabela dos contextos, excluindo os contextos que n�o est�o na lista
	 */
	public static String[][] inicializaTabelaContextos(String[] contextosIncluidos)
	{
		String[][] tabelaSaida = new String[contextosIncluidos.length][10];
		for(int i = 0; i < contextosIncluidos.length; i++)
		{
			Context context = new Context(contextosIncluidos[i]);
			tabelaSaida[i][0] = contextosIncluidos[i]; //Define ID do contexto
			tabelaSaida[i][1] = context.getTipoTarget(); //Define tipo do Target;
			tabelaSaida[i][2] = String.valueOf(context.getQuantObjetos()); //Define quantidade de Objetos
			tabelaSaida[i][3] = String.valueOf(context.getQuantObjetosTipoTarget()); //Define quantidade de Objetos do tipo do Target
			tabelaSaida[i][4] = context.getVPosTarget();
			tabelaSaida[i][5] = String.valueOf(context.getQuantObjetosVPosTarget());
			tabelaSaida[i][6] = context.getHPosTarget();
			tabelaSaida[i][7] = String.valueOf(context.getQuantObjetosHPosTarget());
			tabelaSaida[i][8] = String.valueOf(context.getQuantObjetosTipoLandmark());
			tabelaSaida[i][9] = String.valueOf(context.getContextType());
		}
		return tabelaSaida;
	}
	/*
	 * Processa o XML, percorre os trials
	 */
	public static void processaXML(File arquivoXML, FileWriter fileWriter)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		//Nota: Blocos Try/Catch s�o obrigat�rios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			try {
				//Leitura do XML
				//Inicializa PrintWriter
				PrintWriter printWriter = new PrintWriter(fileWriter);
				
				
				//Define estrutura Document lendo XML
				Document doc = db.parse(arquivoXML);
				//Define elemento raiz
				Element raiz = doc.getDocumentElement();
				
				//Define o nome do Trial
				String trial = raiz.getAttribute("ID");
				
				//Cria uma lista dos trials
				NodeList listaContexts = raiz.getElementsByTagName("CONTEXT");
				
				//Percorre a lista dos trials e imprime os parametros no arquivo
				for(int i = 0; i < listaContexts.getLength(); i++)
				{
					Element elemento = (Element) listaContexts.item(i);
					
					escreveContexto(printWriter, elemento, trial);
					
						
				}
				
				//Fecha o printWriter
				printWriter.close();
				
			} 
			catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Escreve paremetros do trial no arquivo de saida
	 */
	public static void escreveContexto(PrintWriter printWriter, Element elemento, String trial)
	{
		String id = elemento.getAttribute("ID");
		int i = contextEstaIncluido(id);
		if(i == -1) //Retorna se o contexto for filler
			return;
		printWriter.print(trial); //Imprime o nome do trial
		printWriter.print(",");
		//printWriter.print(id); //Imprime o id
		//printWriter.print(",");
		//ATRIBUTOS FIXOS
		printWriter.print(tabelaContextos[i][1]); //Imprime o tipo do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][2]); //Imprime o numero de objetos do Contexto
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][3]); //Imprime o numero de objetos com o mesmo tipo do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][4]); //Imprime o vPos do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][5]); //Imprime o numero de objetos com o mesmo vPos do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][6]); //Imprime o hPos do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][7]); //Imprime o numero de objetos com o mesmo hPos do target
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][8]); //Imprime o numero de objetos com o mesmo tipo do Landmark
		printWriter.print(",");
		printWriter.print(tabelaContextos[i][9]); //Imprime o numero de objetos com o mesmo tipo do Landmark
		printWriter.print(",");
		//ATRIBUTOS DO TRIAL
		printWriter.print(elemento.getAttribute("LENGTH")); //Imprime o atributo Length do trial
		printWriter.print(",");
		printWriter.print(contemParametro(elemento,"others")); //Imprime se usou others;
		printWriter.print(",");
		printWriter.print(contemPosition(elemento)); //Imprime se usou position
		printWriter.print(",");
		printWriter.print(contemParametro(elemento,"next")); //Imprime se usou next
		printWriter.print(",");
		printWriter.print(contemParametro(elemento,"left")); //Imprime se usou left
		printWriter.print(",");
		printWriter.print(contemParametro(elemento,"right")); //Imprime se usou right
		printWriter.print(",");
		printWriter.print(elemento.getAttribute("REFERENCE-TYPE")); //Imprime REFERENCE-TYPE
		printWriter.print(",");
		printWriter.print(elemento.getAttribute("MINIMAL"));
		
		printWriter.print("\n"); //Pula uma linha
	}
	
	///M�TODOS DOS PARAMETROS
	
	/*
	 * Verifica se o Context est� na lista de Contexts incluidos e retorna sua posi��o, se n�o retorna -1;
	 */
	static int contextEstaIncluido(String id, String[] contextsIncluidos)
	{
		for (int i = 0; i < contextsIncluidos.length; i++) {
			if(contextsIncluidos[i].equals(id))
				return i;
		}
		return -1;
	}
	static int contextEstaIncluido(String id)
	{
		return contextEstaIncluido(id, contextosIncluidos);
	}
	
	/*
	 * Verifica se o trial usou determinado parametro (Ex: Next, Other...)
	 * Retorna 1 se verdadeiro e 0 se falso.
	 */
	static int contemParametro(Element elemento, String parametro)
	{
		//Define o elemento de tag ATTRIBUTE-SET
		Element raizAtributos = (Element)elemento.getElementsByTagName("ATTRIBUTE-SET").item(0); 
		
		NodeList listaAtributos = raizAtributos.getElementsByTagName("ATTRIBUTE");
		//Percorre atributos
		for(int j = 0; j < listaAtributos.getLength(); j++)
		{
			//Verifica se h� atributo colour declarado, se n�o retorna valor padr�o "white"
			Element atributo = (Element) listaAtributos.item(j);
			if(atributo.getAttribute("NAME").equals(parametro))
			{
				return 1;
			}
		}
		return 0;
	}
	static int contemPosition(Element elemento)
	{
		if(contemParametro(elemento,"vpos") == 1 || contemParametro(elemento,"hpos") == 1)
			return 1;
		else
			return 0;
	}
}


