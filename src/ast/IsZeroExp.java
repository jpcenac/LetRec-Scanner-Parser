package ast;

import java.io.IOException;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class IsZeroExp extends Expression{
	public Expression check;
	
	public IsZeroExp(Expression check) {
		this.check = check;
	}
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}
	
	public String toString() {
		return "IsZeroExp{\n " + check + "\n}";
		
	}
}
