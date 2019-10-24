package parser;

public class IfExp implements LetLangExp{
	LetLangExp condExp, thenExp, elseExp;
	
	public IfExp(LetLangExp cond_exp, LetLangExp cond1, LetLangExp cond2){
		this.condExp = cond_exp;
		this.thenExp = cond1;
		this.elseExp = cond2;
	}
	
	public Integer eval(){
		if(condExp.getClass().equals(isZeroExp.class)||condExp.getClass().equals(BoolExp.class)||condExp.getClass().equals(LetExp.class)||condExp.getClass().equals(IfExp.class)) {
/*			System.out.println(condExp.eval() instanceof Boolean);
			System.out.println(condExp.getClass());*/
			if(condExp.eval() instanceof Boolean)  {
				if(Boolean.TRUE.equals(condExp.eval())) {
					//System.out.println("Then: " + thenExp.eval());
					return (Integer)thenExp.eval();
				}
				else{
					//System.out.println("Else: " + thenExp.eval());
					return (Integer)elseExp.eval();
				}
			}
		}
		System.out.println("ERROR: if conditional is not of boolean type");
		return 0;
		
		//if(thenExp.getClass() == isZeroExp.class | VarExp.class | ConstExp.class | DiffExp.class)
	}
}

