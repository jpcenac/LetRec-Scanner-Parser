package parser;

public class isZeroExp implements LetLangExp{
	Boolean result;
	int cast;
	public isZeroExp(LetLangExp Exp) {
		cast = (Integer)Exp.eval();
		eval();
		}
	
/*	public isZeroExp(int Exp) {
		
	}*/
	public Boolean eval() {
		if(cast == 0) {
			//System.out.println(true);
			return true;
		}
		//System.out.println(false);
		return false;
	}
}