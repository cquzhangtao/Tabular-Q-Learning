package com.tao.ai.rl.tabular.core;

import java.io.Serializable;
import java.util.HashMap;


public class QValueMap extends HashMap<StateActionPair,Double> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3597256691928292277L;

	public double get(State state,Action action) {

		StateActionPair pair=new StateActionPair(state,action);
		if(this.get(pair)==null) {
			System.out.println("Error in QValueMap " +pair);
			return 0;
		}
		return this.get(pair);
	}
	
	public boolean contains(State state,Action action) {
		StateActionPair pair=new StateActionPair(state,action);
		return this.containsKey(pair);
	}
	
	public void put(State state,Action action,double value) {
		StateActionPair pair=new StateActionPair(state,action);
		this.put(pair, value);
	}
}
