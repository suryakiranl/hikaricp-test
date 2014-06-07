package com.ssn.test.client.callables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import com.ssn.test.client.SimulateReadsAndWrites;
import com.ssn.test.dao.util.ScoresDAOImpl;

public class WriteCallable implements Callable<List<Float>> {

	/**
	 * This method writes 1000 rows to the database and returns
	 * the randomly generated score_values as a list.
	 */
	@Override
	public List<Float> call() throws Exception {
		List<Float> scoreValues = new ArrayList<Float>();
		
		for(int i = 0 ; i < SimulateReadsAndWrites.NUMBER_OF_ITERATIONS_PER_THREAD; i++) {
			float score_value = new Random().nextFloat();
			ScoresDAOImpl.save(score_value);
			scoreValues.add(score_value);
		}
		
		return scoreValues;
	}

}
