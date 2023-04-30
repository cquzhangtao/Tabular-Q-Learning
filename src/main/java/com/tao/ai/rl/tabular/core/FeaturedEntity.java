package com.tao.ai.rl.tabular.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeaturedEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3175791624531219001L;
	private List<Object> features=new ArrayList<Object>();

	public List<Object> getFeatures() {
		return features;
	}

	public void setFeatures(List<Object> features) {
		this.features = features;
	}
	
	
	public String toString() {
		String str="";
		for(Object feature:features) {
			str+=feature.toString()+"-";
		}
		str=str.substring(0, str.length()-1);
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FeaturedEntity) {
			FeaturedEntity entity=(FeaturedEntity) obj;
			int idx=0;
			for(Object feature:features) {
				if(feature instanceof Number) {
					if(Math.abs((Double)feature-(Double)entity.getFeatures().get(idx))>0.0000001) {
						return false;
					}
				}else {
					if(feature!=entity.getFeatures().get(idx)) {
						return false;
					}
				}
				idx++;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		
		int accumulator = 0;
		for(Object feature:features) {
			accumulator ^= feature.hashCode();	
		}

		return accumulator;
	}
}
