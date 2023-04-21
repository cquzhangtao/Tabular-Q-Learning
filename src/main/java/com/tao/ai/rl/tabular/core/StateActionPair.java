package com.tao.ai.rl.tabular.core;

import java.io.Serializable;

public class StateActionPair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1521076513325688012L;
	private State state;
	private Action action;
	
	
	public StateActionPair(State state,Action action) {
		this.state=state;
		this.action=action;
	}
	
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
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

		accumulator ^= state.hashCode();
		accumulator ^= action.hashCode();

		return accumulator;
	}
	
	public String toString() {
		if(state==null) {
			return "[null,"+action.toString()+"]";
		}
		return "["+state.toString()+","+action.toString()+"]";
	}
}
