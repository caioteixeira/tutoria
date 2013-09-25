import java.io.File;
import java.io.FileNotFoundException;
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
		
		
		processaXML(new File("atomic.xml"), new File("saida.xml"));

	}
	public static void processaXML(File XMLAtomic,/*File XMLRelation, */File arquivoXMLSaida)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch s�o obrigat�rios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
			Document docAtomic = db.parse(XMLAtomic);
			//Define estrutura Document do XML de saida
			Document docSaida = db.newDocument();
			
			//Define elemento raiz 
			Element raizAtomic = docAtomic.getDocumentElement();
			
			/* Element raizRelation = docRelation.getDocumentElement();
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
			
			adicionaContext(raizAtomic, raizSaida, docSaida);
			
			//docSaida.normalizeDocument();
			
			salvaXML(docSaida, arquivoXMLSaida);
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
	private static void adicionaContext(Element raizAtomic, Element raizSaida, Document docSaida)
	{
		NodeList domains = raizAtomic.getElementsByTagName("DOMAIN");
		for(int i = 0; i < domains.getLength();i++)
		{
			Element context = docSaida.createElement("CONTEXT");
			Element domainAtomic = (Element)domains.item(i);
			context.setAttribute("ID", domainAtomic.getAttribute("ID"));
			
			adicionaEntity(domainAtomic, context, docSaida);
			
			raizSaida.appendChild(context);
		}
	}
	private static void adicionaEntity(Element domainAtomic, Element context, Document docSaida)
	{
		NodeList entitysAtomic = domainAtomic.getElementsByTagName("ENTITY");
		for(int i = 0; i<entitysAtomic.getLength();i++)
		{
			Element entity = docSaida.createElement("ENTITY");
			Element entityAtomic = (Element)entitysAtomic.item(i);
			entity.setAttribute("ID", entityAtomic.getAttribute("ID"));
			//TODO:Adicionar atributo Function
			adicionaAtributos(entityAtomic, entity,docSaida);
			context.appendChild(entity);
		}
		
		
	}
	private static void adicionaAtributos(Element entityAtomic, Element entity, Document docSaida)
	{
		NodeList atributosAtomic = entityAtomic.getElementsByTagName("ATTRIBUTE");
		for(int i = 0;i<atributosAtomic.getLength(); i++)
		{
			Element atributo = (Element)atributosAtomic.item(i);
			switch(atributo.getAttribute("NAME"))
			{
				case "type":
					Element novoAtributo = docSaida.createElement("ATTRIBUTE");
					novoAtributo.setAttribute("NAME", "type");
					novoAtributo.setAttribute("VALUE", atributo.getAttribute("VALUE"));
					entity.appendChild(novoAtributo);
					break;
				default:
					continue;
			}
		}
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
			e.printStackTrace();
		}
	}
}
