package expval;

public class NumVal extends ExpVal{
	public Integer number;

	public NumVal(Integer number){
		this.number = number;
	}
	
	public String toString() {
		return "NUMVAL:" + number;
	}
}
