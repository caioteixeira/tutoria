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


public class ConversorMundo {
	public static void processaXML(File XMLAtomic, File XMLRelation, File XMLDescriptions, File arquivoXMLSaida)
	{
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
		//Nota: Blocos Try/Catch sao obrigatorios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Define estrutura Document dos XMLs de entrada
			Document docAtomic = db.parse(XMLAtomic);
			Document docRelation = db.parse(XMLRelation);
			//Define estrutura Document do XML de saida
			Document docSaida = db.newDocument();
			
			//Define elemento raiz 
			Element raizAtomic = docAtomic.getDocumentElement();
			
			Element raizRelation = docRelation.getDocumentElement();
			
			
			//Verifica se ha um elemento Domain, se nao o cria
			Element raizSaida;
			if(docSaida.getDocumentElement() == null)
			{
				Element Domain = docSaida.createElement("DOMAIN");
				docSaida.appendChild(Domain);	
			}
			//Define a raiz do documento de saida
			raizSaida = docSaida.getDocumentElement();
			
			//Adiciona os contextos preenchidos
			adicionaContext(raizAtomic,raizRelation, raizSaida, XMLDescriptions, docSaida);
			
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
	private static void adicionaContext(Element raizAtomic,Element raizRelation, Element raizSaida, File XMLDescriptions, Document docSaida)
	{
		//Pega todos os contextos (Tags <DOMAIN>) do XML Atomic
		NodeList domainsAtomic = raizAtomic.getElementsByTagName("DOMAIN");
		
		//Cria e preenche os contextos com dados extraidos dos DOMAINS
		for(int i = 0; i < domainsAtomic.getLength();i++)
		{
			Element context = docSaida.createElement("CONTEXT");
			Element domainAtomic = (Element)domainsAtomic.item(i);
			String id = domainAtomic.getAttribute("ID");
			
			//Procura o DOMAIN correspondente no XML Relations
			Element domainRelation = procuraElementPorID(raizRelation,id);
		
			//Adiciona os objetos
			adicionaEntity(domainAtomic, domainRelation, context, XMLDescriptions, docSaida);
			context.setAttribute("ID", id);
			
			//Adiciona o contexto como no da raiz
			raizSaida.appendChild(context);
		}
	}
	private static Element procuraElementPorID(Element relational, String id)
	{
		//Pega a lista dos elementos filhos com tag <DOMAIN>
		NodeList domainsRelation = relational.getElementsByTagName("DOMAIN");
		
		for(int i = 0; i < domainsRelation.getLength(); i++)
		{
			Element domain = (Element)domainsRelation.item(i);
			
			//retorna o elemento DOMAIN se tiver a ID procurada
			if(domain.getAttribute("ID").equals(id))
			{
				//System.out.println(id);
				return domain;
			}
		}
		//Se n�o encontrar, retorna null
		return null;
	}
	
	
	
	private static void adicionaEntity(Element domainAtomic, Element domainRelation, Element context, File XMLDescriptions, Document docSaida)
	{
		//Lista dos objetos do DOMAIN Atomic
		NodeList entitysAtomic = domainAtomic.getElementsByTagName("ENTITY");
		//Cria os objetos com base nos objetos do DOMAIN Atomic
		for(int i = 0; i<entitysAtomic.getLength();i++)
		{
			Element entity = docSaida.createElement("ENTITY");
			Element entityAtomic = (Element)entitysAtomic.item(i);
			String id = entityAtomic.getAttribute("ID");
			entity.setAttribute("ID", id);
			//TODO:Adicionar atributo Function
			if(ConversorTrial.foiCitado(id, XMLDescriptions))
				entity.setAttribute("FUNCTION", "target");
			else
				entity.setAttribute("FUNCTION", "landmark");
			
			//Adiciona atributos
			adicionaAtributos(entityAtomic,domainRelation, entity, docSaida);
			context.appendChild(entity);
		}
		
		
	}
	private static void adicionaAtributos(Element entityAtomic, Element domainRelation, Element entity, Document docSaida)
	{
		//Lista dos atributos contidos no Entity atomic
		NodeList atributosAtomic = entityAtomic.getElementsByTagName("ATTRIBUTE");
		
		//Adiciona atributos
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
					
			}
		}
		
		//Se tiver relations, as adiciona no elemento
		if(domainRelation != null)
		{
			NodeList relations = domainRelation.getElementsByTagName("RELATION");
			String id = entity.getAttribute("ID");
			
			//Percorre as Relations
			for(int i = 0; i < relations.getLength(); i++)
			{
				Element relation = (Element)relations.item(i);
				//Se a Relation for referente ao objeto atual...
				if(relation.getAttribute("TARGET").equals(id))
				{
					String landmark = relation.getAttribute("LANDMARK");
					NodeList atributos = relation.getElementsByTagName("ATTRIBUTE");
					for(int j = 0; j<atributos.getLength(); j++)
					{
						Element atributo = (Element)atributos.item(j);
						
						//System.out.println(atributo.getAttribute("VALUE"));
						Element novoAtributo = docSaida.createElement("ATTRIBUTE");
						if(atributo.getAttribute("NAME").equals("position")){
							novoAtributo = docSaida.createElement("ATTRIBUTE");
							novoAtributo.setAttribute("NAME", atributo.getAttribute("VALUE"));
							novoAtributo.setAttribute("VALUE", landmark);
							entity.appendChild(novoAtributo);
							break;
						}
						
						
					}
				}
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
	//Retorna de que sala o objeto é
	public static String salaObjeto(String id, Document atomic)
	{
		Element raiz = atomic.getDocumentElement();
		NodeList domains = raiz.getElementsByTagName("DOMAIN");
		for(int i = 0; i<domains.getLength();i++)
		{
			Element domain = (Element) domains.item(i);
			NodeList entitys = domain.getElementsByTagName("ENTITY");
			for(int j = 0; j<entitys.getLength(); j++)
			{
				Element entity = (Element) entitys.item(j);
				if(entity.getAttribute("ID").equals(id))
				{
					return domain.getAttribute("ID");
				}
			}
		}
		return "";
	}
}
