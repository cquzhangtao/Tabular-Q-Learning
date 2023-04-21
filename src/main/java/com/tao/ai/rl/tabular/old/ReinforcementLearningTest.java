package com.tao.ai.rl.tabular.old;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.data.norm.MaxMinNormalizer;




public class ReinforcementLearningTest {

	private static boolean neuralFittedQLearning = false;
	private static int objective = 2;
	private static int maxIteration = 20000;
	private static double greedy = 0.2;
	private static boolean showChart = true;
	// private static int maxIteration=10;

	static boolean terminate = false;

	enum Algorithm {
		Q_learning, SARSA
	}

	public static void main(String[] args) {

		ConvergenceChart chart = null;
		if (showChart) {
			chart = new ConvergenceChart(Algorithm.values());
			chart.setVisible(true);
			chart.pack();
		}
		// nerualNetwork();
		DataSet dummySet = prepare();
		MaxMinNormalizer maxMinNormalizer = new MaxMinNormalizer(dummySet);

		List<Job> jobs = generateJobs();
		Object qValues;
		qValues = valueIteration(jobs);
		evaluate(jobs, qValues,maxMinNormalizer);
		//qValues = sarsa(jobs, maxMinNormalizer, chart);
		//evaluate(jobs, qValues, maxMinNormalizer);
		//qValues = qLearning(jobs,maxMinNormalizer,chart);
		//evaluate(jobs, qValues, maxMinNormalizer);


	}

	public static Object sarsa(List<Job> ojobs, MaxMinNormalizer maxMinNormalizer, ConvergenceChart chart) {
		return learning(ojobs, maxMinNormalizer, chart, Algorithm.SARSA);

	}

	public static Object qLearning(List<Job> ojobs, MaxMinNormalizer maxMinNormalizer, ConvergenceChart chart) {
		return learning(ojobs, maxMinNormalizer, chart, Algorithm.Q_learning);

	}

	private static double[] getStateContent(List<Job> state, double time) {
		boolean noStateInput = true;
		if (noStateInput) {
			double[] stateContent = new double[0];
			return stateContent;
		} else {
			// if(objective==1) {
			double[] stateContent = new double[1];
			stateContent[0] = state.size();
			return stateContent;
			// }else {
			/*
			 * double[] stateContent=new double[1]; double sum=0; for(Job job:state) {
			 * sum+=job.getDueDate()-time; } stateContent[0]=sum; return stateContent;
			 */
			// }
		}
	}

	private static double[] getActionContent(Job action) {
		double[] actionContent = null;
		if (objective == 1) {
			actionContent = new double[1];
			actionContent[0] = action.getTimeOnStep();

		} else if (objective == 2) {
			actionContent = new double[1];
			actionContent[0] = action.getDueDate();
		} else if (objective == 3) {
			actionContent = new double[2];
			actionContent[0] = action.getTimeOnStep();
			actionContent[1] = action.getDueDate();
		}
		return actionContent;
	}

	private static double getReward(List<Job> jobs, Job job, double time, double totalCompletionTime,
			int finishedNumber, int tardyNumber, double sumTardiness, double sumLateness, double maxLateness) {
		double reward = 0;

		// **********************************************************
		// **********************************************************

		double sum = 0;
		for (Job ijob : jobs) {
			if (objective == 2) {
				sum += time - ijob.getDueDate();// OK
			} else if (objective == 1) {
				sum += 999999 - ijob.getTimeOnStep();// OK
			} else {
				if (ijob.getDueDate() < time) {// OK
					sum += 1;
				}
			}
		}
		reward = -sum;
		// **********************************************************
		// **********************************************************
		if (objective == 1) {
			reward = -job.getTimeOnStep(); // OK
		} else if (objective == 2) {
			reward = -job.getDueDate(); // OK
		} else {
			reward = -(job.getDueDate() - time - job.getTimeOnStep());
		}

		// **********************************************************
		// **********************************************************
//		
////		reward = -sumLateness;//No
//		if (objective == 1) {
//			reward = -totalCompletionTime;// OK
//		} else if (objective == 2) {
//			reward = -maxLateness;// No
//		} else {
//			reward = -tardyNumber;// OK
//		}
////		
//		// **********************************************************
//		// **********************************************************
//		if (objective == 1) {
//			reward = -totalCompletionTime / finishedNumber;// OK
//		} else if (objective == 2) {
//			reward = -sumLateness / finishedNumber;// No
//		} else {
//			reward = -tardyNumber / finishedNumber;// No
//		}
//		if (objective == 2 || objective == 3) {
//			reward = -sumTardiness / finishedNumber;
//			;// OK
//		}
//
//		// **********************************************************
//		// **********************************************************

//		reward = -sumLateness/finishedNumber*job.getTimeOnStep();//No
//		reward = -tct/finishedNumber*job.getTimeOnStep();//OK
//		reward = -tardyNumber/finishedNumber*job.getTimeOnStep();//No

		// **********************************************************
		// *******************OKOKOK*********************************

//		if (jobs.size() == 1) {
//			if (objective == 2) {
//				reward = getMaxValue() - maxLateness;// OK
//			} else if (objective == 1) {
//				reward = getMaxValue() - totalCompletionTime;// OK
//			} else {
//				reward = getMaxValue() - tardyNumber;// OK
//			}
//		} else {
//			reward = 0;
//		}

		// **********************OKOKOK******************************
		// **********************************************************

		return reward;
	}

	public static Object learning(List<Job> ojobs, MaxMinNormalizer maxMinNormalizer, ConvergenceChart chart,
			Algorithm algorithm) {
		Random rnd = new Random(0);

		QValueMap oldValues = new QValueMap();
		QValueMap newValues = new QValueMap();

		List<Job> state;
		double learningRate = 0.1;
		double discountFactor = 0.5;
		int iterate = 0;

		int inputSize = getStateContentSize() + getActionContentSize();
		MultiLayerPerceptron neuralNetwork = createNeuralNetwork();
		DataSet trainingSet = new DataSet(inputSize, 1);

		while (!terminate) {
			state = new ArrayList<Job>(ojobs);
			double time = 0;
			double tct = 0;
			int tardyNumber = 0;
			int finishedNumber = 0;
			double sumLateness = 0;
			double sumTardiness = 0;
			double maxLateness = Double.NEGATIVE_INFINITY;
			double maxError = Double.NEGATIVE_INFINITY;

			trainingSet.clear();

			Job action = state.get(0);

			double maxValueInNextState = Double.NEGATIVE_INFINITY;
			for (int j = 0; j < state.size(); j++) {
				Job njob = state.get(j);
				double qvalue = getQValue(oldValues, neuralNetwork, maxMinNormalizer, state, njob, time);
				if (qvalue > maxValueInNextState) {
					action = njob;
					maxValueInNextState = qvalue;
				}
			}

			double r = rnd.nextDouble();
			if (r < greedy) {
				action = state.get(rnd.nextInt(state.size()));
			}

			newValues.clear();
			while (state.size() > 0) {

				time += action.getTimeOnStep();
				tct += time;
				finishedNumber++;
				double lateness = time - action.getDueDate();
				sumLateness += lateness;
				if (maxLateness < lateness) {
					maxLateness = lateness;
				}
				if (lateness > 0) {
					tardyNumber++;
					sumTardiness += lateness;
				}

				double oldValue = getQValue(oldValues, neuralNetwork, maxMinNormalizer, state, action, time);

				double reward = getReward(state, action, time, tct, finishedNumber, tardyNumber, sumTardiness,
						sumLateness, maxLateness);

				List<Job> newState = new ArrayList<Job>(state);
				newState.remove(action);

				Job bestJob = null;
				// double maxValueInNextState;
				if (newState.size() > 0) {
					maxValueInNextState = Double.NEGATIVE_INFINITY;

					for (Job njob : newState) {
						double nValue = getQValue(oldValues, neuralNetwork, maxMinNormalizer, newState, njob, time);

						if (nValue > maxValueInNextState) {
							maxValueInNextState = nValue;
							bestJob = njob;
						}
					}
					r = rnd.nextDouble();
					if (r < greedy) {
						bestJob = newState.get(rnd.nextInt(newState.size()));
					}
					if (algorithm == Algorithm.SARSA) {
						maxValueInNextState = getQValue(oldValues, neuralNetwork, maxMinNormalizer, newState, bestJob,
								time);
					}
				} else {
					maxValueInNextState = 0;
				}

				double newValue = (1 - learningRate) * oldValue
						+ learningRate * (reward + discountFactor * maxValueInNextState);

				newValues.put(state, action, newValue);

				if (neuralFittedQLearning) {
					DataSetRow row = new DataSetRow(getInput(state, action, time));
					double value;
					value = reward + discountFactor * maxValueInNextState;
					// value=newValue;
					row.setDesiredOutput(new double[] { value });
					trainingSet.add(row);
				}

				// jobs.remove(job);

				state = newState;

				action = bestJob;

				double error = Math.abs(newValue - oldValue);
				if (error > maxError) {
					maxError = error;
				}

			}
			oldValues.putAll(newValues);
			iterate++;
			if (neuralFittedQLearning) {
				// neuralNetwork.reset();
				maxMinNormalizer.normalize(trainingSet);
				neuralNetwork.learn(trainingSet);

				for (int i = 0; i < trainingSet.size(); i++) {
					neuralNetwork.setInput(trainingSet.get(i).getInput());
					neuralNetwork.calculate();
					// System.out.println(Arrays.toString(trainingSet.get(i).getInput())+","+neuralNetwork.getOutput()[0]+","+trainingSet.get(i).getDesiredOutput()[0]);
				}
			}
			if (iterate % 1 == 0) {
				double obj = getObjective(ojobs, oldValues, maxMinNormalizer);

				System.out.println(iterate + "\t" + maxError + "\t" + obj);
				if (showChart) {

					chart.addData(algorithm, iterate, maxError, obj);
				}
			}

			/*
			 * if(tct<1900) { System.out.println(iterate + "\t" + maxError); break; }
			 */

			if (maxError <= getMaxError() || iterate > maxIteration) {
				System.out.println(iterate + "\t" + maxError);
				break;
			}

		}

		if (neuralFittedQLearning) {
			return neuralNetwork;
		}
		return oldValues;

	}

	private static MultiLayerPerceptron createNeuralNetwork() {
		int inputSize = getStateContentSize() + getActionContentSize();
		MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(inputSize, 3, 1);
		// neuralNetwork.setLearningRule(new BackPropagation());
		// neuralNetwork.randomizeWeights(rnd);

		neuralNetwork.getLearningRule().setMaxIterations(500);
		neuralNetwork.getLearningRule().setMaxError(0.001);
		LearningEventListener listener = new LearningEventListener() {

			@Override
			public void handleLearningEvent(LearningEvent event) {
				SupervisedLearning rule = (SupervisedLearning) event.getSource();
				// System.out.println(Arrays.toString(neuralNetwork.getWeights()));
				// System.out.println(rule.getCurrentIteration()+","+rule.getTotalNetworkError());

			}
		};
		neuralNetwork.getLearningRule().addListener(listener);
		return neuralNetwork;
	}

	private static double getMaxValue() {

		if (objective == 2) {
			return 800;
		} else if (objective == 1) {
			return 10000;
		} else if (objective == 3) {
			return 10;
		} else {
			System.out.println("werid");
			return -1;
		}
	}

	private static double getMaxError() {
		if (neuralFittedQLearning) {
			if (objective == 2) {
				return 2000;
			} else if (objective == 1) {
				return 800;
			} else if (objective == 3) {
				return 0.4;
			} else {
				System.out.println("werid");
				return -1;
			}
		} else {
			if (objective == 2) {
				return 0.000;
			} else if (objective == 1) {
				return 0.01;
			} else if (objective == 3) {
				return 0.000;
			} else {
				System.out.println("werid");
				return -1;
			}
		}
	}

	private static double antiNormalize(MaxMinNormalizer maxMinNormalizer, double data, boolean input) {
		return antiNormalize(maxMinNormalizer, new double[] { data }, input)[0];
	}

	private static double[] antiNormalize(MaxMinNormalizer maxMinNormalizer, double[] data, boolean input) {
		double[] results = new double[data.length];
		double[] min;
		double[] max;
		if (input) {
			min = maxMinNormalizer.getMinIn();
			max = maxMinNormalizer.getMaxIn();
		} else {
			min = maxMinNormalizer.getMinOut();
			max = maxMinNormalizer.getMaxOut();
		}
		for (int i = 0; i < data.length; i++) {
			if (Double.isNaN(data[i])) {
				data[i] = 0;
			}
			results[i] = data[i] * (max[i] - min[i]) + min[i];
		}

		return results;
	}

	private static double[] normalize(MaxMinNormalizer maxMinNormalizer, double[] data, boolean input) {
		double[] results = new double[data.length];
		double[] min;
		double[] max;
		if (input) {
			min = maxMinNormalizer.getMinIn();
			max = maxMinNormalizer.getMaxIn();
		} else {
			min = maxMinNormalizer.getMinOut();
			max = maxMinNormalizer.getMaxOut();
		}
		for (int i = 0; i < data.length; i++) {
			results[i] = (data[i] - min[i]) / (max[i] - min[i]);
		}

		return results;
	}

	private static int getStateContentSize() {
		List<Job> state = new ArrayList<Job>();
		return getStateContent(state, 0).length;
	}

	private static int getActionContentSize() {
		Job job = new Job();
		return getActionContent(job).length;
	}

	private static double[] getInput(List<Job> state, Job action, double time) {
		double[] sc = getStateContent(state, time);
		double[] ac = getActionContent(action);

		if (sc.length == 0) {
			return ac;
		}

		if (ac.length == 0) {
			return sc;
		}
		return concatenate(sc, ac);
	}

	private static QValueMap valueIteration(List<Job> jobs) {

		List<List<Job>> states = new ArrayList<List<Job>>();
		for (int n = 1; n <= jobs.size(); n++) {
			combinations2(jobs.toArray(new Job[0]), n, 0, new Job[n], states);
		}

		QValueMap tempValues = new QValueMap();
		QValueMap values = new QValueMap();

		for (List<Job> state : states) {
			for (Job action : state) {
				values.put(state, action, 0.0);
			}
		}

		double discountFactor = 0.6;
		int iterate = 0;

		while (true) {
			double maxError = Double.NEGATIVE_INFINITY;
			tempValues.clear();
			for (List<Job> state : states) {
				for (Job action : state) {
					double nextBestActionValue;
					List<Job> newState = new ArrayList<Job>(state);
					newState.remove(action);
					if (newState.size() > 0) {
						// newState = stateMap.get(Key(newState));

						nextBestActionValue = Double.NEGATIVE_INFINITY;
						for (Job nextAction : newState) {
							double nextActionValue = values.get(newState, nextAction);
							if (nextBestActionValue < nextActionValue) {
								nextBestActionValue = nextActionValue;
							}
						}
					} else {
						nextBestActionValue = 1;
					}
					double oldValue = values.get(state, action);

					double duration = action.getTimeOnStep();

					// double reward = -state.size()*duration;
					double reward = -duration;
					// double reward = duration;

					// double reward = -action.getDueDate();

					double newValue = reward + discountFactor * nextBestActionValue;

					tempValues.put(state, action, newValue);

					double error = Math.abs(oldValue - newValue);
					if (error > maxError) {
						maxError = error;
					}
				}
			}
			values.clear();
			values.putAll(tempValues);

			iterate++;
			System.out.println(iterate + "," + maxError);
			if (maxError < 0.00001 || iterate > 20) {
				// System.out.println(values.size());
				break;
			}
		}

		return values;

	}

	private static List<Job> generateJobs() {
		List<Job> jobs = new ArrayList<Job>();

		Random rnd = new Random(0);
		for (int i = 0; i < 10; i++) {
			Job job = new Job();
			job.setTimeOnStep(1 + rnd.nextInt(99));
			job.setDueDate(rnd.nextInt(500));
			jobs.add(job);
		}
		return jobs;
	}

	private static void evaluate(List<Job> jobsCopy, Object values, MaxMinNormalizer maxMinNormalizer) {
		List<Job> jobs = new ArrayList<Job>(jobsCopy);
		List<Job> sortedJobs = new ArrayList<Job>(jobs);
		Collections.sort(sortedJobs, new Comparator<Job>() {

			@Override
			public int compare(Job arg0, Job arg1) {
				return (int) (arg0.getTimeOnStep() - arg1.getTimeOnStep());
			}
		});

		double thereticalTotalCompletionTime = 0;
		double curTime = 0;
		for (Job job : sortedJobs) {
			curTime += job.getTimeOnStep();
			thereticalTotalCompletionTime += curTime;
		}

		Collections.sort(sortedJobs, new Comparator<Job>() {

			@Override
			public int compare(Job arg0, Job arg1) {
				return (int) (arg0.getDueDate() - arg1.getDueDate());
			}
		});
		curTime = 0;
		double thereticalMaxLateness = Double.NEGATIVE_INFINITY;
		for (Job job : sortedJobs) {
			curTime += job.getTimeOnStep();
			double lateness = curTime - job.getDueDate();
			if (thereticalMaxLateness < lateness) {
				thereticalMaxLateness = lateness;
			}
		}

		minimaizeNumberOfTardyJobs(sortedJobs);
		Collections.sort(sortedJobs, new Comparator<Job>() {

			@Override
			public int compare(Job arg0, Job arg1) {
				return (int) (arg0.getPriorityByRule() - arg1.getPriorityByRule());
			}
		});
		curTime = 0;
		int thereticalMinTardyNumber = 0;
		for (Job job : sortedJobs) {
			curTime += job.getTimeOnStep();
			double lateness = curTime - job.getDueDate();
			if (lateness > 0) {
				thereticalMinTardyNumber++;
			}
		}

		System.out.println("-------------------------------");
		System.out.println("PTime\tDueDate\tQ");
		System.out.println("-------------------------------");

		double time = 0;
		double totalCompletionTime = 0;
		double maxLateness = Double.NEGATIVE_INFINITY;
		int tardyNumber = 0;
		while (!jobs.isEmpty()) {
			Job bestJob = null;
			double maxvalue = Double.NEGATIVE_INFINITY;
			for (Job job : jobs) {
				double value = getQValue(values, maxMinNormalizer, jobs, job, time);
				if (maxvalue < value) {
					maxvalue = value;
					bestJob = job;
				}
			}
			jobs.remove(bestJob);
			time += bestJob.getTimeOnStep();
			totalCompletionTime += time;
			double lateness = time - bestJob.getDueDate();
			if (lateness > 0) {
				tardyNumber++;
			}
			if (lateness > maxLateness) {
				maxLateness = lateness;
			}
			System.out.println(
					bestJob.getTimeOnStep() + "\t" + bestJob.getDueDate() + "\t" + String.format("%.2f", maxvalue));
		}
		System.out.println("-------------------------------");
		System.out.println("Total CT\tMax Lateness\tTardy Num");
		System.out.println("-------------------------------");
		System.out.println(thereticalTotalCompletionTime + ", " + totalCompletionTime + ";   " + thereticalMaxLateness
				+ ", " + maxLateness + ";   " + thereticalMinTardyNumber + ", " + tardyNumber);
	}

	private static double getObjective(List<Job> jobsCopy, Object values, MaxMinNormalizer maxMinNormalizer) {
		List<Job> jobs = new ArrayList<Job>(jobsCopy);

		double time = 0;
		double totalCompletionTime = 0;
		double maxLateness = Double.NEGATIVE_INFINITY;
		int tardyNumber = 0;
		while (!jobs.isEmpty()) {
			Job bestJob = null;
			double maxvalue = Double.NEGATIVE_INFINITY;
			for (Job job : jobs) {
				double value = getQValue(values, maxMinNormalizer, jobs, job, time);
				if (maxvalue < value) {
					maxvalue = value;
					bestJob = job;
				}
			}
			jobs.remove(bestJob);
			time += bestJob.getTimeOnStep();
			totalCompletionTime += time;
			double lateness = time - bestJob.getDueDate();
			if (lateness > 0) {
				tardyNumber++;
			}
			if (lateness > maxLateness) {
				maxLateness = lateness;
			}
		}
		if (objective == 1) {
			return totalCompletionTime;
		} else if (objective == 2) {
			return maxLateness;
		} else {
			return tardyNumber;
		}
	}

	private static double getQValue(QValueMap qvalues, MultiLayerPerceptron neuralNetwork,
			MaxMinNormalizer maxMinNormalizer, List<Job> state, Job action, double time) {
		if (neuralFittedQLearning) {
			return getQValue(neuralNetwork, maxMinNormalizer, state, action, time);
		}
		return getQValue(qvalues, state, action, time);
	}

	private static double getQValue(Object qvalues, List<Job> state, Job action, double time) {
		return getQValue(qvalues, null, state, action, time);
	}

	private static double getQValue(Object qvalues, MaxMinNormalizer maxMinNormalizer, List<Job> state, Job action,
			double time) {
		if (qvalues instanceof MultiLayerPerceptron) {
			MultiLayerPerceptron neuralNetwork = (MultiLayerPerceptron) qvalues;
			neuralNetwork.setInput(normalize(maxMinNormalizer, getInput(state, action, time), true));
			neuralNetwork.calculate();
			return antiNormalize(maxMinNormalizer, neuralNetwork.getOutput()[0], false);
		} else {
			QValueMap values = (QValueMap) qvalues;
			if (values.contains(state, action)) {
				return values.get(state, action);
			} else {
				values.put(state, action, 0.0);
				return 0.0;
			}
		}

	}

	static void combinations2(Job[] arr, int len, int startPosition, Job[] result, List<List<Job>> resutls) {
		if (len == 0) {
			// System.out.println(Arrays.toString(result));
			resutls.add(new ArrayList<Job>(Arrays.asList(result)));
			return;
		}
		for (int i = startPosition; i <= arr.length - len; i++) {
			result[result.length - len] = arr[i];
			combinations2(arr, len - 1, i + 1, result, resutls);
		}
	}

	private static void minimaizeNumberOfTardyJobs(List<Job> jobs) {

		int n = jobs.size();
		List<Job> J = new ArrayList<Job>();
		List<Job> Jd = new ArrayList<Job>();
		List<Job> Jc = new ArrayList<Job>();
		Jc.addAll(jobs);

		Collections.sort(Jc, new Comparator<Job>() {

			@Override
			public int compare(Job o1, Job o2) {
				return (int) (o1.getDueDate() - o2.getDueDate());
			}
		}

		);

		for (int k = 0; k < n; k++) {
			Job jobk = Jc.remove(0);
			J.add(jobk);

			double sum = 0;
			double pl = 0;
			int l = 0;
			int idx = 0;
			for (Job job : J) {
				double ptime = job.getTimeOnStep();
				sum += ptime;
				if (pl < ptime) {
					pl = ptime;
					l = idx;
				}
				idx++;
			}

			double dk = jobk.getDueDate();

			if (sum > dk) {
				Job jobL = J.remove(l);
				Jd.add(jobL);

			}
		}

		J.addAll(Jd);
		int idx = 0;

		for (Job job : J) {
			job.setPriorityByRule(idx);
			idx++;
		}

	}

	private static void nerualNetwork() {
		// create new perceptron network
		MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(2, 1, 1);
		neuralNetwork.getLearningRule().setMaxIterations(1000);
		// create training set
		DataSet trainingSet = new DataSet(2, 1);
		// add training data to training set (logical OR function)
		Random rnd = new Random(0);
		for (int i = 0; i < 100; i++) {
			int v1 = rnd.nextInt(10);
			int v2 = rnd.nextInt(10);
			int v3 = v1 + v2;
			DataSetRow row = new DataSetRow(new double[] { v1, v2 }, new double[] { v3 });
			// row.setDesiredOutput();
			trainingSet.add(row);
		}
		// learn the training set
		neuralNetwork.learn(trainingSet);
		// save the trained network into file
		neuralNetwork.save("or_perceptron.nnet");
		// load the saved network
		neuralNetwork = (MultiLayerPerceptron) MultiLayerPerceptron.createFromFile("or_perceptron.nnet");
		// set network input
		neuralNetwork.setInput(1, 8);
		// calculate network
		neuralNetwork.calculate();
		// get network output
		double[] out = neuralNetwork.getOutput();
		System.out.println(out[0]);
	}

	private static DataSet prepare() {

		Thread thread = new Thread() {
			public void run() {
				try {
					System.in.read();

					terminate = true;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		thread.start();

		int inputSize = getStateContentSize() + getActionContentSize();
		DataSet dummySet = new DataSet(inputSize, 1);
		double[] inputMin = new double[inputSize];
		double[] inputMax = new double[inputSize];

		for (int i = 0; i < inputMax.length; i++) {
			inputMax[i] = 1000;
		}
		DataSetRow row = new DataSetRow(inputMax);
		// row.setDesiredOutput(new double[] {5000});
		row.setDesiredOutput(new double[] { 999999 });
		dummySet.add(row);
		row = new DataSetRow(inputMin);
		row.setDesiredOutput(new double[] { 0 });
		dummySet.add(row);
		return dummySet;
	}

	public static double[] concatenate(double[] a, double[] b) {
		int aLen = a.length;
		int bLen = b.length;

		double[] c = new double[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

}
