/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.bignumber.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import ch.fankib.bignumber.engine.hazlecast.RoundRobinDistributor;

public class HazelcastNumberEngine extends BigNumberEngine {

	public static class RecursiveWorker implements Serializable, Callable<List<BigNumber>> {

		private static final long serialVersionUID = 1L;

		private RecursiveNumberEngine recursiveEngine = new RecursiveNumberEngine();

		private List<BigNumber> batch = new ArrayList<>();

		public RecursiveWorker(List<BigNumber> batch) {
			this.batch.addAll(batch);
		}

		@Override
		public List<BigNumber> call() throws Exception {
			System.out.println(Thread.currentThread().getName() + ": resolve batch! (size=" + batch.size() + ")");
			recursiveEngine.resolve(batch);
			return batch;
		}

	}

	private RoundRobinDistributor rrd;
	private IExecutorService executorService;

	public HazelcastNumberEngine(HazelcastInstance hazel) {
		rrd = new RoundRobinDistributor(null, hazel.getCluster().getMembers());
		executorService = hazel.getExecutorService("bigNumberDistributor");
	}

	@Override
	public void resolve(List<BigNumber> bigNumbers) {
		int workers = rrd.getSize();
		int perBatch = (int) Math.ceil(bigNumbers.size() / (double) workers);
		List<Future<List<BigNumber>>> futures = new ArrayList<>();
		for (int i = 0; i < workers; i++) {
			int index = i;
			Callable<List<BigNumber>> task = null;
			if (i < workers - 1) {
				task = new RecursiveWorker(bigNumbers.subList((index) * perBatch, (index + 1) * perBatch));
			} else {
				task = new RecursiveWorker(bigNumbers.subList((index) * perBatch, bigNumbers.size()));
			}
			futures.add(executorService.submitToMember(task, rrd.next()));
		}
		int mergeIndex = 0;
		for (Future<List<BigNumber>> future : futures) {
			try {
				List<BigNumber> resolvedOnes = future.get();

				// merge:
				for (BigNumber resolved : resolvedOnes) {
					bigNumbers.get(mergeIndex).resolve(resolved.getValue());
					mergeIndex++;
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void abstractResolve(BigNumber bigNumber) {
		// TODO Auto-generated method stub

	}

}
