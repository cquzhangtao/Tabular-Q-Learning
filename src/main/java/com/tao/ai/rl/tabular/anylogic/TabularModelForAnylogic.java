package com.tao.ai.rl.tabular.anylogic;

import java.util.List;

import com.tao.ai.rl.tabular.core.TabularModel;
import com.tao.ai.rl.tabular.utilities.ArrayUtilities;

public class TabularModelForAnylogic extends TabularModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 144161225823821016L;
	private List<DecisionRule> actions;

	public TabularModelForAnylogic(List<Double[]>stateFeatureSpace,List<DecisionRule> actions) {	
		
		super(ArrayUtilities.converTo2DX(stateFeatureSpace), ArrayUtilities.converTo2DY(actions));
		this.setActions(actions);

	}
	


	public List<DecisionRule> getActions() {
		return actions;
	}

	public void setActions(List<DecisionRule> actions) {
		this.actions = actions;
	}
	
	
	

}
