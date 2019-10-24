package parser;

public class BoolExp extends Exception implements LetLangExp<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Boolean value;
	public BoolExp(Boolean value) {
		this.value = value;
	}
	
	public Boolean eval() {
		if (value == true)
				return true;
		return false;
	}
}