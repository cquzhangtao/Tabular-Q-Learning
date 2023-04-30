package com.tao.ai.rl.tabular.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.DecisionRule;

class ArrayUtilitiesTest {

	@Test
	void testConverItemTo2D() {
		List<DecisionRule> rules = new ArrayList<DecisionRule>();
		rules.add(DecisionRule.RL);
		rules.add(DecisionRule.FIFO);
		rules.add(DecisionRule.SPT);
		rules.add(DecisionRule.Random);
		rules.add(DecisionRule.ESFIFO);
		rules.add(DecisionRule.ESSPT);
		rules.add(DecisionRule.ESRandom);
		rules.add(DecisionRule.LSFIFO);
		rules.add(DecisionRule.LSSPT);
		rules.add(DecisionRule.LSRandom);
		List<Object[]> result = ArrayUtilities.converItemTo2D(rules);
		assertEquals(result.get(1).length, 1);
		assertEquals(result.get(1)[0], DecisionRule.FIFO);
	}

	@Test
	void testConverTo2DY() {
		List<DecisionRule> rules = new ArrayList<DecisionRule>();
		rules.add(DecisionRule.RL);
		rules.add(DecisionRule.FIFO);
		rules.add(DecisionRule.SPT);
		rules.add(DecisionRule.Random);
		rules.add(DecisionRule.ESFIFO);
		rules.add(DecisionRule.ESSPT);
		Object[][] result = ArrayUtilities.converTo2DY(rules);
		assertEquals(result.length, 1);
		assertEquals(result[0][3], DecisionRule.Random);
		
	}

	@Test
	void testConverTo2DX() {
		// queue length
		Double[] stateFeature1=new Double[]{2.5,7.5,12.5,17.5,22.5,27.5,32.5,37.5};
		// percentage of A in queue
		Double[] stateFeature2=new Double[]{12.5,37.5,62.5,87.5};
		// percentage of B in queue
		Double[] stateFeature3=new Double[]{12.5,37.5,62.5,87.5};
		// percentage of lots in queue 0<=revist counter<3
		Double[] stateFeature4=new Double[]{12.5,37.5,62.5,87.5};
		// percentage of lots in queue 3<=revist counter<6
		Double[] stateFeature5=new Double[]{12.5,37.5,62.5,87.5};
		// percentage of lots in queue 6<=revist counter<9
		Double[] stateFeature6=new Double[]{12.5,37.5,62.5,87.5};

		List<Double[]> stateFeatureSpace=new ArrayList<>();
		stateFeatureSpace.add(stateFeature1);
		stateFeatureSpace.add(stateFeature2);
		stateFeatureSpace.add(stateFeature3);
		stateFeatureSpace.add(stateFeature4);
		stateFeatureSpace.add(stateFeature5);
		stateFeatureSpace.add(stateFeature6);
		
		Object[][] result = ArrayUtilities.converTo2DX(stateFeatureSpace);
		assertEquals(result.length, 6);
		assertEquals(result[2][3], 87.5);
	}

}
