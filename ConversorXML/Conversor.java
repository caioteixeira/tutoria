import java.io.File;


public class Conversor {
	public static void main(String[] args) {
		
		File atomic = new File("atomic.xml");
		File relation =  new File("relation.xml");
		File saida = new File("saida.xml");
		
		File saidaTrial = new File("saidaTrial.xml");
		File descriptions = new File("descriptions.xml");
		
		ConversorMundo.processaXML(atomic,relation, descriptions, saida);
		ConversorTrial.processaXML(descriptions, atomic);

	}
}
