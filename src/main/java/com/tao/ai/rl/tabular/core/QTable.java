package com.tao.ai.rl.tabular.core;

public class QTable {
	
	private double[][] qvalues;
	
	public QTable(int stateNum,int actionNum) {
		qvalues=new double[stateNum][actionNum];
	}
	
	public double getQValue(int state,int action) {
		return qvalues[state][action];
	}
	
	public void setQValue(int state,int action,double value) {
		 qvalues[state][action]=value;
	}

}
