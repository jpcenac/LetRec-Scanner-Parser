package parser;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import ast.*;
import scanner.*;
import interpreter.Interpreter;
import interpreter.ValueOf;
import scanner.let_lang_scanner.Token;
import scanner.let_lang_scanner.TokenType;

public class letrec_parser {
	protected LinkedList<Token> parse_stream;
	protected LinkedList<String> parseexps;
	protected LinkedList<Expression> exps = new LinkedList<>();
	
	public letrec_parser(LinkedList<Token> tokenStream) throws Exception {
		parse_stream = tokenStream;
		parseexps = new LinkedList<String>();
		//LinkedList<Expression> exps = new LinkedList<Expression>();
		//parseExp();
		
		Expression finalExp = parseExp();
		Interpreter inte = new Interpreter();
		ValueOf prog = new ValueOf(finalExp);
		
		//prog.visit(inte);

		System.out.println("Parse-Tree\n" + finalExp);
/*		for(Expression e : exps) {
			System.out.println(e.toString());
		}*/
		
		//System.out.println("\nRESULT:::");
		System.out.println(prog.visit(inte));
	}
	
	public Expression parseExp() throws Exception{
		TokenType type = parse_stream.peek().type;
		String psdata = parse_stream.peek().data;
		//System.out.print(psdata + " ");
		switch (type) {
		case INTEGER:
			//System.out.println("INTPARSE");
			return parseConstExp();
		case LETREC:
			return parseLetRecExp(TokenType.LETREC);
		case ISZERO:
			return parseIsZeroExp(TokenType.ISZERO);
		/*case IF:
			return parseIfExp();*/
		case LET:
			//System.out.println("LETPARSE");
			return parseLetExp(TokenType.LET);
		case PLUS:
			//System.out.println("PLUSPARSE");
			return parseAddExp(TokenType.PLUS);
		case MINUS:
			//System.out.println("MINUSPARSE");
			return parseDiffExp(TokenType.MINUS);
		case IDENTIFIER:
			//System.out.println("VARPARSE");
			return parseVarExp();
		case IF:
			return parseIfExp(TokenType.IF);
		case PROC:
			return parseProcExp(TokenType.PROC);
		case LPAREN:
			return parseCallProc();
		
		default:
			throw new Exception("\nIncorrect Parse, check input on: "
					+ psdata);
		}	
	}



	public void consume(TokenType check) throws Exception{
		if(parse_stream.peek().type == check) {
			parse_stream.pollFirst();
		}else {
			throw new Exception("Parsing Type Error\n" 
					+ "Expected: " 
					+ check 
					+ "\n Received: "
					+ parse_stream.peek().type + " " + parse_stream.peek().data);
		}
	}

	
	private Expression parseConstExp() throws Exception{
		Integer number = Integer.parseInt(parse_stream.peek().data);
		consume(TokenType.INTEGER);
		parseexps.add(new ConstExp(number).toString());
		return new ConstExp(number);
	}
	
	private Expression parseLetExp(TokenType LETtype) throws Exception{
		//System.out.println("LET EXP");
		String var = new String();
		Expression letexp = null;
		
		consume(LETtype);
		
		while(parse_stream.peek().type == TokenType.IDENTIFIER) {
			var = parse_stream.peek().data;
			consume(TokenType.IDENTIFIER);
			consume(TokenType.ASSIGN);
			letexp = parseExp();
		}
		consume(TokenType.IN);
		Expression letbody = parseExp();
		
		exps.add(new LetExp(var, letexp, letbody));
		return new LetExp(var, letexp,letbody);
	}
	
	private Expression parseAddExp(TokenType PLUStype) throws Exception {
		consume(PLUStype);
		consume(TokenType.LPAREN);
		Expression left = parseExp();
		consume(TokenType.COMMA);
		Expression right = parseExp();
		consume(TokenType.RPAREN);
		
		exps.add(new AddExp(left, right));
		return new AddExp(left, right);
	}
	private Expression parseIfExp(TokenType tif) throws Exception {
		consume(tif);
		
		Expression ifarg = parseExp();
		
		consume(TokenType.THEN);
		
		Expression thenexp = parseExp();
		
		consume(TokenType.ELSE);
		
		Expression elsexp = parseExp();
		
		exps.add(new IfExp(ifarg, thenexp, elsexp));
		return new IfExp(ifarg, thenexp, elsexp);
		
	}
	private Expression parseIsZeroExp(TokenType iss) throws Exception {
		consume(iss);
		consume(TokenType.LPAREN);
		Expression arg = parseExp();
		consume(TokenType.RPAREN);
		exps.add(new IsZeroExp(arg));
		return new IsZeroExp(arg);
		
	}
	
	private Expression parseDiffExp(TokenType MINUStype) throws Exception {
		consume(MINUStype);
	/*	if(parse_stream.peek().type == TokenType.INTEGER) {
			Integer number = Integer.parseInt(parse_stream.peek().data);
			return new ConstExp(true, number);
		}*/
		consume(TokenType.LPAREN);
		Expression left = parseExp();
		consume(TokenType.COMMA);
		Expression right = parseExp();
		consume(TokenType.RPAREN);
		
		exps.add(new DiffExp(left, right));
		return new DiffExp(left, right);
	}
	
	private Expression parseProcExp(TokenType PROCtype) throws Exception{
		
		consume(PROCtype);
		consume(TokenType.LPAREN);
		String arg = parse_stream.pollFirst().data;
		consume(TokenType.RPAREN);
		Expression body = parseExp();
		exps.add(new ProcExp(arg, body));
		return new ProcExp(arg, body);
	}
	
	private Expression parseVarExp() throws Exception{
		String var = new String();
		while(parse_stream.peek().type == TokenType.IDENTIFIER) {
			var += parse_stream.pollFirst().data;
		}
		//consume(TokenType.IDENTIFIER);
		exps.add(new VarExp(var));
		return new VarExp(var);
	}
	
	private Expression parseCallProc() throws Exception {
		
		consume(TokenType.LPAREN);
		Expression procvar = parseExp();
		Expression exp = null;
		while(parse_stream.peek().type != TokenType.RPAREN) {
			exp = parseExp();
		}
		
		consume(TokenType.RPAREN);
		exps.add(new ProcVarExp(procvar, exp));
		return new ProcVarExp(procvar, exp);
	}
	private Expression parseLetRecExp(TokenType LRtype) throws Exception {
		//System.out.println("HELLO");
		consume(LRtype);
		String pname = new String();
		while(parse_stream.peekFirst().type != TokenType.LPAREN) {
			pname += parse_stream.pollFirst().data;
		}
		//System.out.println(pname);
		consume(TokenType.LPAREN);
		String arg = parse_stream.pollFirst().data;
		consume(TokenType.RPAREN);
		//System.out.println(exps);
		consume(TokenType.ASSIGN);
		//List<Expression> pbody = new ArrayList<>();
		Expression pbody = null;
		while(parse_stream.peekFirst().type != TokenType.IN) {
			pbody = parseExp();
		}
		consume(TokenType.IN);
		Expression letrecbody = parseExp();
		return new LetRecExp(pname, arg, pbody, letrecbody);
	}
}
