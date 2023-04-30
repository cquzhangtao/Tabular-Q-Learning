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

import com.tao.ai.rl.tabular.utilities.TabularModelUtilities;

public class Agent {
	
	private TabularModel model;
	private double learningRate = 0.1;
	private double discountFactor = 0.5;
	private static double greedy = 0.2;
	
	
	private State preState;
	private Action preAction;
	
	private double totalReward=0;
	private List<Double>totalRewardPerEpisod=new ArrayList<>();

	
	private Random rnd=new Random(0);
	
	private boolean training=true;
	
	public Agent(TabularModel model) {
		this.model=model;
	}
	
	public Agent(String model) {
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
			model = (TabularModel) oi.readObject();
	
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
		State state=TabularModelUtilities.convertToState(model,stateFeatures);
		List<Action> actions=new ArrayList<Action>();
		Map<Action,Integer> action2IdxMap=new HashMap<>();
		int idx=0;
		for(Object[] features:actionFeaturess) {
			Action action=TabularModelUtilities.convertToAction(model,features);
			actions.add(action);
			action2IdxMap.put(action, idx);
			idx++;
		}
		Action selAction= selectAction(state,actions,reward);
		if(!action2IdxMap.containsKey(selAction)) {
			System.out.println("error in Agent,"+selAction +","+action2IdxMap) ;
		}
		return action2IdxMap.get(selAction);
		
		
		
		
	}
	
	public Action selectAction(State state, List<Action> actions, double reward) {
		Action bestAction=null;
		if(rnd.nextDouble()<greedy&&training) {
			int size = actions.size();
			int item = rnd.nextInt(size); 			
			bestAction= actions.get(item);
			//System.out.println("bestAction "+bestAction);

		}else {
		
			double maxQValue=Double.NEGATIVE_INFINITY;
			
			for(Action action : actions) {
				double qvalue=model.getQValue(state, action);
				//System.out.println("Q value "+qvalue);
				if(qvalue>maxQValue) {
					maxQValue=qvalue;
					bestAction=action;
				}
				
			}
		}
		if(bestAction==null) {
			System.out.println("error in Agent 1,");
		}
		
		if(training) {
			updateQValue(state, bestAction,actions, reward);
		}
		totalReward+=reward;
		
		return bestAction;
		
	}

	private void updateQValue(State state, Action action, List<Action> actions,double reward) {
		if(preState==null) {
			preState=state;
			preAction=action;
			return;
		}
		updateQValue(preState,preAction,state,action,actions,reward);
		
		preState=state;
		preAction=action;
		
		
		
	}
	
	private void updateQValue(State state, Action action, State nextState,  Action nextAction,List<Action> nextActions,double reward) {
		double oldValue=model.getQValue(state, action);

		
		double maxValue=Double.NEGATIVE_INFINITY;
		for(Action naction:nextActions) {
			double nextValue=model.getQValue(nextState, naction);
			if(nextValue>maxValue) {
				maxValue=nextValue;
			}
		}
		
		double futureEstimate=reward+discountFactor*maxValue;
		double newValue=oldValue+learningRate*(futureEstimate-oldValue);
		
		model.updateQValue(state, action, newValue);
		
	}
	
	public void onSimEnd() {
		totalRewardPerEpisod.add(totalReward);
		totalReward=0;
	}



	public TabularModel getModel() {
		return model;
	}



	public void setModel(TabularModel model) {
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



	public static double getGreedy() {
		return greedy;
	}



	public static void setGreedy(double greedy) {
		Agent.greedy = greedy;
	}



	public double getTotalReward() {
		return totalReward;
	}

}
