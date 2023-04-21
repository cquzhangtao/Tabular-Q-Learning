package com.tao.ai.rl.tabular.utilities;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.tao.ai.rl.tabular.core.Action;
import com.tao.ai.rl.tabular.core.FeaturedEntity;
import com.tao.ai.rl.tabular.core.State;
import com.tao.ai.rl.tabular.core.StateActionPair;
import com.tao.ai.rl.tabular.core.TabularModel;

public class TabularModelUtilities {
	
	public static void generateQTable(TabularModel model) {
		
		for(Action action:model.getActionSet()) {
			if(model.getStateSpace().isEmpty()) {
				StateActionPair pair=new StateActionPair(null,action);
				model.getqTable().put(pair, 0.0);				
			}else {
				for(State state:model.getStateSpace()) {
	
					StateActionPair pair=new StateActionPair(state,action);
					model.getqTable().put(pair, 0.0);
				}
			}
		}
		
	}
	
	public static <T extends FeaturedEntity> List<T> fullCombination(Object[][]featureSpace,Supplier<T> supplier) {
		List<T> entityList=new ArrayList<T>();	
		if(featureSpace.length==0) {
			return entityList;
		}

		entityList.add(supplier.get());
		List<T> newEntityList=new ArrayList<T>();
		
		for(Object[] feature:featureSpace) {
			for(Object featureV:feature) {
				for(T entity: entityList) {
				
					newEntityList.add(combineWithOtherFeatures(entity,featureV,supplier));
				}
			}
			entityList=newEntityList;
			newEntityList=new ArrayList<T>();
		}
		return entityList;
	}
	
	
	private static <T extends FeaturedEntity>  T combineWithOtherFeatures (T entity, Object feature,Supplier<T> supplier) {
		T newEntity=supplier.get();
		newEntity.getFeatures().addAll(entity.getFeatures());
		newEntity.getFeatures().add(feature);
		return newEntity;
		
	}

	public static State convertToState(TabularModel model, Object[] stateFeatures) {
		
		if(stateFeatures.length!=model.getStateFeatureSpace().length) {
			System.out.println("error in TabularUtilities 2");
			return null;
		}
		if(stateFeatures.length==0) {
			return null;
		}
		State state=new State();
		
		int idx=0;
		for(Object[] featureLevels:model.getStateFeatureSpace()) {
			state.getFeatures().add(getClosedLevel(stateFeatures[idx],featureLevels));			
			idx++;
		}

		return state;
	}

	public static Action convertToAction(TabularModel model, Object[] features) {
		if(features.length==0||features.length!=model.getActionFeatureSpace().length) {
			System.out.println("error in TabularUtilities 3");
			return null;
		}
		Action action=new Action();
		
		int idx=0;
		for(Object[] featureLevels:model.getActionFeatureSpace()) {
			action.getFeatures().add(getClosedLevel(features[idx],featureLevels));			
			idx++;
		}

		return action;
	}
	
	public static Object getClosedLevel(Object item,Object[] levelList) {
		
		if(item instanceof Number) {
			Object closestItem=null;
			double minDiffer=Double.MAX_VALUE;
			for(Object obj:levelList) {
				double diff=Math.abs((Double)item-(Double)obj);
				if(diff<minDiffer) {
					minDiffer=diff;
					closestItem= obj;
				}
			}
			return closestItem;
			
		}else {
			for(Object obj:levelList) {
				if(obj.equals(item)) {
					return item;
				}
			}
			System.out.println("error in TabularModelUtilities");
			return null;
			
		}
		
		
	}


}
