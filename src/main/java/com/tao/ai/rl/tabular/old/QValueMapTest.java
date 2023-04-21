package com.tao.ai.rl.tabular.old;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;



class QValueMapTest {

	@Test
	void testGetListOfJobJob() {
		QValueMap values=new QValueMap();
		
		Job job1=new Job();
		Job job2=new Job();
		Job job3=new Job();
		Job job4=new Job();
		Job job5=new Job();
		
		List<Job> jobs1=new ArrayList<Job>();
		jobs1.add(job1);
		jobs1.add(job3);
		
		State state1=new State(jobs1);
		
		List<Job> jobs2=new ArrayList<Job>();
		jobs2.add(job1);
		jobs2.add(job3);
		
		State state2=new State(jobs2);
		
		assertEquals(state1,state2);
		
		values.put(jobs1, job4, -1);
		
		assertTrue(values.contains(jobs2, job4));
		
		assertEquals(-1,values.get(jobs2,job4));
		
		
	}

	//@Test
	void testContains() {
		//fail("Not yet implemented");
	}

}
