package com.tao.ai.rl.tabular.core;

import java.io.Serializable;

public class TabularModel2 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4460622595722832138L;
	private Object[][] stateFeatureSpace;
	private Object[][] actionFeatureSpace;
	
	private int[] stateFeatureLevelNum;


	private int[] actionFeatureLevelNum;

	private QTable qTable;
	private QTable targetQTable;
	private int stateNum = 1;
	private int actionNum = 1;
	private int[] stateCumm;

	private QTable preQTable;


	private int[] actionCumm;
	
	public TabularModel2(Object[][] stateFeatureSpace, Object[][] actionFeatureSpace) {
		stateFeatureLevelNum=new int[stateFeatureSpace.length];
		actionFeatureLevelNum=new int[actionFeatureSpace.length];
		int idx=0;
		for (Object[] levels : stateFeatureSpace) {
			stateNum = stateNum * levels.length;
			stateFeatureLevelNum[idx]=levels.length;
			idx++;
		}
		idx=0;
		for (Object[] levels : actionFeatureSpace) {
			actionNum = actionNum * levels.length;
			actionFeatureLevelNum[idx]=levels.length;
			idx++;
		}
		
		actionCumm=new int[actionFeatureLevelNum.length];
		actionCumm[actionFeatureLevelNum.length-1]=1;
		for(int i=actionFeatureLevelNum.length-2;i>=0;i--) {
			actionCumm[i]=actionCumm[i+1]*actionFeatureLevelNum[i+1];
		}
		
		stateCumm=new int[stateFeatureLevelNum.length];
		stateCumm[stateFeatureLevelNum.length-1]=1;
		for(int i=stateFeatureLevelNum.length-2;i>=0;i--) {
			stateCumm[i]=stateCumm[i+1]*stateFeatureLevelNum[i+1];
		}
		
		qTable = new QTable(stateNum, actionNum);
		targetQTable= new QTable(qTable,stateNum, actionNum);
		this.stateFeatureSpace = stateFeatureSpace;
		this.actionFeatureSpace = actionFeatureSpace;
		System.out.println(
				"State space:" + stateNum + ",action set:" + actionNum + ",Q table len:" + actionNum * stateNum);

	}



	public QTable getqTable() {
		return qTable;
	}

	public double getQValue(int state, int action) {
		return qTable.getQValue(state, action);
	}

	public void updateQValue(int state, int action, double newValue) {
		qTable.setQValue(state, action, newValue);
	}

	public Object[][] getStateFeatureSpace() {
		return stateFeatureSpace;
	}

	public Object[][] getActionFeatureSpace() {
		return actionFeatureSpace;
	}
	public int[] getStateFeatureLevelNum() {
		return stateFeatureLevelNum;
	}



	public int[] getActionFeatureLevelNum() {
		return actionFeatureLevelNum;
	}
	
	public int[] getStateCumm() {
		return stateCumm;
	}



	public int[] getActionCumm() {
		return actionCumm;
	}



	public void rollBack() {
		//targetQTable=new QTable(qTable,stateNum,actionNum);
		qTable=new QTable(preQTable,stateNum,actionNum);
	}



	public void moveon() {

		preQTable=new QTable(qTable,stateNum,actionNum);
		qTable=new QTable(targetQTable,stateNum,actionNum);
	
		
	}
}
