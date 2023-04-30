package com.tao.ai.rl.tabular.utilities;

import com.tao.ai.rl.tabular.core.TabularModel2;

public class StateActionUtilities {

	public static int getStateIndex(TabularModel2 model, Object[] stateFeatures) {
		if(stateFeatures.length!=model.getStateFeatureSpace().length) {
			System.out.println("error in StateActionUtilities 2");
			return -1;
		}
		if(stateFeatures.length==0) {
			return -1;
		}
		int levelIdices[]=new int[stateFeatures.length];
		
		int idx=0;
		for(Object[] featureLevels:model.getStateFeatureSpace()) {
			levelIdices[idx]=getClosedLevelIndex(stateFeatures[idx],featureLevels);			
			idx++;
		}

		return getStateIdxFromLevels(levelIdices,model);
	}



	public static int getActionIndex(TabularModel2 model, Object[] features) {
		if(features.length==0||features.length!=model.getActionFeatureSpace().length) {
			System.out.println("error in StateActionUtilities 3");
			return -1;
		}
		int levelIdices[]=new int[features.length];
		
		int idx=0;
		for(Object[] featureLevels:model.getActionFeatureSpace()) {
			levelIdices[idx]=getClosedLevelIndex(features[idx],featureLevels);			
			idx++;
		}

		return getActionIdxFromLevels(levelIdices,model);
	}
	
	
	public static int getIdxFromLevels(int[] levelIdices, int[] featureLevelNum) {
		int cummu[]=new int[featureLevelNum.length];
		cummu[featureLevelNum.length-1]=1;
		for(int i=featureLevelNum.length-2;i>=0;i--) {
			cummu[i]=cummu[i+1]*featureLevelNum[i+1];
		}
		int idx=0;
		for(int i=0;i<levelIdices.length;i++) {
			idx+=cummu[i]*levelIdices[i];
		}
		return idx;
	}
	
	public static int getActionIdxFromLevels(int[] levelIdices, TabularModel2 model) {

		int idx=0;
		for(int i=0;i<levelIdices.length;i++) {
			idx+=model.getActionCumm()[i]*levelIdices[i];
		}
		return idx;
	}
	
	public static int getStateIdxFromLevels(int[] levelIdices, TabularModel2 model) {

		int idx=0;
		for(int i=0;i<levelIdices.length;i++) {
			idx+=model.getStateCumm()[i]*levelIdices[i];
		}
		return idx;
	}
	
	private static int getClosedLevelIndex(Object item,Object[] levelList) {
		
		if(item instanceof Number) {
			Object closestItem=null;
			double minDiffer=Double.MAX_VALUE;
			int idx=0;
			int okidx=0;
			for(Object obj:levelList) {
				double diff=0;
				if(item instanceof Double) {
					diff=Math.abs((Double)item-(Double)obj);
				} else if(item instanceof Integer) {
					diff=Math.abs((Integer)item-(Integer)obj);
				}
				 
				if(diff<minDiffer) {
					minDiffer=diff;
					closestItem= obj;
					okidx=idx;
				}
				idx++;
			}
			return okidx;
			
		}else {
			int idx=0;
			for(Object obj:levelList) {
				if(obj.equals(item)) {
					return idx;
				}
				idx++;
			}
			System.out.println("error in StateActionUtilities");
			return -1;
			
		}
		
		
	}

	

}
