package locator.common.config;

public class ProjectProperties {
	private String ssrc;
	private String tsrc;
	private String sbin;
	private String tbin;
	public ProjectProperties(String ssrc, String tsrc, String sbin, String tbin) {
		this.ssrc = ssrc;
		this.tsrc = tsrc;
		this.sbin = sbin;
		this.tbin = tbin;
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
}
