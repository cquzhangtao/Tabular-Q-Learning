package com.tao.ai.rl.tabular.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.tao.ai.rl.tabular.anylogic.DecisionRule;
import com.tao.ai.rl.tabular.core.Action;
import com.tao.ai.rl.tabular.core.State;
import com.tao.ai.rl.tabular.core.TabularModel;

class TabularModelUtilitiesTest2 {

	@Test
	void testGenerateQTable() {
		Double[][]stateFeatureSpace=new Double[][] {{2.0,33.0},{10.0,21.0}};
		Double[][] actionFeatureSpace=new Double[][] {{23.0,33.0},{40.0,58.0}};
		TabularModel model=new TabularModel(stateFeatureSpace, actionFeatureSpace) ;
		TabularModelUtilities.generateQTable(model);
		assertEquals(model.getqTable().size(),16);
	}

	@Test
	void testFullCombination() {
		List<State> states = TabularModelUtilities.fullCombination(new Object[][] {{11,12,13,14,15},{21},{31,32,33}}, State::new);
		assertEquals(states.size(), 15);
		List<Action> actions = TabularModelUtilities.fullCombination(new Object[][] {{11,12,13},{21},{31,32,33}}, Action::new);
		assertEquals(actions.size(), 9);
		TabularModel model=new TabularModel(states,actions);
		assertEquals(model.getqTable().size(), 135);
		actions = TabularModelUtilities.fullCombination(new Object[][] {{11,12,13}}, Action::new);
		assertEquals(actions.size(), 3);
		actions = TabularModelUtilities.fullCombination(new Object[][] {}, Action::new);
		assertEquals(actions.size(), 0);
	}

	@Test
	void testConvertToState() {
		Double[][]stateFeatureSpace=new Double[][] {{2.0,33.0},{10.0,21.0}};
		Double[][] actionFeatureSpace=new Double[][] {{23.0,33.0},{40.0,58.0}};
		TabularModel model=new TabularModel(stateFeatureSpace, actionFeatureSpace) ;
		Double[] actionFeatures=new Double[] {24.0,57.0};
		State act=TabularModelUtilities.convertToState(model, actionFeatures);
		assertEquals(act.getFeatures().get(0), 33.0);
		assertEquals(act.getFeatures().get(1), 21.0);
	}

	@Test
	void testConvertToAction() {
		Double[][]stateFeatureSpace=new Double[][] {{2.0,33.0},{10.0,21.0}};
		Double[][] actionFeatureSpace=new Double[][] {{23.0,33.0},{40.0,58.0}};
		TabularModel model=new TabularModel(stateFeatureSpace, actionFeatureSpace) ;
		Double[] actionFeatures=new Double[] {24.0,57.0};
		Action act=TabularModelUtilities.convertToAction(model, actionFeatures);
		assertEquals(act.getFeatures().get(0), 23.0);
		assertEquals(act.getFeatures().get(1), 58.0);
	}

	@Test
	void testGetClosedLevel() {
		Double item=123.0;
		Double levels[] =new Double[] {5.0,20.0,205.0};
		assertEquals(TabularModelUtilities.getClosedLevel(item,levels),205.0);
		
		DecisionRule rule=DecisionRule.ES;
		DecisionRule[] rules=new DecisionRule[] {DecisionRule.ES,DecisionRule.Customer1};
		assertEquals(TabularModelUtilities.getClosedLevel(rule,rules),DecisionRule.ES);
	}

}
