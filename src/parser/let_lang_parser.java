package parser;

//1. Program 		::= Expression
//2. Expression 	::= Number
//3. 				::= - ( Expression , Expression )
//4. 				::= iszero ( Expression )
//5. 				::= if Expression then Expression else Expression
//6. 				::= Identifier
//7. 				::= let Identifier = Expression in Expression

/*    	LPAREN("\\("),
	RPAREN("\\)"),
	COMMA(","),
	MINUS("-"),
	ASSIGN("="),
	ISZERO("iszero"),
	IF("if"),
	THEN("then"),
	ELSE("else"),
	LET("let"),
	IN("in"),
	//might be wrong
	IDENTIFIER("[a-z]"),
	INTEGER("[0-9]+"),
	BOOLEAN("bool"),
	UNKNOWN("_"),
    WHITESPACE("[ \t\f\r\n]+");*/


import java.util.Stack;
import scanner.let_lang_scanner.Token;
import scanner.let_lang_scanner.TokenType;
import parser.LetLangExp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class let_lang_parser<T>  {
	
	
	private LinkedList<Token> parse_stream;
	private List<Hashtable<String,T>> SymbolTable = new ArrayList<Hashtable<String,T>>();
	
	
	private Stack<Integer> expStack = new Stack<Integer>();
	
	public let_lang_parser(LinkedList<Token> in_stream, Hashtable<String, T> PreEnv){
		envSet(PreEnv);
		splice_stream(in_stream);
		parse_stream = in_stream;
		System.out.print("Spliced Tokens: " + in_stream + "\n");
		
		parseProgram(parse_stream);
		System.out.println(expStack.peek());
		//System.out.print(expStack);
	}
	
	public void envSet(Hashtable<String,T> envInput) {
		if(envInput.isEmpty()) {
			System.out.println("No environment set before program");
		}
		else
		{
			SymbolTable.add(envInput);
			System.out.println("Environment before Program: " + envInput);
		}
	}
	
	private void splice_stream(LinkedList<Token> in) {
		for(int i = 0; i <in.size(); i++) {
			if(in.get(i).type == TokenType.RPAREN || in.get(i).type == TokenType.LPAREN || in.get(i).type == TokenType.COMMA 
					|| in.get(i).type == TokenType.THEN || in.get(i).type == TokenType.ELSE || in.get(i).type == TokenType.ASSIGN
					|| in.get(i).type == TokenType.IN
					)
			{
					in.remove(i);
					i--;
			}
		}
	}
	
	LetLangExp<?>  parseProgram(LinkedList<Token> tokens) {
		System.out.println("-Parsing Program-Parse Tree-");
		return parseExp(tokens);
	}
	
	LetLangExp<?> parseExp(LinkedList<Token> tokens) {
	while(!tokens.isEmpty()) {
		//System.out.print(" ");
		if(tokens.peek().type == TokenType.MINUS){
			
			System.out.print("DiffExp{\n");
			
			return parseDiff(tokens);
		}
		else if(tokens.peek().type == TokenType.ISZERO){
			
			System.out.print("isZero{\n");
			
			return parseIsZero(tokens);
		}
		else if(tokens.peek().type == TokenType.INTEGER){
	
			System.out.print("ConstExp(\n");
			
			return parseNumber(tokens);
		}
		else if(tokens.peek().type == TokenType.IF) {
			
			System.out.print("IfExp{\n");
			
			return parseIf(tokens);
		}
		else if(tokens.peek().type == TokenType.LET){
			System.out.print("LetExp{\n");
			return parseLet(tokens);
		}
		else if(tokens.peek().type == TokenType.IDENTIFIER) {
			
			System.out.print("VarExp{\n");
			
			return parseVar(tokens);
		}
		else if(tokens.peek().type == TokenType.PLUS) {
			System.out.print("AddExp{\n");
			return parseAdd(tokens);
		}
		}
	return new ConstExp(expStack.lastElement());
	}

	
	LetLangExp<?> parseDiff(LinkedList<Token> tokens) {
		
		tokens.pollFirst();
		LetLangExp<?> left = parseExp(tokens);
		LetLangExp<?> right = parseExp(tokens);
		//System.out.println("DIFFERENCE:" + (Integer)left.eval() + "-"+ (Integer)right.eval()+ "="+((Integer)left.eval() - (Integer)right.eval()));
		System.out.println("}");
		expStack.push(new DiffExp(left, right).eval());
		return new DiffExp(left, right);
	}
	
	LetLangExp<?> parseAdd(LinkedList<Token> tokens){
		tokens.pollFirst();
		LetLangExp<?> left = parseExp(tokens);
		LetLangExp<?> right = parseExp(tokens);
		System.out.println("}");
		expStack.push(new AddExp(left,right).eval());
		return new AddExp(left,right);
	}
	
	LetLangExp<?> parseNumber(LinkedList<Token> tokens) {
		
		String nStr = tokens.pollFirst().data;
		Integer value = Integer.parseInt(nStr);
		System.out.print("" + value  + "\n");
		System.out.print(")\n");
		expStack.push(new ConstExp(value).eval());
		return new ConstExp(value);
	}
	
	LetLangExp<?> parseIsZero(LinkedList<Token> tokens) {
		tokens.pollFirst();
		LetLangExp<?> checkZero = parseExp(tokens);
		return new isZeroExp(checkZero);
	}
	
	LetLangExp<?> parseIf(LinkedList<Token> tokens) {
		tokens.pollFirst();
		LetLangExp<?> ifCon = parseExp(tokens);
		LetLangExp<?> exp1 = parseExp(tokens);
		LetLangExp<?> exp2 = parseExp(tokens);
		
		expStack.push(new IfExp(ifCon,exp1,exp2).eval());
		return new IfExp(ifCon, exp1, exp2);
	}
	
	LetLangExp<?> parseProc(LinkedList<Token> tokens){
		String procID = tokens.pollFirst().data;
		System.out.print("'" + procID +"'\n");
		LetLangExp<?> procBody = parseExp(tokens);
		LetLangExp<?> procEnv = parseVar(tokens);
		return new ProcExp(procID, procBody, procEnv);
	}
	
	@SuppressWarnings("unchecked")
	LetLangExp<?> parseLet(LinkedList<Token> tokens) {
		tokens.pollFirst();
		final Hashtable<String,T> VariableTable = new Hashtable<String, T>();
		LetLangExp<?> ident = parseIdent(tokens);
		String identS = ident.eval().toString();
		
		LetLangExp<?> value = parseExp(tokens);
		Integer valueI = null;
		Boolean valueB = null;
		procedureObject valueP = null;
		if(value.eval().getClass() == Boolean.class) {
			valueB = (Boolean)value.eval();
			VariableTable.put(identS, (T)valueB);
		}else if((value.eval().getClass() == Integer.class)) {
			valueI = (Integer)value.eval();
			VariableTable.put(identS, (T)valueI);
		}else {
			valueP = (procedureObject)value.eval();
			VariableTable.put(identS, (T)valueP);
		}
		
		//System.out.println("VT: " + VariableTable);
		SymbolTable.add(VariableTable);
		
		LetLangExp<?> body = parseExp(tokens);
		//System.out.println("ST: " + SymbolTable);
		SymbolTable.remove(SymbolTable.size() - 1);
		System.out.print("}\n");
		
		expStack.push(new LetExp(ident,value,body).eval());
		return new LetExp(ident, value, body);
	}

	LetLangExp<?> parseIdent(LinkedList<Token> tokens) {
		String ident = tokens.pollFirst().data;
		System.out.print("'" + ident+ "'\n");
		return new identExp(ident);
	}

	
	LetLangExp<?> parseVar(LinkedList<Token> tokens) {
		//System.out.println("ST: " + SymbolTable);
		String var = tokens.pollFirst().data;
		Integer vari = null;
		Boolean varb = null;
		procedureObject varp = null;
		for(int i = SymbolTable.size()-1; i >= 0; i--) {
			if(SymbolTable.get(i).containsKey(var)) {
				
				if(SymbolTable.get(i).get(var).getClass() == Integer.class) {
					vari = (Integer)SymbolTable.get(i).get(var);
					i =0;
					System.out.print("'" + vari + "'\n");
					return new ConstExp(vari);
				}else if(SymbolTable.get(i).get(var).getClass() == Boolean.class){
					varb = (Boolean)SymbolTable.get(i).get(var);
					i =0;
					System.out.print("'" + varb + "'\n");
					return new BoolExp(varb);
				}else {
					varp = (procedureObject)SymbolTable.get(i).get(var);
					i = 0;
					System.out.print("'" + varp + "'\n");
					//return new ProcExp(var, );
				}
			}
		}
		return new ConstExp(vari);
	}

/*	LetLangExp<?> parseProc(LinkedList<Token> tokens){
		tokens.pollFirst();
		final Hashtable<String,T> ProcTable = new Hashtable<String, T>();
		LetLangExp<?> ident = parseIdent(tokens);
		String identP
		
	}*/
}