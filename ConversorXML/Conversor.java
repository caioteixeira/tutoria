import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Conversor {
	public static void main(String[] args) {
		
		
		processaXML(new File("saida.xml"));

	}
	public static void processaXML(/*File XMLAtomic,File XMLRelation, */File arquivoXMLSaida)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch s�o obrigat�rios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			try {
				//Leitura do XML
				
				//Define estrutura Document dos XMLs de entrada
				/*Document docAtomic = db.parse(XMLAtomic);
				Document docRelation = db.parse(XMLRelation);
				*/
				
				//Define estrutura Document do XML de saida
				Document docSaida = db.newDocument();
				/*
				//Define elemento raiz 
				Element raizAtomic = docAtomic.getDocumentElement();
				Element raizRelation = docRelation.getDocumentElement();
				*/
				
				//Verifica se h� um elemento Domain, se n�o o cria
				Element raizSaida;
				if(docSaida.getDocumentElement() == null)
				{
					System.out.println("AA");
					Element Domain = docSaida.createElement("DOMAIN");
					docSaida.appendChild(Domain);
					
				}
				raizSaida = docSaida.getDocumentElement();
				Element Context = docSaida.createElement("CONTEXT");
				raizSaida.appendChild(Context);
				Element Entidade = docSaida.createElement("ENTITY");
				Context.appendChild(Entidade);
				
				
				DOMSource source = new DOMSource(docSaida);
				StreamResult result = new StreamResult(new FileOutputStream(arquivoXMLSaida));
				TransformerFactory transFactory = TransformerFactory.newInstance();
				
				try {
					Transformer transformer = transFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.transform(source, result);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
