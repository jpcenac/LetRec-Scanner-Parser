package parser;

public class VarExp implements LetLangExp{
	Integer variable;
	public VarExp(Integer variable) {
		System.out.println("IN VAREXP:VALUE:" + variable);
		this.variable = variable;
	}
	
	public Integer eval(){
		return variable;
	}
}