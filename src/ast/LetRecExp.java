package ast;

import java.io.IOException;
import java.util.List;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class LetRecExp extends Expression {
	
	public String fname;
	public String arg;
	//public List<Expression> pbody;
	public Expression pbody;
	public Expression letrecbody;
	public LetRecExp(String fname, String arg, Expression pbody, Expression letrecbody){
		this.fname = fname;
		this.arg = arg;
		this.pbody = pbody;
		this.letrecbody = letrecbody;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LetRecExp{\n" + fname);
		sb.append("(" + arg.toString() + ")\n");
		sb.append(pbody.toString()+ "\n");
		sb.append(letrecbody.toString()+ "\n");
		sb.append("}");
		
		return sb.toString();
	}	
	
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}
}
