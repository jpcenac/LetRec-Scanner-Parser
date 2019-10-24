package expval;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import ast.Expression;
import environment.Environment;
import java.io.*;


public class ProcVal extends ExpVal{
	public String arg;
	public Expression body;
	public Environment savedEnv = new Environment();
	//public List<Hashtable<String, ExpVal>> savedEnv = new LinkedList<Hashtable<String, ExpVal>>();
	//public final List<Hashtable<String, ExpVal>> savedEnv;
	
	final public int envLevel;
	private String freekey = new String();
	
	public ProcVal(String args, Expression body, Environment savedEnv) {
		this.arg = args;
		this.body = body;
		//this.savedEnv = savedEnv.SymbolTable;
		this.savedEnv = savedEnv;
		envLevel = savedEnv.retrieveSize();
		//System.out.println("ProcEnv Scope:: "+envLevel);
		//System.out.println(savedEnv.toString());
	}
	
/*	public void freeVar() {
		for(int i = 0; i < envLevel; i++) {
			//System.out.println("arg::" + arg);
			if(!savedEnv.containsVal(arg)) {
				freekey = savedEnv.getKeys(envLevel);
				//System.out.println(freekey);
			}
		}
	}*/
	public void ensureScope(int level) {
		for(int i = level; i < this.savedEnv.size() ; i++) {
			this.savedEnv.remove(level);
		}
	}
	
/*	public List<Hashtable<String, ExpVal>> retrieveProcScope() {
		int level = envLevel;
		List<Hashtable<String, ExpVal>> buildEnv = new LinkedList<Hashtable<String, ExpVal>>();
		for(int i = 0; i < level; i++) {
			//System.out.println("BUILDING PROC ENV::" + i + " ::" + SymbolTable.get(i));
			buildEnv.add(savedEnv.get(i));
		}
		return buildEnv;
	}*/
/*	
	public ExpVal bindFree() {
		List<Hashtable<String, ExpVal>> current = savedEnv.retrieveProcScope(envLevel);
		return current.get(freekey);
	}*/
	
	public String toString() {
		StringBuilder procstring = new StringBuilder();
		procstring.append(arg.toString());
		procstring.append(" :-: ");
		procstring.append(body.toString());
		procstring.append("\n");
		
		//System.out.println(savedEnv.toString());
		return procstring.toString();
	}
}
