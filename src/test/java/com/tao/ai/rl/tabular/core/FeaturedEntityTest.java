package com.tao.ai.rl.tabular.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.DecisionRule;

class FeaturedEntityTest {

	@Test
	void testSetFeatures() {
		FeaturedEntity ent1=new FeaturedEntity();
		
		List<Object> features=new ArrayList<>();
		features.add(56.0);
		features.add(88.0);
		ent1.setFeatures(features);
		
		FeaturedEntity ent2=new FeaturedEntity();
		
		List<Object> features2=new ArrayList<>();
		features2.add(56.0);
		features2.add(88.0);
		ent2.setFeatures(features2);
		
		assertTrue(ent1.equals(ent2));
		
		FeaturedEntity ent3=new FeaturedEntity();
		
		List<Object> features3=new ArrayList<>();
		features3.add(56.0);
		features3.add(809.0);
		ent3.setFeatures(features3);
		assertFalse(ent1.equals(ent3));
		
		
	}
	
	@Test
	void testSetFeatures1() {
		FeaturedEntity ent1=new FeaturedEntity();
		
		List<Object> features=new ArrayList<>();
		features.add(DecisionRule.Customer1);
		features.add(DecisionRule.Customer2);
		ent1.setFeatures(features);
		
		FeaturedEntity ent2=new FeaturedEntity();
		
		List<Object> features2=new ArrayList<>();
		features2.add(DecisionRule.Customer1);
		features2.add(DecisionRule.Customer2);
		ent2.setFeatures(features2);
		
		assertTrue(ent1.equals(ent2));
		
		FeaturedEntity ent3=new FeaturedEntity();
		
		List<Object> features3=new ArrayList<>();
		features3.add(DecisionRule.Customer5);
		features3.add(DecisionRule.Customer6);
		ent3.setFeatures(features3);
		assertFalse(ent1.equals(ent3));
		
		
	}
	
	@Test
	void testSetFeatures2() {
		FeaturedEntity ent1=new FeaturedEntity();
		
		List<Object> features=new ArrayList<>();
		features.add(DecisionRule.Customer1);
		features.add(36.0);
		ent1.setFeatures(features);
		
		FeaturedEntity ent2=new FeaturedEntity();
		
		List<Object> features2=new ArrayList<>();
		features2.add(DecisionRule.Customer1);
		features2.add(36.0);
		ent2.setFeatures(features2);
		
		assertTrue(ent1.equals(ent2));
		
		FeaturedEntity ent3=new FeaturedEntity();
		
		List<Object> features3=new ArrayList<>();
		features3.add(DecisionRule.Customer5);
		features3.add(36.0);
		ent3.setFeatures(features3);
		assertFalse(ent1.equals(ent3));
		
		
	}

}
