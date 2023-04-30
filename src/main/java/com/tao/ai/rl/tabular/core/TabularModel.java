package com.tao.ai.rl.tabular.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tao.ai.rl.tabular.utilities.TabularModelUtilities;

public class TabularModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1424116140124100391L;
	private List<State> stateSpace=new ArrayList<State>();
	private List<Action> actionSet=new ArrayList<Action>();
	
	private Object[][]stateFeatureSpace;
	private Object[][]actionFeatureSpace;
	
	private QValueMap qTable=new QValueMap();
	
	public TabularModel(List<State> stateSpace,List<Action> actionSet) {
		this.stateSpace=stateSpace;
		this.actionSet=actionSet;
		TabularModelUtilities.generateQTable(this);
		System.out.println("State space:"+stateSpace.size()+",action set:"+actionSet.size()+",Q table len:"+qTable.size());
		
	}
	
	public TabularModel(Object[][]stateFeatureSpace,Object[][] actionFeatureSpace) {		
		  this(TabularModelUtilities.fullCombination(stateFeatureSpace,State::new),TabularModelUtilities. fullCombination(actionFeatureSpace,Action::new));
		  this.stateFeatureSpace=stateFeatureSpace;
		  this.actionFeatureSpace=actionFeatureSpace;

	}
	

	public List<State> getStateSpace() {
		return stateSpace;
	}

	public List<Action> getActionSet() {
		return actionSet;
	}

	public QValueMap getqTable() {
		return qTable;
	}
	
	public double getQValue(State state,Action action) {
		return qTable.get(state, action);
	}
	
	public void updateQValue(State state,Action action,double newValue) {
		qTable.put(state, action,newValue);
	}

	public Object[][] getStateFeatureSpace() {
		return stateFeatureSpace;
	}

	public Object[][] getActionFeatureSpace() {
		return actionFeatureSpace;
	}

}
