package ast;

import java.io.IOException;
import java.io.Serializable;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public abstract class Expression implements Serializable{
	public abstract ExpType visit(TypeVisitor visitor) throws ClassNotFoundException, IOException;
}
