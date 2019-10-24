package ast;

import java.io.IOException;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class DiffExp extends Expression{

	public Expression left, right;
	
	public DiffExp(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}
	
	public String toString() {
		return "\nDiffExp(\n " + left.toString() + "\n " + right.toString() + ")";
	}
	
	public ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException {
		return visitor.visit(this);
	}
}
