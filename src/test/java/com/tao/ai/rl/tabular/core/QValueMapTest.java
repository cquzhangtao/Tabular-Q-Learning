package com.tao.ai.rl.tabular.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.DecisionRule;

class QValueMapTest {
	QValueMap map=new QValueMap();
	
	@BeforeEach
	void initMap() {
		map=new QValueMap();
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
		map.put(pair1, 1.11);
		
		
		
		state=new State();		
		features=new ArrayList<>();
		features.add(156.0);
		features.add(889.0);
		state.setFeatures(features);
		
		act=new Action();		
		features1=new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(369.0);
		act.setFeatures(features1);
		
		StateActionPair pair2=new StateActionPair(state,act);
		map.put(pair2, 2.22);
	}

	@Test
	void testGetStateAction() {
		
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
		
		assertEquals(map.get(state, act),1.11);

	}

	@Test
	void testContains() {
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
		assertTrue(map.contains(state, act));
	}

	@Test
	void testPutStateActionDouble() {
		State state = new State();		
		ArrayList features = new ArrayList<>();
		features.add(156.0);
		features.add(889.0);
		state.setFeatures(features);
		
		Action act = new Action();		
		ArrayList features1 = new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(369.0);
		act.setFeatures(features1);
		
		map.put(state, act, 888.88);
		
		state = new State();		
		features = new ArrayList<>();
		features.add(156.0);
		features.add(889.0);
		state.setFeatures(features);
		
		act = new Action();		
		features1 = new ArrayList<>();
		features1.add(DecisionRule.Customer1);
		features1.add(369.0);
		act.setFeatures(features1);
		assertEquals(map.get(state, act),888.88);
	}

}
