package com.tao.ai.rl.tabular.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class QTable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8102479015634521056L;
	private double[][] qvalues;
	private Random rnd=new Random(0);
	private double[][] visits;
	


	public QTable(int stateNum,int actionNum) {
		qvalues=new double[stateNum][actionNum];
		visits=new double[stateNum][actionNum];
		for(int i=0;i<stateNum;i++) {
			for(int j=0;j<actionNum;j++) {
				qvalues[i][j]=-1000*rnd.nextDouble();
				visits[i][j]=0;
			}
		}
		
	}
	
	public QTable() {
		
	}
	


	public QTable(QTable qTable,int stateNum,int actionNum) {

		visits=qTable.visits;
		rnd=qTable.rnd;
		qvalues=Arrays.stream(qTable.qvalues).map(double[]::clone).toArray(double[][]::new);
		
	}
	
	public  double[][] cloneArray(double[][] src) {
	    int length = src.length;
	    double[][] target = new double[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}



	public double getQValue(int state,int action) {
		return qvalues[state][action];
	}
	
	public void setQValue(int state,int action,double value) {
		 qvalues[state][action]=value;
		 visits[state][action]++;
	}
	public double[][] getVisits() {
		return visits;
	}

}
