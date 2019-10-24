package parser;

public class ConstExp implements LetLangExp<Object>{
	Integer value;
	public ConstExp(Integer value) {
		this.value = value;
		eval();
/*		System.out.print(value);
		System.out.println("}");*/
	}
	
	public Integer eval(){
		//System.out.println(" ");
		return Integer.valueOf(value);
	}
}
