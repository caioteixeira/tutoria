public class ClassificadorTeste {
	public static void main(String[] args) {
		ClassificadorUnderspecify a = new ClassificadorUnderspecify();
		
		//a.testa();
		System.out.println(a.underspecify(10,2,"left",3,4,true));
		
		System.out.println(a.underspecify(8,3,"left",3,2,false));
		
		System.out.println(a.underspecify(10,3,"right",3,4,false));
		
		System.out.println(a.underspecify(12,3,"right",3,6,true));
	}
}
