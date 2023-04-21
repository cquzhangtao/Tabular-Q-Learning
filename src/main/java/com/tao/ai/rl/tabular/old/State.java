package com.tao.ai.rl.tabular.old;

import java.util.ArrayList;
import java.util.List;

public class State {
	private List<Job> jobs=new ArrayList<Job>();
	
	public State(List<Job> jobs) {
		this.jobs.addAll(jobs);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof State) {
			State state=(State) obj;
			return jobs.size()==state.jobs.size()&&jobs.containsAll(state.jobs);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		
		int accumulator = 0;
		for (Job obj : jobs) {
			accumulator ^= obj.hashCode();
		}
		return accumulator;
	}
}
