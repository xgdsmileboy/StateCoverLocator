package locator.aux.extractor.core.feature;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import locator.aux.extractor.core.feature.item.ColumnNumber;
import locator.aux.extractor.core.feature.item.Feature;
import locator.aux.extractor.core.feature.item.FileName;
import locator.aux.extractor.core.feature.item.IfUse;
import locator.aux.extractor.core.feature.item.LastAssign;
import locator.aux.extractor.core.feature.item.LineNumber;
import locator.aux.extractor.core.feature.item.MethodName;
import locator.aux.extractor.core.feature.item.MethodSize;
import locator.aux.extractor.core.feature.item.VarName;
import locator.aux.extractor.core.feature.item.VarType;
import locator.aux.extractor.core.parser.Analyzer;
import locator.aux.extractor.core.parser.BasicBlock;
import locator.aux.extractor.core.parser.Use;
import locator.aux.extractor.core.parser.Variable;
import locator.common.java.JavaFile;

public class ClassifierFeature {

	private final static char SEP = '\t';
	
	private static VariableCollectorVisitor visitor = new VariableCollectorVisitor();
	private static List<Feature> featureConfig = new LinkedList<>();
	
	static {
		// can configure flexibly
		featureConfig.add(LineNumber.getInstance());
		featureConfig.add(ColumnNumber.getInstance());
		featureConfig.add(FileName.getInstance());
		featureConfig.add(MethodName.getInstance());
		featureConfig.add(VarName.getInstance());
		featureConfig.add(VarType.getInstance());
		featureConfig.add(LastAssign.getInstance());
		featureConfig.add(MethodSize.getInstance());
		featureConfig.add(IfUse.getInstance());
	}
	
	public static int getFeatureIndex(Class<? extends Feature> clazz) {
		for(int index = 0; index < featureConfig.size(); index ++) {
			if(featureConfig.get(index).getClass() == clazz) {
				return index;
			}
		}
		return -1;
	}
	
	public static String getFeatureHeader() {
		StringBuffer header = new StringBuffer();
		for(Feature feature : featureConfig) {
			header.append(feature.getName() + SEP);
		}
		return header.toString() + "predicate" + SEP + "label";
	}
	
	public static List<String> getFeature(String srcBase, String relJavaFile, int line, String predicate, String label) {
		List<String> features = new LinkedList<>();
		ASTNode node = JavaFile.genASTFromSource(predicate, ASTParser.K_EXPRESSION);
		visitor.reset();
		node.accept(visitor);
		
		Set<String> variable = visitor.getAllVariables();
		BasicBlock basicBlock = Analyzer.analyze(relJavaFile, srcBase);
		Set<Use> uses = basicBlock.getVariableUses(line);
		
		Set<String> alreadyVisited = new HashSet<>();
		for(Use use : uses) {
			Variable v = use.getVariableDefine();
			if(alreadyVisited.contains(v.getName()) || !variable.contains(v.getName())) {
				continue;
			}
			alreadyVisited.add(v.getName());
			StringBuffer buffer = new StringBuffer();
			for(Feature feature : featureConfig) {
				buffer.append(feature.extractFeature(use).getStringFormat() + SEP);
			}
			buffer.append(predicate + SEP + label);
			features.add(buffer.toString());
		}
		
		return features;
	}
	
	
	private static class VariableCollectorVisitor extends ASTVisitor {
		
		private Set<String> variables = new HashSet<>();
		
		public Set<String> getAllVariables() {
			return variables;
		}

		public void reset() {
			variables = new HashSet<>();
		}
		
		public boolean visit(FieldAccess node) {
			variables.add(node.getName().getFullyQualifiedName());
			return true;
		}
		
		public boolean visit(SimpleName node) {
			Pattern clazz = Pattern.compile("^[A-Z][A-Za-z_]*");
			Pattern constant=Pattern.compile("^[A-Z][A-Z_]*");
			String name = node.getFullyQualifiedName();
			if (constant.matcher(name).matches() || clazz.matcher(name).matches()) {
				return true;
			}
			variables.add(name);
			return true;
		}
		
		public boolean visit(QualifiedName node) {
			Pattern clazz = Pattern.compile("^[A-Z][A-Za-z_]*");
			Pattern constant=Pattern.compile("^[A-Z][A-Z_]*");
			// skip some constant and static access
			if (constant.matcher(node.getName().getFullyQualifiedName()).find()
					|| clazz.matcher(node.getQualifier().getFullyQualifiedName()).find()) {
				return true;
			}
			SimpleName name = node.getName();
			variables.add(name.getFullyQualifiedName());
			return true;
		}
		
	}
	
}
