package interpreter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;

import ast.*;
import expval.*;
import environment.Environment;

public class Interpreter implements TypeVisitor {
	Environment itpEnv;
	
	public Interpreter() {
		
		itpEnv = new Environment();
	}
	
	public Interpreter(Environment newEnv) {
		this.itpEnv = newEnv;
	}
	
	@Override
	public ExpType visit(ValueOf vo) throws ClassNotFoundException, IOException {
		
		//System.out.println(env.toString());
		return vo.exp.visit(this);
	}

	@Override
	public ExpType visit(ConstExp exp) {
		return new NumVal(exp.number);
	}

	@Override
	public ExpType visit(LetExp exp) throws ClassNotFoundException, IOException {
		String var = exp.var;
		
		Expression body = exp.body;
		
		//System.out.println("\nLetExp: " + var);
		
		ExpVal val = (ExpVal)exp.letexp.visit(this);
		
		if(val instanceof ProcVal) {
			//System.out.println("Extending Proc Expression");
			ProcVal pval = (ProcVal)val;
			
			Hashtable<String, ExpVal> procdef = new Hashtable<String, ExpVal>();
			//itpEnv.extendEnv(var, pval);
			pval.savedEnv.extendEnv(var, val);
			//procdef.put(var, val);
			//System.out.println("pvalEnv\n" + pval.savedEnv.toString());
			//((ProcVal) val).savedEnv.add(procdef);
			
			//((ProcVal) val).savedEnv.extendEnvRec(procdef);
			//System.out.println("After:::::" + ittoString() );
			
		}
		//System.out.println(" = \n" + val.toString());
		
		itpEnv.extendEnv(var, val);
		itpEnv.writeToFile();
		//System.out.println("+------------------------------------Check" + itpEnv.toString());
		//System.out.println("+Visiting::" + body.visit(this).toString());
		ExpType letval = body.visit(this);
		
		//System.out.println(itpEnv.toString());
		itpEnv.leaveScope();
		//System.out.println("++++++++++++++++++++++++++++++++++===Check" + itpEnv.toString());
		return letval;

	}

	@Override
	public ExpType visit(DiffExp exp) throws ClassNotFoundException, IOException {
		//System.out.println(env.toString());
		Integer left = ((NumVal) exp.left.visit(this)).number;
		Integer right = ((NumVal) exp.right.visit(this)).number;
		
		return new NumVal(left - right);
	}

	@Override
	public ExpType visit(AddExp exp) throws ClassNotFoundException, IOException {
		Integer left = ((NumVal) exp.left.visit(this)).number;
		Integer right = ((NumVal) exp.right.visit(this)).number;
		
		return new NumVal(left + right);
	}

	@Override
	public ExpType visit(VarExp exp) {
		
		String var = exp.var;
		//System.out.println("LOOKING FOR " + var +"\nIN" + itpEnv.toString());
		//System.out.println("VarExpCall::Looking for Binding for:: " + var);
		if(itpEnv.containsProc(var)) {
			ProcVal retrievedProc = (ProcVal)itpEnv.findProc(var);
			
			Hashtable<String, ExpVal> procValExt = new Hashtable<String, ExpVal>();
			procValExt.put(var, new ProcVal(retrievedProc.arg, retrievedProc.body, itpEnv));
			//System.out.println("Returning PrcVal::" + var + " = " +itpEnv.findProc(var) );
			return new ProcVal(retrievedProc.arg, retrievedProc.body, itpEnv);
		}
		
		else if(itpEnv.containsVal(var)) {
			//System.out.println("Returning Valu::" + var + " = " + itpEnv.findExpVal(var) );
			return itpEnv.findExpVal(var);
		
		}else {
			System.out.println("Returing null with var::" + var);
			return null;
		}
	}

	@Override
	public ExpType visit(IfExp exp) throws ClassNotFoundException, IOException {
		//System.out.println("IfExp Eval("  + exp.toString() + " )");
		Expression ift = exp.ifthen;
		Expression ife = exp.ifelse;
		
		Boolean val = ((BoolVal) exp.arg.visit(this)).val;
		if(val == true) {
			return ift.visit(this);
		}
		else {
			return ife.visit(this);
		}
	}

	@Override
	public ExpType visit(ProcExp exp) throws IOException, ClassNotFoundException {
		//System.out.println("!!----------PROC EXP----------!!");
		itpEnv.writeToFile();
		Environment procEnv = new Environment();
		procEnv= itpEnv.readFile();
		//System.out.println("ProcExpEnv====:::\n" + procEnv.SymbolTable);
		
		return new ProcVal(exp.var, exp.body, procEnv);
		//return retProc;
	}

	@Override
	public ExpType visit(ProcVarExp exp) throws ClassNotFoundException, IOException {
		//System.out.println("!!----------PROC VAL CALL----------!!");
		
		ProcVal proc = (ProcVal)exp.procedure.visit(this);
		//System.out.println("ProcValEnv------:::------\n" + proc.savedEnv.toString());
		//System.out.println(proc.savedEnv.toString());
		Environment holdEnv = new Environment();
		
		holdEnv = itpEnv.readFile();
		proc.savedEnv.writeToFile();
		
		itpEnv = proc.savedEnv.readFile();

		
		Expression operand = exp.operand;

		String formalvar = proc.arg;
		//System.out.println("!!----------PVC::OP VISIT----------!!");
		ExpVal arg = (ExpVal)operand.visit(this);

		
		itpEnv.extendEnv(formalvar, arg);
		//System.out.println("!!----------PVC::BODY VISIT----------!!");
		ExpType procval = proc.body.visit(this);
		
		holdEnv.writeToFile();
		itpEnv = holdEnv.readFile();
		//System.out.println("!!----------PVC::RETURNING----------!!");
		return procval;
		
	}

	@Override
	public ExpType visit(LetRecExp exp) throws ClassNotFoundException, IOException {
		//System.out.println("///====LetRecExp///=====");
		String procname = exp.fname;
		String boundvar = exp.arg;
		Expression procbody = exp.pbody;
		
		/*System.out.println("procname=" + procname 
						+" boundvar=" + boundvar 
						+ "\nprocbody=" + procbody);
		*/
		Environment holdEnv = new Environment();
		holdEnv.clear();
		holdEnv.addAll(itpEnv.SymbolTable);
		//itpEnv.clear();
		
		
		Environment letRecProcEnv = new Environment();
		//letRecProcEnv.clear();
		letRecProcEnv.addAll(itpEnv);
		
		Hashtable<String, ExpVal> rp = new Hashtable<String, ExpVal>();
		rp.put(procname, new ProcVal(boundvar, procbody, letRecProcEnv));
		itpEnv.extendEnvRec(rp);
		
		
		//itpEnv.clear();
		Expression letrecbody = exp.letrecbody;
		//itpEnv.clear();
		
		
		ExpType letrecval = (ExpVal)letrecbody.visit(this);
		//itpEnv.clear();
		itpEnv = holdEnv;
		holdEnv.clear();
		return letrecval;
	}

	@Override
	public ExpType visit(IsZeroExp exp) throws ClassNotFoundException, IOException {
		Integer value = ((NumVal)exp.check.visit(this)).number;
		if(value == 0) {
			return new BoolVal(true);
		}else {
			return new BoolVal(false);
		}
	}
}
