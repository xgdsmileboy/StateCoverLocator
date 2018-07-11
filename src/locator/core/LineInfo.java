package locator.core;

import java.util.HashMap;
import java.util.Map;

public class LineInfo {
	private int line;
	private String relJavaPath;
	private String clazz;
	private Map<String, String> allLegalVariablesMap;
	public LineInfo(){
		allLegalVariablesMap = new HashMap<String, String>();
	}
	public LineInfo(int line, String relJavaPath, String clazz) {
		this.line = line;
		this.relJavaPath = relJavaPath;
		this.clazz = clazz;
		allLegalVariablesMap = new HashMap<String, String>();
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getRelJavaPath() {
		return relJavaPath;
	}
	public void setRelJavaPath(String relJavaPath) {
		this.relJavaPath = relJavaPath;
	}
	public void addLegalVariable(String key, String value) {
		this.allLegalVariablesMap.put(key, value);
	}
	public String getLegalVariableType(String varName) {
		return this.allLegalVariablesMap.get(varName);
	}
	public boolean containsVaraible(String key) {
		return this.allLegalVariablesMap.containsKey(key);
	}
}
