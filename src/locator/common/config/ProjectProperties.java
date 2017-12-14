package locator.common.config;

import java.util.List;

public class ProjectProperties {
	private String ssrc;
	private String tsrc;
	private String sbin;
	private String tbin;
	private List<String> classpath;
	public ProjectProperties(String ssrc, String tsrc, String sbin, String tbin, List<String> classpath) {
		this.ssrc = ssrc;
		this.tsrc = tsrc;
		this.sbin = sbin;
		this.tbin = tbin;
		this.classpath = classpath;
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
}
