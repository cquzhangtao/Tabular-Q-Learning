# Introduction
Neural networks are often used to approximate policies and value functions, but they do not guarantee convergence. Tabular-based algorithms can guarantee convergence and can be also used to solve problems having a relatively big state space as long as enough computation power supports them. If it is impossible to set up corresponding hardware, anyway, the state space can be reduced by some dimension reduction algorithms or domain knowledge first. For this reason, an agent with a tabular Q-learning algorithm is developed in this project and will be embedded into the simulation model to learn scheduling knowledge. The continuous features in state space are split into several levels by predefined min, max, and step size. <br /><br />
# How to use?
Before the simulation starts, define state space and action set, and create an agent initialized with the state space, action set, and parameters.<br />
``` java
////////////////////////
//State definition
////////////////////////
// queue length
Double[] stateFeature1=new Double[]{2.0,4.0,6.0,8.0,10.0};
// percentage of A in queue
Double[] stateFeature2=new Double[]{25.0,75.0};
// percentage of B in queue
Double[] stateFeature3=new Double[]{25.0,75.0};
// percentage of lots in queue 1<=revist counter<5
Double[] stateFeature4=new Double[]{25.0,75.0};
// percentage of lots in queue 6<=revist counter<10
Double[] stateFeature5=new Double[]{25.0,75.0};
// percentage of lots in queue 11<=revist counter<15
Double[] stateFeature6=new Double[]{25.0,75.0};
// percentage of lots in queue 16<=revist counter<20
Double[] stateFeature7=new Double[]{25.0,75.0};

List<Double[]> stateFeatureSpace=new ArrayList<>();
stateFeatureSpace.add(stateFeature1);
stateFeatureSpace.add(stateFeature2);
stateFeatureSpace.add(stateFeature3);
stateFeatureSpace.add(stateFeature4);
stateFeatureSpace.add(stateFeature5);
stateFeatureSpace.add(stateFeature6);
stateFeatureSpace.add(stateFeature7);

////////////////////////
//Action Set definition
////////////////////////
List<DecisionRule> actionSet=new ArrayList<>();
actionSet.add(DecisionRule.SPT);
actionSet.add(DecisionRule.FIFO);
actionSet.add(DecisionRule.LIFO);
actionSet.add(DecisionRule.LPT);
actionSet.add(DecisionRule.Random);

///////////////////////////
// RL model and agent
///////////////////////////
TabularModelForAnylogic2 rlModel=new TabularModelForAnylogic2(stateFeatureSpace,actionSet);
rlAgent=new AgentForAnylogic2(rlModel);
rlAgent.setLearningRate(0.2);
rlAgent.setLearningRateDecay(0.008);
rlAgent.setMaxEpochLearningRate(100000);
rlAgent.setEpsilon(0.1);
rlAgent.setEpsilonDecay(1);
rlAgent.setMaxEpochEpsilon(1000);
rlAgent.setDiscountFactor(0.9);
rlAgent.setMinLearningRate(0.001);
```
Whenever a decision needs to be made in the simulation, ask the agent to do it.
``` java
Double[] curStateFeatures=getStateFeatures();	
//double reward=-1*timeWeightedWIP;
double reward=-1*avgCT;
rule=rlLotSelectAgent.selectAction(curStateFeatures,reward);
```
# Publication
For more information, please refer to our paper. <br/>
[A REINFORCEMENT LEARNING APPROACH FOR IMPROVED PHOTOLITHOGRAPHY SCHEDULES](https://dl.acm.org/doi/pdf/10.5555/3643142.3643319)
