package com.tao.ai.rl.tabular.anylogic;

import java.io.Serializable;

public enum DecisionRule implements Serializable {
	RL,RL_Train,FIFO,SPT,Random,LeastEntrantTimes,MostEntrantTimes,
	LeastEntrantTimes_FIFO,LeastEntrantTimes_SPT,LeastEntrantTimes_Random,
	MostEntrantTimes_FIFO,MostEntrantTimes_SPT,MostEntrantTimes_Random, 
	Customer1, Customer2, Customer3, Customer4, Customer5, Customer6, Customer7, Customer8, Customer9, Customer10,

}
