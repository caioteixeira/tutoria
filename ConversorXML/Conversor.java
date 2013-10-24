import java.io.File;


public class Conversor {
	public static void main(String[] args) {
		
		File atomic = new File("atomic-properties-world1.xml");
		File relation =  new File("relation-properties-world1.xml");
		File saida = new File("Context World 1.xml");
		File descriptions = new File("corpus-world1.xml");
		ConversorMundo.processaXML(atomic,relation, descriptions, saida);
		ConversorTrial.processaXML(descriptions, atomic,"TrialsWorld1");
		
		atomic = new File("atomic-properties-world2.xml");
		relation =  new File("relation-properties-world2.xml");
		saida = new File("Context World 2.xml");
		descriptions = new File("corpus-world2.xml");
		ConversorMundo.processaXML(atomic,relation, descriptions, saida);
		ConversorTrial.processaXML(descriptions, atomic,"TrialsWorld2");
		
		atomic = new File("atomic-properties-world3.xml");
		relation =  new File("relation-properties-world3.xml");
		saida = new File("Context World 3.xml");
		descriptions = new File("corpus-world3.xml");
		ConversorMundo.processaXML(atomic,relation, descriptions, saida);
		ConversorTrial.processaXML(descriptions, atomic,"TrialsWorld3");
		
	}
}
