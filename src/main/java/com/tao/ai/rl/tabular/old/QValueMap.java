package com.tao.ai.rl.tabular.old;

import java.util.HashMap;
import java.util.List;

public class QValueMap extends HashMap<StateActionPair,Double>{

	public double get(List<Job>state,Job action) {
		StateActionPair pair=new StateActionPair(state,action);
		return this.get(pair)==null?Double.NEGATIVE_INFINITY:this.get(pair);
	}
	
	public boolean contains(List<Job>state,Job action) {
		StateActionPair pair=new StateActionPair(state,action);
		return this.containsKey(pair);
	}
	
	public void put(List<Job>state,Job action,double value) {
		StateActionPair pair=new StateActionPair(state,action);
		this.put(pair, value);
	}
}
