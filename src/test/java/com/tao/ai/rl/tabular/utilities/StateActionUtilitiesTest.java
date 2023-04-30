package com.tao.ai.rl.tabular.utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StateActionUtilitiesTest {

	@Test
	void testGetIdxFromLevels() {
		int[] count = new int[] {2,5,3,4};
		assertEquals( StateActionUtilities.getIdxFromLevels(new int[] {0,1,1,3},count ),19);
		assertEquals( StateActionUtilities.getIdxFromLevels(new int[] {0,0,0,0},count ),0);
		assertEquals( StateActionUtilities.getIdxFromLevels(new int[] {1,4,2,3},count ),119);
		assertEquals( StateActionUtilities.getIdxFromLevels(new int[] {1,3,1,3},count ),103);
	}

}
