package parser;

public class AddExp implements LetLangExp<Object>{
	LetLangExp<?> left, right;
	public AddExp(LetLangExp<?> l, LetLangExp<?> r) {
		
		left = l;
		right = r;
		eval();
	}
	public Integer eval() {
		if(left.eval().getClass() == Boolean.class && right.eval().getClass() == Boolean.class)
		{	Boolean rightB = (Boolean)right.eval();
		 	Boolean leftB = (Boolean)left.eval();
			
			if(leftB == false && rightB == false) {
				return 0;
			}
			else if(leftB == false && rightB == true){
				return 1;
			}else if(leftB == true && rightB == false) {
				return 1;
			}else {
				return 2;
			}
			
		}else if(right.eval().getClass() == Boolean.class) {
			Boolean rightB = (Boolean)right.eval();
			if(rightB == false) {
				return (Integer)left.eval();
			}
			else {
				return (Integer)left.eval() + 1;
			}
		}else if(left.eval().getClass() == Boolean.class)
		{
			Boolean leftB = (Boolean)left.eval();
			if(leftB == false) {
				return (Integer)right.eval();
			}
			else {
				return 1 + (Integer)right.eval();
			}
			
		}
		return (Integer)left.eval() - (Integer)right.eval();
	}
}
