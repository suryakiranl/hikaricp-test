package com.ssn.test.client.callables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.ssn.test.client.SimulateReadsAndWrites;
import com.ssn.test.dao.util.ScoresDAOImpl;

public class ReadCallable implements Callable<List<Integer>> {
	/**
	 * This method will call the loadAll API 1000 times and 
	 * returns the number of rows retrieved in each request as a
	 * list.
	 */
	@Override
	public List<Integer> call() throws Exception {
		List<Integer> recordCount = new ArrayList<Integer>();
		for(int i = 0; i < SimulateReadsAndWrites.NUMBER_OF_ITERATIONS_PER_THREAD; i++) {
			recordCount.add(ScoresDAOImpl.countRows());	
		}
		
		return recordCount;
	}

}
