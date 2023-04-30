package com.tao.ai.rl.tabular.anylogic;

import java.util.ArrayList;
import java.util.List;

import com.tao.ai.rl.tabular.core.Agent;
import com.tao.ai.rl.tabular.core.Agent2;
import com.tao.ai.rl.tabular.utilities.ArrayUtilities;

public class AgentForAnylogic2 extends Agent2 {

	public AgentForAnylogic2(TabularModelForAnylogic2 model) {
		super(model);

	}
	
	public AgentForAnylogic2(String model) {
		super(model);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures) {
		return selectAction(stateFeatures,0);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures, double reward) {
		return selectAction(stateFeatures,((TabularModelForAnylogic2)getModel()).getActions(),reward);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures, List<DecisionRule> rules, double reward) {
		int idx= super.selectAction(stateFeatures,ArrayUtilities.converItemTo2D( rules), reward);
		return rules.get(idx);
	}
	
	
	
	

}
