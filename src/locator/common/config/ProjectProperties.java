package locator.common.config;

import java.util.List;

import jdk7.wrapper.JCompiler.SOURCE_LEVEL;

public class ProjectProperties {
	private String ssrc;
	private String tsrc;
	private String sbin;
	private String tbin;
	private List<String> classpath;
	private SOURCE_LEVEL source = SOURCE_LEVEL.L_1_4;
	private SOURCE_LEVEL target = SOURCE_LEVEL.L_1_4;
	public ProjectProperties(String ssrc, String tsrc, String sbin, String tbin, List<String> classpath, SOURCE_LEVEL source, SOURCE_LEVEL target) {
		this.ssrc = ssrc;
		this.tsrc = tsrc;
		this.sbin = sbin;
		this.tbin = tbin;
		this.classpath = classpath;
		this.source = source;
		this.target = target;
	}
	public String getSsrc() {
		return ssrc;
	}
	public String getTsrc() {
		return tsrc;
	}
	public String getSbin() {
		return sbin;
	}
	public String getTbin() {
		return tbin;
	}
	public List<String> getClasspath() {
		return classpath;
	}
	public SOURCE_LEVEL getSourceLevel() {
		return source;
	}
	public SOURCE_LEVEL getTargetLevel() {
		return source;
	}
}
