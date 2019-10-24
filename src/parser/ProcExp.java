package parser;

public class ProcExp extends procedureObject implements LetLangExp{
	String procID;
	LetLangExp<?> body;
	LetLangExp<?> env;
	
	public ProcExp(String id, LetLangExp<?> inB, LetLangExp<?> inE) {
		procID = id;
		body = inB;
		env = inE;
	}
	//Interchanging between procedure object and expression is important to remember
	//You either parse for the object to be created first, then unparse it for expression to be evaluated
	//when called.
	private void bindProcID(String pID, LetLangExp<?> pB) {
		String parseBody = pB.toString();
		
	}
	
	public Integer eval() {
		return 0;	
	}
/*	public procedureObject eval() {
		return procedureObject----	
	}*/
	
}
