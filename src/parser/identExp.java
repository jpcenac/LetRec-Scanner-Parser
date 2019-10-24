package parser;

public class identExp implements LetLangExp<Object>{
	String ident;
	
	public identExp(String id) {
		ident = id;
	}
	
	public String eval() {
		return ident.toString();
	}
	
}
