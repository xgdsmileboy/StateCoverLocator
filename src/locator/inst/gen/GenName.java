package locator.inst.gen;

public class GenName {
	
	private static int variableCount = 0;
	
	public static String genVariableName(int line) {
		variableCount++;
		return "automatic_" + Integer.toString(line) + "_" + Integer.toString(variableCount); 
	}
}
