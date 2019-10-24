package interpreter;

import java.io.IOException;

import ast.*;


//Visitor design pattern
public interface TypeVisitor {
	ExpType visit(ConstExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(LetExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(ValueOf exp)throws ClassNotFoundException, IOException;
	ExpType visit(DiffExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(AddExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(VarExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(IfExp exp) throws ClassNotFoundException, IOException;
	ExpType visit(ProcExp exp) throws IOException, ClassNotFoundException;
	ExpType visit(ProcVarExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(LetRecExp exp)throws ClassNotFoundException, IOException;
	ExpType visit(IsZeroExp exp)throws ClassNotFoundException, IOException;
	
}
