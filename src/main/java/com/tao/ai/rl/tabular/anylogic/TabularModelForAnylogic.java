package com.tao.ai.rl.tabular.anylogic;

import java.util.List;

import com.tao.ai.rl.tabular.core.TabularModel;

public class TabularModelForAnylogic extends TabularModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 144161225823821016L;
	private List<DecisionRule> actions;

	public TabularModelForAnylogic(List<Double[]>stateFeatureSpace,List<DecisionRule> actions) {	
		
		super(converTo2DX(stateFeatureSpace), converTo2DY(actions));
		this.setActions(actions);

	}
	
	private static Object[][] converTo2DY(List<DecisionRule> actions){
		Object[][] actionFeatures=new Object[1][];
		actionFeatures[0]=actions.toArray();
		return actionFeatures;
	}
	
	private static Object[][] converTo2DX(List<Double[]> stateFeatureSpace){
		Object[][] stateFeatures=new Object[stateFeatureSpace.size()][];
		int idx=0;
		for(Double[] featureLevels:stateFeatureSpace) {
			stateFeatures[idx]=featureLevels;
			idx++;
		}
		return stateFeatures;
	}

	public List<DecisionRule> getActions() {
		return actions;
	}

	public void setActions(List<DecisionRule> actions) {
		this.actions = actions;
	}
	
	
	

}
