package com.tao.ai.rl.tabular.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import com.tao.ai.rl.tabular.core.State;
import com.tao.ai.rl.tabular.core.TabularModel;
import com.tao.ai.rl.tabular.core.Action;
class TabularModelUtilitiesTest {

	@Test
	void test() {
		
	
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

}
