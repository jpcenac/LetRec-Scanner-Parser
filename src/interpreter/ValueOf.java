package interpreter;

import java.io.IOException;

import ast.Expression;

public class ValueOf {
	public Expression exp;
	
	public ValueOf(Expression exp) {
		this.exp = exp;
	}
	
	public String toString() {
		return "Value: " + exp.toString();
	}
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}
}
