package com.tao.ai.rl.tabular.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.AgentForAnylogic;
import com.tao.ai.rl.tabular.anylogic.DecisionRule;
import com.tao.ai.rl.tabular.anylogic.TabularModelForAnylogic;
import com.tao.ai.rl.tabular.utilities.TabularModelUtilities;

class AgentTest {

	@Test
	void test() {
		////////////////////////
		//State definition
		////////////////////////
		//queue length
		Double[] stateFeature1=new Double[]{2.5,7.5,12.5,17.5,22.5,27.5,32.5,37.5};
		//percentage of A in queue
		Double[] stateFeature2=new Double[]{12.5,37.5,62.5,87.5};
		//percentage of B in queue
		Double[] stateFeature3=new Double[]{12.5,37.5,62.5,87.5};
		//percentage of lots in queue 0<=revist counter<3
		Double[] stateFeature4=new Double[]{12.5,37.5,62.5,87.5};
		//percentage of lots in queue 3<=revist counter<6
		Double[] stateFeature5=new Double[]{12.5,37.5,62.5,87.5};
		//percentage of lots in queue 6<=revist counter<9
		Double[] stateFeature6=new Double[]{12.5,37.5,62.5,87.5};
		
		List<Double[]> stateFeatureSpace=new ArrayList<>();
		stateFeatureSpace.add(stateFeature1);
		stateFeatureSpace.add(stateFeature2);
		stateFeatureSpace.add(stateFeature3);
		stateFeatureSpace.add(stateFeature4);
		stateFeatureSpace.add(stateFeature5);
		stateFeatureSpace.add(stateFeature6);
		
		////////////////////////
		//Action definition
		////////////////////////
		List<DecisionRule> actionSet=new ArrayList<>();
		actionSet.add(DecisionRule.FIFO);
		actionSet.add(DecisionRule.SPT);
		actionSet.add(DecisionRule.Random);
		actionSet.add(DecisionRule.ESFIFO);
		actionSet.add(DecisionRule.ESSPT);
		actionSet.add(DecisionRule.ESRandom);
		actionSet.add(DecisionRule.LSFIFO);
		actionSet.add(DecisionRule.LSSPT);
		actionSet.add(DecisionRule.LSRandom);
		
		
		///////////////////////////
		//RL model and agent
		///////////////////////////
		TabularModelForAnylogic rlModel=new TabularModelForAnylogic(stateFeatureSpace,actionSet);
		
		State state=new State();		
		List<Object> features=new ArrayList<>();
		features.add(7.5);
		features.add(37.5);
		features.add(62.5);
		features.add(12.5);
		features.add(62.5);
		features.add(12.5);
		state.setFeatures(features);
		
		Action act=new Action();		
		List<Object> features1=new ArrayList<>();
		features1.add(DecisionRule.ESSPT);
		act.setFeatures(features1);
		
		rlModel.updateQValue(state, act, 567);
		
		state=new State();		
		features=new ArrayList<>();
		features.add(7.5);
		features.add(12.5);
		features.add(62.5);
		features.add(12.5);
		features.add(62.5);
		features.add(12.5);
		state.setFeatures(features);
		
		act=new Action();		
		features1=new ArrayList<>();
		features1.add(DecisionRule.FIFO);
		act.setFeatures(features1);
		
		rlModel.updateQValue(state, act, 112);
		
		
		
		AgentForAnylogic rlAgent = new AgentForAnylogic(rlModel);
		rlAgent.setGreedy(0);;
		Double[] curstate = new Double[] {7.8,37.9,61.4,10.5,59.6,11.8};
		DecisionRule rule = rlAgent.selectAction(curstate,8.96);
		Double[] nextState = new Double[] {7.8,12.9,61.4,10.5,59.6,11.8};
		DecisionRule rule1 = rlAgent.selectAction(nextState,32.0);
		
		double oldValue = 567;
		double targetValue = 32.0+ 112*0.9;
		double newValue= oldValue+0.1*(targetValue-oldValue );
		
		
		state=TabularModelUtilities.convertToState(rlModel, curstate);
		
		Action action = TabularModelUtilities.convertToAction(rlModel, new DecisionRule[] {DecisionRule.ESSPT});
		
		double actual = rlModel.getQValue(state, action);

		assertEquals(newValue, actual);
	}

}
