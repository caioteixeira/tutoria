import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class ConversorTrial {
	static String xsl = "identacao.xsl";
	static DocumentBuilder db;
	static String dirDestino;
	public static void processaXML(File descriptions, File atomic, String dirSaida)
	{
		dirDestino = dirSaida;
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		//dbf.setValidating(true);
		
		//Nota: Blocos Try/Catch sao obrigatorios
		try {
			db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
		    
			Document doc = db.parse(descriptions);
			Document docAtomic = db.parse(atomic);
			
			//Define elemento raiz 
			Element raiz = doc.getDocumentElement();
			
			//System.out.println(raiz.getTagName());
			
			escreveTrials(raiz, doc, docAtomic);
			
			
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
	private static void escreveTrials(Element raiz, Document descriptions, Document atomic)
	{
		NodeList expressoes = raiz.getElementsByTagName("EXPRESSION");
		for(int i = 0; i < expressoes.getLength(); i++)
		{
			File arquivoTrial;
			Element expressao = (Element)expressoes.item(i);
			String playerId = expressao.getAttribute("PLAYER_ID");
			//System.out.print(playerId);
			arquivoTrial = new File(dirDestino+"/"+playerId+".xml");
			Document doc;
			try {
				doc = db.parse(arquivoTrial);
				escreveContext(expressao,  atomic, doc);
			
			} catch (IOException  e) {
				doc = db.newDocument();
				Element trial = doc.createElement("TRIAL");
				trial.setAttribute("ID", playerId);
				doc.appendChild(trial);
				escreveContext(expressao, atomic, doc);
			}
			catch(SAXException e)
			{
				System.out.println("Arquivo vazio!");
				return;
			}
			
			salvaXML(doc, arquivoTrial);
		}	
	}
	private static void escreveContext(Element expressao, Document atomic, Document docTrial )
	{
		String target = expressao.getAttribute("TARGET_ID");
		//docTrial.normalizeDocument();
		Element raiz = docTrial.getDocumentElement();
		
		Element context = docTrial.createElement("CONTEXT");
		
		if(raiz.getElementsByTagName("ATTRIBUTE").getLength()>0)
		{
			Text a = docTrial.createTextNode("\t");
			raiz.appendChild(a);
			//a.appendChild(context);
		}
		
		//System.out.print(" "+target+"\n");
		context.setAttribute("TARGET", target);
		context.setAttribute("ID", ConversorMundo.salaObjeto(target, atomic));
		Element atributeSet = docTrial.createElement("ATTRIBUTE-SET");
		escreveAtributosObjeto(expressao, atributeSet, docTrial);
		context.appendChild(atributeSet);
		
		//docTrial.appendChild(raiz);
		raiz.appendChild(context);
		
	}
	private static void escreveAtributosObjeto(Element expressao, Element atributeSet, Document docTrial)
	{
		Element entityExpressao = (Element) expressao.getElementsByTagName("ENTITY").item(0);
		if(entityExpressao!=null)
		{
			NodeList atributos = entityExpressao.getElementsByTagName("ATTRIBUTE");
			for(int i = 0; i<atributos.getLength();i++)
			{
				Element atributo = (Element) atributos.item(i);
				switch(atributo.getAttribute("NAME"))
				{
					case "type":
						Element tipo = docTrial.createElement("ATTRIBUTE");
						tipo.setAttribute("NAME","type");
						tipo.setAttribute("VALUE", atributo.getAttribute("VALUE"));
						atributeSet.appendChild(tipo);
						break;
					case "color":
						Element cor = docTrial.createElement("ATTRIBUTE");
						cor.setAttribute("NAME","others");
						cor.setAttribute("VALUE", atributo.getAttribute("VALUE"));
						atributeSet.appendChild(cor);
						break;
					
				}
			}
		}
		NodeList relactions = expressao.getElementsByTagName("RELATIONSHIP");
		for(int i = 0; i < relactions.getLength(); i++)
		{
			Element relationship = (Element) relactions.item(i);
			Element relation = (Element) relationship.getElementsByTagName("RELATION").item(0);
			Element landmark = (Element) relationship.getElementsByTagName("ENTITY").item(0);
			if(relation!=null)
			{
				Element atributoRelation = (Element) relation.getElementsByTagName("ATTRIBUTE").item(0);
				String name = atributoRelation.getAttribute("VALUE");
				switch(name)
				{
					case "left":
						Element esquerda = docTrial.createElement("ATTRIBUTE");
						esquerda.setAttribute("NAME","left");
						esquerda.setAttribute("VALUE", relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(esquerda);
						break;
					case "right":
						Element direita = docTrial.createElement("ATTRIBUTE");
						direita.setAttribute("NAME","right");
						direita.setAttribute("VALUE", relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(direita);
						break;
					case "corner":
						Element canto = docTrial.createElement("ATTRIBUTE");
						canto.setAttribute("NAME","corner"); 
						canto.setAttribute("VALUE", relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(canto);
						break;
					case "next":
						Element proximo = docTrial.createElement("ATTRIBUTE");
						proximo.setAttribute("NAME","next"); 
						proximo.setAttribute("VALUE",relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(proximo);
						break;
					case "up":
						Element acima = docTrial.createElement("ATTRIBUTE");
						acima.setAttribute("NAME","up"); 
						acima.setAttribute("VALUE",relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(acima);
						break;
					case "in front of":
						Element frente = docTrial.createElement("ATTRIBUTE");
						frente.setAttribute("NAME","infrontof"); 
						frente.setAttribute("VALUE", relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(frente);
						break;
					case "behind":
						Element atras = docTrial.createElement("ATTRIBUTE");
						atras.setAttribute("NAME","behind"); 
						atras.setAttribute("VALUE", relation.getAttribute("LANDMARK"));
						atributeSet.appendChild(atras);
						break;
					default:
						System.out.println(name);
						break;	
				}
			}
			if(landmark!=null)
			{
				NodeList atributosLandmark = landmark.getElementsByTagName("ATTRIBUTE");
				for(int j = 0; j<atributosLandmark.getLength();j++)
				{
					Element atributoLandmark = (Element) atributosLandmark.item(j);
					String name = atributoLandmark.getAttribute("NAME");
					
					switch(name)
					{
						case "type":
							Element tipo = docTrial.createElement("ATTRIBUTE");
							tipo.setAttribute("NAME","landmark-type"); 
							tipo.setAttribute("VALUE", atributoLandmark.getAttribute("VALUE"));
							atributeSet.appendChild(tipo);
							break;
						case "color":
							Element cor = docTrial.createElement("ATTRIBUTE");
							cor.setAttribute("NAME","landmark-others"); 
							cor.setAttribute("VALUE", atributoLandmark.getAttribute("VALUE"));
							atributeSet.appendChild(cor);
							break;
						default:
							System.out.println(name);
							break;
					}
				}
			}
		}
		
	}
	public static boolean foiCitado(String id, File descriptions)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch sao obrigatorios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
			Document doc = db.parse(descriptions);
			
			//Define elemento raiz 
			Element raiz = doc.getDocumentElement();
			
			//Nodelist das expressoes
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
			transFactory.setAttribute("indent-number", new Integer(4));
			
			
			File stylesheet = new File(xsl);
			StreamSource stylesource = new StreamSource(stylesheet);
			
			Transformer transformer = transFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			//transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			//transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(source, result);
		} catch (TransformerException e) {
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			File dir = new File(dirDestino);
			dir.mkdir();
			salvaXML(doc, arquivoXML);
		}
	}	
}
