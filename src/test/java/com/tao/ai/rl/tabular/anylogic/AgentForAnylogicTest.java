package com.tao.ai.rl.tabular.anylogic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.core.Action;
import com.tao.ai.rl.tabular.core.State;

class AgentForAnylogicTest {

	@Test
	void testSelectActionDoubleArray() {

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
		
		
		
		AgentForAnylogic rlAgent = new AgentForAnylogic(rlModel);
		Double[] stateArray = new Double[] {7.8,37.9,61.4,10.5,59.6,11.8};
		rlAgent.setGreedy(0);
		DecisionRule rule = rlAgent.selectAction(stateArray);
		assertEquals(rule, DecisionRule.ESSPT);
		rlAgent.setGreedy(1);
		rule = rlAgent.selectAction(stateArray);
		assertNotEquals(rule, DecisionRule.ESSPT);
	}

	
}
