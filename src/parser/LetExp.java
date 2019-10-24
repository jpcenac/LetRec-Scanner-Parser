package parser;


//incomplete
public class LetExp implements LetLangExp<Object>{
	LetLangExp<?> identifier;
	LetLangExp<?> exp1;
	LetLangExp<?> inBody;

	
	public LetExp(LetLangExp<?> ident, LetLangExp<?> exp, LetLangExp<?> inBody) {
		//System.out.println("Building let");
		identifier = ident;
		this.exp1 = exp;
		this.inBody = inBody;
		
	}
	
	
	public Integer eval() {
		//System.out.println("Evaluating Body:value:" + inBody.eval());
		return Integer.parseInt(inBody.eval().toString()); 
	}
}