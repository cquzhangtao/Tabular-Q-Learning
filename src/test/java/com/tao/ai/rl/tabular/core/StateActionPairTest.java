package com.tao.ai.rl.tabular.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.DecisionRule;

class StateActionPairTest {

	@Test
	void testEqualsObject() {
		State state=new State();		
		List<Object> features=new ArrayList<>();
		features.add(56.0);
		features.add(88.0);
		state.setFeatures(features);
		
		Action act=new Action();		
		List<Object> features1=new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(36.0);
		act.setFeatures(features1);
		
		StateActionPair pair1=new StateActionPair(state,act);
		
		state=new State();		
		features=new ArrayList<>();
		features.add(56.0);
		features.add(88.0);
		state.setFeatures(features);
		
		act=new Action();		
		features1=new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(36.0);
		act.setFeatures(features1);
		
		StateActionPair pair2=new StateActionPair(state,act);
		
		assertTrue(pair2.equals(pair1));
		
	}
	
	@Test
	void testEqualsObject2() {
		State state=new State();		
		List<Object> features=new ArrayList<>();
		features.add(56.0);
		features.add(88.0);
		state.setFeatures(features);
		
		Action act=new Action();		
		List<Object> features1=new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(36.0);
		act.setFeatures(features1);
		
		StateActionPair pair1=new StateActionPair(state,act);
		
		state=new State();		
		features=new ArrayList<>();
		features.add(56.0);
		features.add(889.0);
		state.setFeatures(features);
		
		act=new Action();		
		features1=new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(36.0);
		act.setFeatures(features1);
		
		StateActionPair pair2=new StateActionPair(state,act);
		
		assertFalse(pair2.equals(pair1));
		
	}


}
