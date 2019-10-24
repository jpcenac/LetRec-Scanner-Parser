package environment;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*;

import expval.ExpVal;
import expval.ProcVal;



public class Environment extends ArrayList implements Serializable{
	public List<Hashtable<String, ExpVal>> SymbolTable; 
	public List<Hashtable<String, ExpVal>> ProcTable;
	
	public Environment() {
		SymbolTable = new LinkedList<Hashtable<String, ExpVal>>();
		ProcTable = new LinkedList<Hashtable<String, ExpVal>>();
	}
	
	public void writeToFile() throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Environment.bin"));
		
		oos.writeObject(this);
		oos.close();
	}
	
	public Environment readFile() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Environment.bin"));
		Environment env = (Environment) ois.readObject();
		return env;
		//System.out.println(env.toString());
	}
	
	public Environment(List<Hashtable<String, ExpVal>> in) {
		int size = in.size();
//		/System.out.println("ST SIZE :: " + size);
		if(size == 0) {
			SymbolTable = new LinkedList<Hashtable<String, ExpVal>>();
		}else {
			for(int i = 0; i < size; i++) {
				this.SymbolTable.add(in.get(i));
			}
		}
		this.addAll(in);
		
	}
	
	public void extendEnvRec(Hashtable<String, ExpVal> procvals) {
		ProcTable.add(procvals);
	}
	
	public boolean containsProc(String procName) {
		int psize = ProcTable.size();
		for(int i =0; i< psize; i++) {
			Hashtable<String, ExpVal> currentProc = ProcTable.get(i);
			if(currentProc.containsKey(procName)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addAll(List<Hashtable<String,ExpVal>> in) {
		
		return this.SymbolTable.addAll(in);
	}
	
	public boolean clearaddAll(List<Hashtable<String,ExpVal>> in) {
		this.SymbolTable.clear();
		return this.SymbolTable.addAll(in);
	}
	
	public int retrieveSize() {
		return SymbolTable.size();
	}
	
	public boolean containsVar(String var) {
		int lvl = SymbolTable.size();
		for(int i= lvl-1; i >=0; i--) {
			if(SymbolTable.get(i).contains(var)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsVal(String var) {
		int lvl = SymbolTable.size();
		for(int i = lvl-1; i >= 0; i--) {
			if(SymbolTable.get(i).get(var)!=null) {
				return true;
			}
		}
		return false;
	}
	
	public void extendEnv(String var, ExpVal val) {
		final Hashtable<String, ExpVal> insertion = new Hashtable<>();
		insertion.put(var, val);
		SymbolTable.add(insertion);
	}
	
	/*public void extendEnvRec(Hashtable<String, ExpVal> in) {
		SymbolTable.add(in);
	}*/
	
	
	public ExpVal findExpVal(String var) {
		int lvl = SymbolTable.size();
		for(int i = lvl-1; i >= 0; i--) {
			if(SymbolTable.get(i).containsKey(var)) {
				return SymbolTable.get(i).get(var);
			}
		}
		System.out.println("Couldn't find Value, returning null");
		return null;
	}
	
	public void leaveScope() {
		//System.out.println("Popping Scope from level::" + SymbolTable.size());
		SymbolTable.remove(SymbolTable.size()-1);
		//ProcTable.remove((ProcTable.size()-1));
		
	}
	
	public void envClear() {
		SymbolTable.clear();
	}
	
/*	public Environment DeepCopy(Environment in) {
		Environment copy = new Environment();
		for(Hashtable<String, ExpVal> ip : in.SymbolTable) {
			copy.add(ip);
		}
		return copy;
	}*/
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int lvl = SymbolTable.size();
		sb.append("\n||||||||||||||||ENV|||||||||||||||||");
		for(int i=0; i<lvl; i++) {
			sb.append("\n"+"ScopeLevel:_:" + i + "  " + SymbolTable.get(i));
		}
		sb.append("\n|||||||||End Current Env||||||||||||\n");
		
		sb.append("\n|||||||||||||ProcEnv||||||||||");
		for(int j = 0; j < ProcTable.size(); j++) {
			sb.append("\nRecProc::: " + ProcTable.get(j));
		}
		sb.append("\n|||||||||End ProcEnv||||||||||||||\n");
		return sb.toString();
	}

	public ExpVal findProc(String var) {
		int psize = ProcTable.size();
		for(int i = 0; i < psize; i++) {
			Hashtable<String, ExpVal> currentProc = ProcTable.get(i);
			if(currentProc.containsKey(var)) {
				return currentProc.get(var);
			}
		}
		return null;
	}
}
