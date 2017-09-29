package ch.fankib.bignumber.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BatchingNumberEngine extends BigNumberEngine {

	private static final int THREADS = 8;

	ExecutorService service = Executors.newFixedThreadPool(THREADS);
	RecursiveNumberEngine recursiveEngine = new RecursiveNumberEngine();

	@Override
	public void resolve(List<BigNumber> bigNumbers) {
		int perBatch = (int) Math.ceil(bigNumbers.size() / (double) THREADS);
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < THREADS; i++) {
			int index = i;
			if (i < THREADS - 1) {
				futures.add(service.submit(() -> {
					recursiveEngine.resolve(bigNumbers.subList((index) * perBatch, (index + 1) * perBatch));
				}));
			} else {
				futures.add(service.submit(() -> {
					recursiveEngine.resolve(bigNumbers.subList((index) * perBatch, bigNumbers.size()));
				}));
			}
		}
		for (Future<?> future : futures) {
			try {
				future.get();
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
