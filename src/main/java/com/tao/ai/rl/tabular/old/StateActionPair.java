package com.tao.ai.rl.tabular.old;

import java.util.List;

public class StateActionPair {

	private State state;
	private Job action;
	
	
	public StateActionPair(List<Job>state,Job action) {
		this.state=new State(state);
		this.action=action;
	}
	
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Job getAction() {
		return action;
	}
	public void setAction(Job action) {
		this.action = action;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StateActionPair) {
			StateActionPair pair=(StateActionPair) obj;
			return pair.getState().equals(state)&&pair.getAction().equals(action);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		
		int accumulator = 0;
		//for (Job obj : jobs) {
			accumulator ^= state.hashCode();
			accumulator ^= action.hashCode();
		//}
		return accumulator;
	}
}
