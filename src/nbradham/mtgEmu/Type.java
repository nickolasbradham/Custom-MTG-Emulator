package nbradham.mtgEmu;

public enum Type {
	COMMANDER(0), LIBRARY(1), TOKEN(2);
	
	private final int val;
	
	private Type(int setVal) {
		val = setVal;
	}
}
