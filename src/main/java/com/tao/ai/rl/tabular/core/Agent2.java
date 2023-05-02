package com.tao.ai.rl.tabular.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tao.ai.rl.tabular.utilities.StateActionUtilities;
import com.tao.ai.rl.tabular.utilities.TabularModelUtilities;

import ch.qos.logback.classic.Logger;

public class Agent2 {
	
	private TabularModel2 model;
	private double learningRate = 0.1;
	private double learningRateDecay = 0.1;
	private double discountFactor = 0.9;
	private  double epsilon = 0.2;
	private  double epsilonDecay = 0.2;
	
	
	private int preState=-1;
	private int preAction=-1;
	private double preEpisodReward=Double.NEGATIVE_INFINITY;
	private double totalReward=0;
	private List<Double>totalRewardPerEpisod=new ArrayList<>();
	
	private int epoch=0;
	private int maxEpoch=100;

	


	private Random rnd=new Random(0);
	
	private boolean training=true;
	
	public Agent2(TabularModel2 model) {
		this.model=model;
	}
	
	public Agent2(String model) {
		load(model);
		training=false;
	}
	
	public void save(String path) {
		try {
			FileOutputStream f = new FileOutputStream(new File(path));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(model);

			o.close();
			f.close();
			System.out.println("Q table saved in "+path);

			

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}

	}
	
	public void load(String path) {
		
		try {
			FileInputStream fi = new FileInputStream(new File(path));
			ObjectInputStream oi = new ObjectInputStream(fi);
	
			// Read objects
			model = (TabularModel2) oi.readObject();
	
			oi.close();
			fi.close();
			System.out.println("Q table loaded from "+path);
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public int selectAction(Object[] stateFeatures, List<Object[]> actionFeaturess, double reward) {
		int state=StateActionUtilities.getStateIndex(model,stateFeatures);
		List<Integer> actions=new ArrayList<Integer>();
		Map<Integer,Integer> action2IdxMap=new HashMap<>();
		int idx=0;
		for(Object[] features:actionFeaturess) {
			int action=StateActionUtilities.getActionIndex(model,features);
			actions.add(action);
			action2IdxMap.put(action, idx);
			idx++;
		}
		int selAction= selectAction(state,actions,reward);
		if(!action2IdxMap.containsKey(selAction)) {
			System.out.println("error in Agent,"+selAction +","+action2IdxMap) ;
		}
		return action2IdxMap.get(selAction);
		
		
		
		
	}
	
	public int selectAction(int state, List<Integer> actions, double reward) {
		int bestAction=-1;
		if(rnd.nextDouble()<getDecaiedEpsilon()&&training) {
			int size = actions.size();
			int item = rnd.nextInt(size); 			
			bestAction= actions.get(item);
			//System.out.println("bestAction "+bestAction);

		}else {
		
			double maxQValue=Double.NEGATIVE_INFINITY;
			
			for(int action : actions) {
				double qvalue=model.getQValue(state, action);
				//System.out.println("Q value "+qvalue);
				if(qvalue>maxQValue) {
					maxQValue=qvalue;
					bestAction=action;
				}
				
			}
		}
		if(bestAction==-1) {
			System.out.println("error in Agent 1,");
		}
		
		if(training) {
			updateQValue(state, bestAction,actions, reward);
		}
		totalReward+=reward;
		
		return bestAction;
		
	}

	private void updateQValue(int state, int action, List<Integer> actions,double reward) {
		if(preState==-1) {
			preState=state;
			preAction=action;
			return;
		}
		updateQValue(preState,preAction,state,action,actions,reward);
		
		preState=state;
		preAction=action;
		
		
		
	}
	
	private void updateQValue(int state, int action, int nextState,  int nextAction,List<Integer> nextActions,double reward) {
		double oldValue=model.getQValue(state, action);

		
		double maxValue=Double.NEGATIVE_INFINITY;
		for(int naction:nextActions) {
			double nextValue=model.getQValue(nextState, naction);
			if(nextValue>maxValue) {
				maxValue=nextValue;
			}
		}
		
		double futureEstimate=reward+discountFactor*maxValue;
		double newValue=oldValue+getDecaiedLearningRate()*(futureEstimate-oldValue);
		
		//System.out.println("update q :"+oldValue+","+newValue);
		
		
		model.updateQValue(state, action, newValue);
		
	}
	
	public void onSimEnd() {
		totalRewardPerEpisod.add(totalReward);
		/*if(preEpisodReward>totalReward) {
			model.rollBack();
		}else {
			preEpisodReward=totalReward;
			model.moveon();
		}*/
		//model.moveon();
		totalReward=0;
		epoch++;
	}



	public TabularModel2 getModel() {
		return model;
	}



	public void setModel(TabularModel2 model) {
		this.model = model;
	}



	public double getLearningRate() {
		return learningRate;
	}



	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}



	public double getDiscountFactor() {
		return discountFactor;
	}



	public void setDiscountFactor(double discountFactor) {
		this.discountFactor = discountFactor;
	}



	public  double getGreedy() {
		return epsilon;
	}



	public  void setGreedy(double greedy) {
		this.epsilon = greedy;
	}



	public double getTotalReward() {
		return totalReward;
	}
	
	private double getDecaiedValue8(int step2zero,double initial) {
		return Math.max(0.001, 1.0*(step2zero-epoch)/step2zero*initial);
	}
	
	private double getDecaiedValueGreed8(int step2zero,double initial) {
		return Math.max(0.00001, 1.0*(step2zero-epoch)/step2zero*initial);
	}
	
	private int epoch0d01=-1;
	
	private double getDecaiedLearningRate() {
		//double value=  learningRate/(1+0.025*epoch);
		
		double value=learningRate*Math.pow(learningRateDecay, epoch/maxEpoch);
		//if(value>0.0005) {
//			if(epoch0d01>-1) {
//				epoch0d01=epoch;
//			}
			return value;
//		
		//}
		//return Math.max(0, 1.0*(500-epoch+epoch0d01)/500*0.01);
	}
	private double getDecaiedEpsilon() {
		double value=epsilon*Math.pow(epsilonDecay, epoch/maxEpoch);
//		
//		//if(value>0.0005) {
//			if(epoch0d01>-1) {
//				epoch0d01=epoch;
//			}
			return value;
//		
//		//}
		//return Math.max(0, 1.0*(500-epoch+epoch0d01)/500*0.01);
	}
	
	public void setMaxEpoch(int maxEpoch) {
		this.maxEpoch = maxEpoch;
	}

	public void setLearningRateDecay(double learningRateDecay) {
		this.learningRateDecay = learningRateDecay;
	}

	public double getEpsilonDecay() {
		return epsilonDecay;
	}

	public void setEpsilonDecay(double epsilonDecay) {
		this.epsilonDecay = epsilonDecay;
	}

}
