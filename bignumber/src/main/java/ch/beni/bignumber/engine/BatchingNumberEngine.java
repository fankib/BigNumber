package ch.beni.bignumber.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BatchingNumberEngine extends BigNumberEngine {

	ExecutorService service = Executors.newFixedThreadPool(4);
	RecursiveNumberEngine recursiveEngine = new RecursiveNumberEngine();

	@Override
	public void resolve(List<BigNumber> bigNumbers) {
		int perBatch = (int) Math.ceil(bigNumbers.size() / 4.0);
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			int index = i;
			if (i < 3) {
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
