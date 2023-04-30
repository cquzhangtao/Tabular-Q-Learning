package com.tao.ai.rl.tabular.utilities;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtilities {
	
	public static List<Object[]> converItemTo2D(List<? extends Object> rules){
		List<Object[]> results=new ArrayList<>();
		
		for(Object rule:rules) {
			Object[] ruleA=new Object[1];
			ruleA[0]=rule;
			results.add(ruleA);
		}

		return results;
	}
	
	public static Object[][] converTo2DY(List<? extends Object> actions){
		Object[][] actionFeatures=new Object[1][];
		actionFeatures[0]=actions.toArray();
		return actionFeatures;
	}
	
	public static Object[][] converTo2DX(List<Double[]> stateFeatureSpace){
		Object[][] stateFeatures=new Object[stateFeatureSpace.size()][];
		int idx=0;
		for(Double[] featureLevels:stateFeatureSpace) {
			stateFeatures[idx]=featureLevels;
			idx++;
		}
		return stateFeatures;
	}

}
