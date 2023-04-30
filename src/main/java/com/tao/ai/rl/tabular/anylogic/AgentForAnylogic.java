package com.tao.ai.rl.tabular.anylogic;

import java.util.ArrayList;
import java.util.List;

import com.tao.ai.rl.tabular.core.Agent;
import com.tao.ai.rl.tabular.utilities.ArrayUtilities;

public class AgentForAnylogic extends Agent {

	public AgentForAnylogic(TabularModelForAnylogic model) {
		super(model);

	}
	
	public AgentForAnylogic(String model) {
		super(model);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures) {
		return selectAction(stateFeatures,0);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures, double reward) {
		return selectAction(stateFeatures,((TabularModelForAnylogic)getModel()).getActions(),reward);
	}
	
	public DecisionRule selectAction(Double[] stateFeatures, List<DecisionRule> rules, double reward) {
		int idx= super.selectAction(stateFeatures,ArrayUtilities.converItemTo2D( rules), reward);
		return rules.get(idx);
	}
	
	
	
	

}
