package com.tao.ai.rl.tabular.old;

import java.util.HashSet;
import java.util.List;

public class StateActionPairSet extends HashSet<StateActionPair>{
	
	public boolean contains(List<Job>state,Job action) {
		StateActionPair pair=new StateActionPair(state,action);
		
		return this.contains(pair);
	}
	
	public StateActionPair get(List<Job>state,Job action) {
		return new StateActionPair(state,action);

	}

}
