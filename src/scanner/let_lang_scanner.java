package scanner;

import parser.*;
import scanner.let_lang_scanner.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class let_lang_scanner {
	static Hashtable<String,Integer> PreEnv  = new Hashtable<String, Integer>();
    public static enum TokenType {
    	LPAREN("\\("),
    	RPAREN("\\)"),
    	COMMA(","),
    	MINUS("-"),
    	PLUS("\\+"),
    	ASSIGN("="),
    	PROC("proc"),
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
        WHITESPACE("[ \t\f\r\n]+");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {
        public TokenType type;
        public String data;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        public String getData(){
            return data;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), data);
        }
    }

    public static ArrayList<Token> lex(String input) {
    	
        // The tokens to return
        ArrayList<Token> tokens = new ArrayList<Token>();

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        //System.out.println(input);
        while (matcher.find()) {

            if (matcher.group().matches(TokenType.LPAREN.pattern)) {
                tokens.add(new Token(TokenType.LPAREN, matcher.group()));
                //System.out.println("WHO: "+matcher.group()+"\n");
                continue;
            } else if (matcher.group().matches(TokenType.RPAREN.pattern)) {
                tokens.add(new Token(TokenType.RPAREN, matcher.group()));
                continue;
            } else if (matcher.group().matches(TokenType.COMMA.pattern)) {
                tokens.add(new Token(TokenType.COMMA, matcher.group()));
                continue;
            } else if (matcher.group().matches(TokenType.MINUS.pattern)) {
                tokens.add(new Token(TokenType.MINUS, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.PLUS.pattern)) {
                tokens.add(new Token(TokenType.PLUS, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.ASSIGN.pattern)) {
                tokens.add(new Token(TokenType.ASSIGN, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.ISZERO.pattern)) {
                tokens.add(new Token(TokenType.ISZERO, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.IF.pattern)) {
                tokens.add(new Token(TokenType.IF, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.PROC.pattern)) {
                tokens.add(new Token(TokenType.PROC, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.THEN.pattern)) {
                tokens.add(new Token(TokenType.THEN, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.ELSE.pattern)) {
                tokens.add(new Token(TokenType.ELSE, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.LET.pattern)) {
                tokens.add(new Token(TokenType.LET, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.IN.pattern)) {
                tokens.add(new Token(TokenType.IN, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.IDENTIFIER.pattern)) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.INTEGER.pattern)) {
                tokens.add(new Token(TokenType.INTEGER, matcher.group()));
                continue;
            }else if (matcher.group().matches(TokenType.BOOLEAN.pattern)) {
                tokens.add(new Token(TokenType.BOOLEAN, matcher.group()));
                continue;
            }
            else if (matcher.group().matches(TokenType.WHITESPACE.pattern)) {
                continue;
            }
        }

        return tokens;
    }
    
    public static void applyEnv(String s, Integer i) {
    	String ins=s;
    	Integer ii = i;
    	
    	PreEnv.put(s, ii);
    	
    }

    public static void main(String[] args) {
    	String input1	 = "let x =7 in let y = 2 in let y = let x = -(x,1) in -(x,y) in -(-(x,8),y)";
    	String input2 	 = "if iszero(-(x,11)) then -(y,2) else -(y,4)";
    	String input3    = "let y = 5 in -(y,3)";
    	String input4    = "let x = 5 in let y = let x = -(x,1) in -(x,4)";
    	String input5    = "let x = iszero(1) in +(7,x)";
    	String input6	 = "let x = 200 in let f = proc (z) -(z,x) in let x 100 "
    							+ "in let g = proc (z) -(z,x) in -((f 1), (g 1))";
    	String input7    = "let x = 200 in let f = proc(z) -(z,x) in let t = (f 350)";
        ArrayList<Token> tokens = lex(input1);
        
       /* applyEnv("x",32);
        applyEnv("y",22);
*/
        LinkedList<Token> lltok = new LinkedList<Token>();
        lltok.addAll(tokens);
        
        let_lang_parser<Integer> parse_tokens = new let_lang_parser<Integer>(lltok, PreEnv); 
        
    }
    
    
}