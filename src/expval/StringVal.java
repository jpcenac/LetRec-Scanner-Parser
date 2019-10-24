package expval;

public class StringVal extends ExpVal{
	public String val;
	
	public StringVal(String val)
	{
		this.val = val;
	}
	
	public String toString() {
		return "" + val;
	}
}
