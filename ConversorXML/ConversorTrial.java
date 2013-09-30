import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


public class ConversorTrial {
	static DocumentBuilder db;
	public static void processaXML(File descriptions)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch s�o obrigat�rios
		try {
			db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
			Document doc = db.parse(descriptions);
			
			//Define elemento raiz 
			Element raiz = doc.getDocumentElement();
			
			//System.out.println(raiz.getTagName());
			
			escreveTrials(raiz);
			
			
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void escreveTrials(Element raiz)
	{
		NodeList expressoes = raiz.getElementsByTagName("EXPRESSION");
		for(int i = 0; i < expressoes.getLength(); i++)
		{
			File arquivoTrial;
			Element expressao = (Element)expressoes.item(i);
			arquivoTrial = new File("trials/"+expressao.getAttribute("PLAYER_ID")+".xml");
			Document doc;
			Element raizSaida;
			try {
				doc = db.parse(arquivoTrial);
				
			
			} catch (IOException | SAXException e) {
				doc = db.newDocument();
				Element trial = doc.createElement("TRIAL");
				trial.setAttribute("ID", expressao.getAttribute("PLAYER_ID"));
				doc.appendChild(trial);
			}
			raizSaida = doc.getDocumentElement();
			
			salvaXML(doc, arquivoTrial);
		}	
	}
	
	public static boolean foiCitado(String id, File descriptions)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch s�o obrigat�rios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
			Document doc = db.parse(descriptions);
			
			//Define elemento raiz 
			Element raiz = doc.getDocumentElement();
			
			//Nodelist das express�es
			NodeList expressoes = raiz.getElementsByTagName("EXPRESSION");
			
			for(int i = 0; i<expressoes.getLength(); i++)
			{
				Element expressao = (Element)expressoes.item(i);
				if(expressao.getAttribute("TARGET_ID").equals(id))
					return true;
			}
			
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static void salvaXML(Document doc, File arquivoXML)
	{		
		try {
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(arquivoXML));
			TransformerFactory transFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			File dir = new File("trials");
			dir.mkdir();
			salvaXML(doc, arquivoXML);
		}
	}	
}
