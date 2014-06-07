package com.ssn.test.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssn.test.client.callables.ReadCallable;
import com.ssn.test.client.callables.WriteCallable;

public class SimulateReadsAndWrites {
	private static final Logger LOG = LoggerFactory.getLogger(SimulateReadsAndWrites.class);
	
	public static final int NUMBER_OF_THREADS = 10;
	public static final int NUMBER_OF_ITERATIONS_PER_THREAD = 10;
	public static final int WORKER_MULTIPLICATION_FACTOR = 5;
	
	public static void main(String[] args) {
		long startTime = new Date().getTime();
		ExecutorService writeExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		ExecutorService readExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		List<Future<List<Integer>>> readList = new ArrayList<Future<List<Integer>>>();
		List<Future<List<Float>>> writeList = new ArrayList<Future<List<Float>>>();
		
		long beforeSubmitWriteFutures = new Date().getTime();
		for(int i = 0; i < NUMBER_OF_THREADS * WORKER_MULTIPLICATION_FACTOR; i++) {
			Callable<List<Float>> worker = new WriteCallable();
			Future<List<Float>> submit = writeExecutor.submit(worker);
			writeList.add(submit);
			LOG.debug("Added write worker + " + (i + 1));
		}
		LOG.debug("Done adding write workers.");
		writeExecutor.shutdown();
		LOG.debug("Write executor shutdown");
		
		long beforeSubmitReadFutures = new Date().getTime();
		for(int i = 0; i < NUMBER_OF_THREADS * WORKER_MULTIPLICATION_FACTOR; i++) {
			Callable<List<Integer>> worker = new ReadCallable();
			Future<List<Integer>> submit = readExecutor.submit(worker);
			readList.add(submit);
			LOG.debug("Added read worker + " + (i + 1));
		}
		LOG.debug("Done adding read workers");
		readExecutor.shutdown();
		LOG.debug("Read executor shutdown");
		
		// Start printing results
		long beforePrintingWriteResults = new Date().getTime();
		for(Future<List<Float>> future : writeList) {
			try {
				List<Float> values = future.get();
				LOG.debug("Values saved : " + values);
				if(values.size() != NUMBER_OF_ITERATIONS_PER_THREAD) {
					LOG.error("*** This thread missed the WRITE count. Expected = " + NUMBER_OF_ITERATIONS_PER_THREAD + ", Actual = " + values.size());
				}
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Exception in printing writeList block", e);
			}
		}
		
		long beforePrintingReadResults = new Date().getTime();
		for(Future<List<Integer>> future : readList) {
			try {
				List<Integer> values = future.get();
				LOG.debug("Number of rows fetched in each iteration: " + values);
				
				if(values.size() != NUMBER_OF_ITERATIONS_PER_THREAD) {
					LOG.error("*** This thread missed the READ count. Expected = " + NUMBER_OF_ITERATIONS_PER_THREAD + ", Actual = " + values.size());
				}
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Exception in printing readList block", e);
			}
		}
		
		long atTheEnd = new Date().getTime();
		
		// Print diagnostic results.
		LOG.info("Time taken to prep variables = " + (beforeSubmitWriteFutures - startTime) + " ms");
		LOG.info("Time taken to submit write futures = " + (beforeSubmitReadFutures - beforeSubmitWriteFutures) + " ms");
		LOG.info("Time taken to submit read futures = " + (beforePrintingWriteResults - beforeSubmitReadFutures) + " ms");
		LOG.info("Time taken to print write results = " + (beforePrintingReadResults - beforePrintingWriteResults) + " ms");
		LOG.info("Time taken to print read results = " + (atTheEnd - beforePrintingReadResults) + " ms");
		LOG.info("Total time taken = " + (atTheEnd - startTime) + " ms");
	}

}
