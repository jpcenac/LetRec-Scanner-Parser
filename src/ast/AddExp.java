package ast;

import java.io.IOException;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class AddExp extends Expression {
	
	public Expression left, right;
	
	public AddExp(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}
	
	public String toString() {
		return "AddExp{" + left.toString() + " + " + right.toString() + "}";
	}
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
		}
}
