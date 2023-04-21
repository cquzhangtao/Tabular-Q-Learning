package com.tao.ai.rl.tabular.anylogic;

import java.util.ArrayList;
import java.util.List;

import com.tao.ai.rl.tabular.core.Agent;

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
		int idx= super.selectAction(stateFeatures,converItemTo2D( rules), reward);
		return rules.get(idx);
	}
	
	private static List<Object[]> converItemTo2D(List<DecisionRule> rules){
		List<Object[]> results=new ArrayList<>();
		
		for(DecisionRule rule:rules) {
			Object[] ruleA=new Object[1];
			ruleA[0]=rule;
			results.add(ruleA);
		}

		return results;
	}
	
	

}
