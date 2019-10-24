package ast;

import java.io.IOException;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class IfExp extends Expression{
	public Expression arg;
	public Expression ifthen;
	public Expression ifelse;
	public IfExp(Expression arg, Expression ifthen, Expression ifelse) {
		this.arg = arg;
		this.ifthen = ifthen;
		this.ifelse = ifelse;
	}
		
	@Override
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}
	
	public String toString() {
		return "IfExp{ " + arg.toString() + "\n then{" 
				+ ifthen.toString() + "}\n else{"
				+ ifelse.toString() + "}\n"
				+ "}";
	}

}
