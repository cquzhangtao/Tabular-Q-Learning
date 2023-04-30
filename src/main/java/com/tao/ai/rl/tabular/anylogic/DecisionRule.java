package com.tao.ai.rl.tabular.anylogic;

import java.io.Serializable;

public enum DecisionRule implements Serializable {
	RL,RL_Train,FIFO,LIFO,SPT,LPT,Random,ES,LS,
	ESFIFO,ESSPT,ESRandom,
	LSFIFO,LSSPT,LSRandom, 
	ESLIFO,ESLPT,LSLIFO,LSLPT,
	Customer1, Customer2, Customer3, Customer4, Customer5, Customer6, Customer7, Customer8, Customer9, Customer10,

}
