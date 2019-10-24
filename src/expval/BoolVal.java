package expval;

public class BoolVal extends ExpVal{
	public Boolean val;
	
	public BoolVal(Boolean val) {
		this.val = val;
	}
	
	public String toString() {
		return ":BoolVal:" + val;
	}
}
