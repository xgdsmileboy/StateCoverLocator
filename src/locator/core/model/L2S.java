/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.pku.sei.conditon.dedu.DeduConditionVisitor;
import edu.pku.sei.conditon.dedu.DeduFeatureGenerator;
import edu.pku.sei.conditon.dedu.InvockerBU;
import edu.pku.sei.conditon.dedu.pred.ExprGenerator;
import edu.pku.sei.conditon.dedu.pred.ExprPredItem;
import edu.pku.sei.conditon.dedu.pred.GenExprItem;
import edu.pku.sei.conditon.dedu.pred.OriPredItem;
import edu.pku.sei.conditon.dedu.pred.VarPredItem;
import edu.pku.sei.conditon.ds.VariableInfo;
import edu.pku.sei.conditon.util.Cmd;
import edu.pku.sei.conditon.util.StringUtil;
import edu.pku.sei.conditon.util.TypeUtil;
import locator.common.config.Constant;
import locator.common.java.Pair;
import locator.common.java.Subject;
import locator.common.util.CmdFactory;
import locator.common.util.ExecuteCommand;
import locator.core.run.path.LineInfo;

/**
 * @author Jiajun
 * @date Dec 19, 2017
 */
public class L2S extends Model {

	public L2S() {
		super(Constant.STR_ML_HOME + "/model", Constant.STR_ML_EXP_OUT_FILE_PATH,
				Constant.STR_ML_PREDICT_EXP_PATH);
//		super(Constant.STR_ML_HOME + "/model/l2s", Constant.STR_ML_EXP_OUT_FILE_PATH + "/l2s",
//				Constant.STR_ML_PREDICT_EXP_PATH + "/l2s");
		__name__ = "@L2S ";
	}

	@Override
	public void trainModel(Subject subject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Map<String, List<Pair<String, String>>>> predict(Subject subject, List<String> varFeaturs,
			List<String> exprFeatures, Map<String, LineInfo> lineInforMapping) {
		
//		String name = subject.getName();
//		int id = subject.getId();
//		String srcRoot = subject.getHome() + subject.getSsrc();
//		String testRoot = subject.getHome() + subject.getTsrc();
		String relJavaFile = "org/apache/commons/math3/fraction/Fraction.java";
		int line = 212;
//		
//		name = "math";
//		id = 1;
//		srcRoot = "/home/jiajun/d4j/test_pojs/math/math_1_buggy/src/main/java";
//		testRoot = "/home/jiajun/d4j/test_pojs/math/math_1_buggy/src/test/java";
//		relJavaFile = "org/apache/commons/math3/fraction/Fraction.java";
//		line = 212;
		
		try {
			dummyMethod(subject, relJavaFile, line, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public void dummyMethod(Subject subject, String filePath, int line, int ithSuspicous) throws IOException, InterruptedException {
		String projectName = subject.getName(); 
		int id = subject.getId(); 
		String srcRoot = subject.getHome() + subject.getSsrc(); 
		String testRoot = subject.getHome() + subject.getTsrc();
		String projAndBug = projectName + '_' + id;
		String model = InvockerBU.bugToModelMap.get(projAndBug);
		final String PREDICTOR_ROOT = Constant.STR_ML_HOME;
		
		
//		List<String> varFeatures = DeduFeatureGenerator.generateVarFeatures(projAndBug, model, srcRoot, testRoot, filePath, line);
		ASTNode hitNode = DeduFeatureGenerator.getHitNode(projAndBug, model, srcRoot, testRoot, filePath, line);

		Map<String, VariableInfo> allVarInfoMap = DeduFeatureGenerator.getAllVariablesMap();
		
		//varname -> var feature prefix
		Map<String, String> varToFeaPrefixMap = DeduFeatureGenerator.getVarToVarFeaturePrefixMap(projAndBug, model, srcRoot, testRoot, filePath, line);
		
		//pred to its oripreditem
		String path = PREDICTOR_ROOT + "/input/" + projectName + "/" + model + "/expr/" + model + ".allpred.csv";
		Map<String, OriPredItem> allOriPredicates = InvockerBU.loadAllOriPredicate(path);
		
		Map<String, Integer> pos0TimeMap = InvockerBU.getPos0TimeMap(allOriPredicates);

		List<String> varFeatures = InvockerBU.generateV0Lines(varToFeaPrefixMap, pos0TimeMap);
		InvockerBU.wirteBUPred(projectName, String.valueOf(id), "v0", varFeatures);
		double startTime = System.currentTimeMillis();
		ExecuteCommand.executeL2SPredict(subject, "v0");
		double endTime = System.currentTimeMillis();
		System.out.println("FIRST VAR TIME: " + (endTime - startTime)/1000 + " s" );
		
		List<VarPredItem> vars = InvockerBU.getSortedVarList(allVarInfoMap, projectName, model, 0);
		
		String fileName = filePath.substring(filePath.lastIndexOf("/"));
		fileName = fileName.substring(0, fileName.length() - 5);//remove '.java'
		
		List<ExprPredItem> allExprs = new ArrayList<>();
		
		List<VarPredItem> varsForZero = vars;
		if(vars.size() > InvockerBU.TOP_VAR_OF_POSITION){
			varsForZero = vars.subList(0, InvockerBU.TOP_VAR_OF_POSITION);
		}
		
		for(VarPredItem varItemAtZero: varsForZero){
			String curVarFea = varToFeaPrefixMap.get(varItemAtZero.getLiteral());
			if(curVarFea == null || curVarFea.length() == 0){
				continue;
			}
			//occpostime
			String posZeroTime = ""+(pos0TimeMap.containsKey(varItemAtZero.getLiteral()) ? pos0TimeMap.get(varItemAtZero.getLiteral()) : 0);
			
			String v0Line = StringUtil.connectMulty(InvockerBU.del, curVarFea, posZeroTime, "?");
			InvockerBU.wirteBUPred(projectName, String.valueOf(id), "expr", Arrays.asList(v0Line));
			
			startTime = System.currentTimeMillis();
			ExecuteCommand.executeL2SPredict(subject, "expr", InvockerBU.EXPR_PRED_NUM);
			endTime = System.currentTimeMillis();
			System.out.println("EXPR TIME: " + (endTime - startTime)/1000 + " s" );
			
			List<ExprPredItem> exprs = InvockerBU.readExprCSV(fileName, projectName, model);//sorted
			
			List<ExprPredItem> filtedExprs = new ArrayList<>();
			
			for(ExprPredItem exprItem: exprs){
			
				System.out.println(exprItem);
				
				if(! ExprGenerator.isLegalExprForV0(varItemAtZero, exprItem, allOriPredicates)){
					continue;
				}
				
				String predFeature = exprItem.getExprFeature();
				Map<Integer, List<VarPredItem>> predVars = new HashMap<>();//MAP: position => varitem list
				List<VarPredItem> v0List = new ArrayList<>();
				v0List.add(varItemAtZero);
				predVars.put(0, v0List);
				
				for(int n = 1; n < exprItem.getPositionNum(); n++){
					List<String> varFeatersAtN = new ArrayList<>();
					
					for(VarPredItem varItemLatter: vars){
						
						boolean argued = DeduConditionVisitor.usedAsParam(exprItem.getTemVarCompletPred(), varItemLatter.getLiteral());
						String argUsed = argued ? "true" : "false";
						
						boolean typeFit = TypeUtil.isLegalVarAtPosition(exprItem.getPred(), n, varItemLatter.getInfo(), allOriPredicates); 
						String vNfit = typeFit ? "true" : "false";
						
						String vNUsed = (varItemLatter == varItemAtZero) ? "true" : "false";

						String vNPrefix = varToFeaPrefixMap.get(varItemLatter.getLiteral());
						
						assert vNPrefix != null;
						
						String occuredTime = "" + (DeduConditionVisitor.getVarOccurredTimeAtTheExprPosion(varItemLatter.getLiteral(), exprItem.getPred(), n, allOriPredicates));
						
						String varNLine = StringUtil.connectMulty(InvockerBU.del, vNPrefix, predFeature, argUsed, vNfit, occuredTime, vNUsed, "" + n, "?");
						
						varFeatersAtN.add(varNLine);
					}
					
					InvockerBU.wirteBUPred(projectName, String.valueOf(id), "var", varFeatersAtN);
					
					startTime = System.currentTimeMillis();
					ExecuteCommand.executeL2SPredict(subject, "var");
					endTime = System.currentTimeMillis();
					System.out.println(varItemAtZero.getLiteral() + ": " + exprItem.getPred() + " POS " + n + " TIME: " + (endTime - startTime)/1000 + " s" );
					
					List<VarPredItem> varsAtN = InvockerBU.getSortedVarList(allVarInfoMap, projectName, model, n);
					predVars.put(n, varsAtN);
					
				}//for(int n = 1; n < exprItem.getPositionNum(); n++)
				
				exprItem.setPredVars(predVars);
				
				if(predVars.size() == exprItem.getPositionNum()){
					filtedExprs.add(exprItem);
				}

			}//for(ExprPredItem exprItem: exprs)
			
			allExprs.addAll(filtedExprs);
			
		}//for(VarPredItem varItemAtZero: vars)
		
		ExprGenerator generator = new ExprGenerator(hitNode, allExprs, allOriPredicates);
		List<GenExprItem> res = generator.generateExpr();
		
		String proj_Bug_ithSusp = projAndBug + "_" + ithSuspicous;

		InvockerBU.dumpResult(projectName, String.valueOf(id), proj_Bug_ithSusp, res);
	}
	
	public static void main(String[] args) {
		Constant.PROJECT_HOME = "/home/jiajun/d4j/test_pojs";
		Subject subject = new Subject("math", 12, "/src/main/java", "/src/test/java", "/target/classes", "/target/test-classes", null);
		L2S l2s = new L2S();
		l2s.predict(subject, null, null, null);
	}

}
