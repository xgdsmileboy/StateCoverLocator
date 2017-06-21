package locator.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import locator.common.config.Constant;
import locator.common.config.ProjectProperties;
import locator.common.java.Subject;

/**
 * Select the projects to test.
 * @author lillian
 *
 */
public class ProjectSelector {
	
	/**
	 * Select project {@code whichProject} with bug id{@code whichBug}.
	 * @param whichProject
	 * 		: Name of the project
	 * @param whichBug
	 * 		: Bug id
	 * @return single subject
	 */
	public static Subject select(String whichProject, int whichBug) {
		ProjectProperties prop = Constant.PROJECT_PROP.get(whichProject);
		return new Subject(whichProject, whichBug, prop.getSsrc(), prop.getTsrc(), prop.getSbin(), prop.getTbin());
	}
	
	/**
	 * Select all bugs in project {@code whichProject}
	 * @param whichProject
	 * 		: Name of the project
	 * @return list of satisfied subject
	 */
	public static List<Subject> select(String whichProject) {
		int bugNumber = Constant.BUG_NUMBER.get(whichProject);
		List<Subject> result = new ArrayList<Subject>();
		for(int i = 1; i <= bugNumber; i++) {
			result.add(select(whichProject, i));
		}
		return result;
	}
	
	/**
	 * Select bugs in project {@code whichProject} with bug id in {@code whichBugs}
	 * @param whichProject
	 * 		: Name of the project
	 * @param whichBugs
	 * 		: list of bug id
	 * @return list of satisfied subject
	 */
	public static List<Subject> select(String whichProject, List<Integer> whichBugs) {
		List<Subject> result = new ArrayList<Subject>();
		for(Integer bugID : whichBugs) {
			result.add(select(whichProject, bugID));
		}
		return result;
	}
	
	/**
	 * Select all projects
	 * @return all subjects
	 */
	public static List<Subject> selectAll() {
		List<Subject> result = new ArrayList<Subject>();
		for(String name: Constant.PROJECT_NAME) {
			int bugNumber = Constant.BUG_NUMBER.get(name);
			for(int i = 1; i <= bugNumber; i++) {
				result.add(select(name, i));
			}
		}
		return result;
	}
	
	/**
	 * Select {@code samleCount} number of projects randomly.
	 * @param sampleCount
	 * 		: Number of projects to be selected.
	 * @return randomly selected projects.
	 */
	public static List<Subject> randomSelect(int sampleCount) {
		double [] evenDist = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		return randomSelect(evenDist, sampleCount);
	}
	
	/**
	 * Select {@code samleCount} number of projects randomly according to a probability distribution {@code prob}.
	 * @param prob
	 * 		: Probability distribution
	 * @param sampleCount
	 * 		: Number of projects to be selected.
	 * @return randomly selected projects.
	 */
	public static List<Subject> randomSelect(double [] prob, int sampleCount) {
		if (prob.length != Constant.PROJECT_NAME.length) {
			LevelLogger.error("Number of the probabilities should be the same as the number of project.");
			return null;
		}
		double [] prefixSum = new double[Constant.PROJECT_NAME.length + 1];
		double sum = 0.0;
		for(int i = 0; i < prob.length; i++) {
			sum += prob[i];
		}
		prefixSum[0] = 0;
		for(int i = 1; i < prefixSum.length; i++) {
			prefixSum[i] = prefixSum[i - 1] + prob[i - 1] / sum;
		}
		Random random = new Random(0);
		List<Subject> result = new ArrayList<Subject>();
		for(int i = 0; i < sampleCount; i++) {
			int projectID = binarySearch(prefixSum, 0, prefixSum.length, random.nextDouble());
			String whichProject = Constant.PROJECT_NAME[projectID];
			int whichBug = random.nextInt(Constant.BUG_NUMBER.get(whichProject));
			result.add(select(whichProject, whichBug));
		}
		return result;
	}
	
	private static int binarySearch(double [] prob, int b, int e, double r) {
		if (b == e - 1) {
			return b;
		}
		int mid = (b + e) / 2;
		if (r < prob[mid]) {
			return binarySearch(prob, b, mid, r);
		} else {
			return binarySearch(prob, mid, e, r);
		}
	}
}
