import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Context {
	private File  arquivoXMLContexts;
	private Element context;
	
	public Context(String id)
	{
		arquivoXMLContexts = new File("Contexts.xml");
		//Inicializa Parsers DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		//Nota: Blocos Try/Catch são obrigatórios
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			try {
			
				
				
				//Define estrutura Document lendo XML
				Document doc = db.parse(arquivoXMLContexts);
				Element raiz = doc.getDocumentElement();
				NodeList listaContexts = raiz.getElementsByTagName("CONTEXT");
				//NodeList listaContexts = doc.getElementsByTagName("CONTEXT");
				
				//Define o elemento Context como raiz
				for(int i = 0; i < listaContexts.getLength(); i++)
				{
					Element elemento = (Element)listaContexts.item(i);
					//System.out.println(id);
					if(elemento.getAttribute("ID").equals(id))
					{
						context = elemento;
					}
				}
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
	
	//Retorna a quantidade de objetos do contexto
	public int getQuantObjetos()
	{
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		return listaObjetos.getLength();
	}
	
	//Retorna a cor do Target
	public String getCorTarget()
	{
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		//Percorre objetos
		for(int i = 0; i < listaObjetos.getLength(); i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			//Verifica se é o target
			if(elemento.getAttribute("FUNCTION").equals("target"))
			{
				return corDoElemento(elemento);
			}
		}
		return "";
		
	}
	
	//Retorna o tipo do target
	public String getTipoTarget()

	{
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		//Percorre objetos
		for(int i = 0; i < listaObjetos.getLength(); i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			//Verifica se é o target
			if(elemento.getAttribute("FUNCTION").equals("target"))
			{
				return tipoDoElemento(elemento);
			}
		}
		return "";
	}
	
	public String getHPosTarget()

	{
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		//Percorre objetos
		for(int i = 0; i < listaObjetos.getLength(); i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			//Verifica se é o target
			if(elemento.getAttribute("FUNCTION").equals("target"))
			{
				return hPosDoElemento(elemento);
			}
		}
		return "";
	}
	
	public String getVPosTarget()

	{
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		//Percorre objetos
		for(int i = 0; i < listaObjetos.getLength(); i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			//Verifica se é o target
			if(elemento.getAttribute("FUNCTION").equals("target"))
			{
				return vPosDoElemento(elemento);
			}
		}
		return "";
	}
	
	//Retorna o numero de objetos com a mesma cor do Target
	public int getQuantObjetosCorTarget()
	{
		String corTarget = getCorTarget();
		int contador = 0;
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		for(int i = 0; i<listaObjetos.getLength();i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			if(!elemento.getAttribute("FUNCTION").equals("target"))
			{
				if(corDoElemento(elemento).equals(corTarget))
				{
					contador++;
				}
			}
		}
		
		return contador;
	}
	//Retorna o numero de objetos do mesmo tipo do Target
	public int getQuantObjetosTipoTarget()
	{
		String tipoTarget = getTipoTarget();
		int contador = 0;
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		for(int i = 0; i<listaObjetos.getLength();i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			if(!elemento.getAttribute("FUNCTION").equals("target"))
			{
				if(tipoDoElemento(elemento).equals(tipoTarget))
				{
					contador++;
				}
			}
		}
		
		return contador;
	}
	
	public int getQuantObjetosHPosTarget()
	{
		String corTarget = getHPosTarget();
		int contador = 0;
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		for(int i = 0; i<listaObjetos.getLength();i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			if(!elemento.getAttribute("FUNCTION").equals("target"))
			{
				if(hPosDoElemento(elemento).equals(corTarget))
				{
					contador++;
				}
			}
		}
		
		return contador;
	}
	
	public int getQuantObjetosVPosTarget()
	{
		String corTarget = getVPosTarget();
		int contador = 0;
		NodeList listaObjetos = context.getElementsByTagName("ENTITY");
		for(int i = 0; i<listaObjetos.getLength();i++)
		{
			Element elemento = (Element)listaObjetos.item(i);
			if(!elemento.getAttribute("FUNCTION").equals("target"))
			{
				if(vPosDoElemento(elemento).equals(corTarget))
				{
					contador++;
				}
			}
		}
		
		return contador;
	}
	
	//Métodos diversos...
	//Retorna cor do objeto
	private String corDoElemento(Element elemento)
	{
		NodeList listaAtributos = elemento.getElementsByTagName("ATTRIBUTE");
		//Percorre atributos
		for(int j = 0; j < listaAtributos.getLength(); j++)
		{
			//Verifica se há atributo colour declarado, se não retorna valor padrão "white"
			Element atributo = (Element) listaAtributos.item(j);
			if(atributo.getAttribute("NAME").equals("colour"))
			{
				return atributo.getAttribute("VALUE");
			}
		}
		return "white";
	}
	//Retorna tipo do elemento
	private String tipoDoElemento(Element elemento)
	{
		NodeList listaAtributos = elemento.getElementsByTagName("ATTRIBUTE");
		//Percorre atributos
		for(int j = 0; j < listaAtributos.getLength(); j++)
		{
			Element atributo = (Element) listaAtributos.item(j);
			if(atributo.getAttribute("NAME").equals("type"))
			{
				return atributo.getAttribute("VALUE");
			}
		}
		return "";
	}
	//Retorna hpos do elemento
	private String hPosDoElemento(Element elemento)
	{
		NodeList listaAtributos = elemento.getElementsByTagName("ATTRIBUTE");
		//Percorre atributos
		for(int j = 0; j < listaAtributos.getLength(); j++)
		{
			//Verifica se há atributo colour declarado, se não retorna valor padrão "white"
			Element atributo = (Element) listaAtributos.item(j);
			if(atributo.getAttribute("NAME").equals("hpos"))
			{
				return atributo.getAttribute("VALUE");
			}
		}
		return "";
	}
	//Retorna vpos do elemento
	private String vPosDoElemento(Element elemento)
	{
		NodeList listaAtributos = elemento.getElementsByTagName("ATTRIBUTE");
		//Percorre atributos
		for(int j = 0; j < listaAtributos.getLength(); j++)
		{
			//Verifica se há atributo colour declarado, se não retorna valor padrão "white"
			Element atributo = (Element) listaAtributos.item(j);
			if(atributo.getAttribute("NAME").equals("vpos"))
			{
				return atributo.getAttribute("VALUE");
			}
		}
		return "";
	}
}

