package ast;

import java.io.IOException;
import java.util.List;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class ProcVarExp extends Expression{
	final public Expression procedure;
	final public Expression operand;
	
	public ProcVarExp(Expression procedure, Expression operand) {
		this.procedure = procedure;
		this.operand = operand;
		
	}
	
	public String toString() {
		StringBuilder cpstring = new StringBuilder();
		cpstring.append("(");
		cpstring.append(procedure.toString());
		
		cpstring.append(":" + operand);
		cpstring.append(")");
		
		return cpstring.toString();
	}
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}

}
