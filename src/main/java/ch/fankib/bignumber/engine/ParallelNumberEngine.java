package ch.fankib.bignumber.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelNumberEngine extends BigNumberEngine {

	ExecutorService service = Executors.newCachedThreadPool();

	public void resolveSharedInputs(Collection<BigNumber> bigNumbers) {
		if (bigNumbers.isEmpty()) {
			return;
		}

		Set<BigNumber> unresolvedInputs = new HashSet<>();
		for (BigNumber bigNumber : bigNumbers) {
			for (BigNumber input : bigNumber.getInputs()) {
				if (!input.isResolved()) {
					unresolvedInputs.add(input);
				}
			}
		}

		resolveSharedInputs(unresolvedInputs);

		resolveWithResolvedInputs(bigNumbers);
	}

	public void resolveWithResolvedInputs(Collection<BigNumber> bigNumbers) {
		List<Future<?>> resolvers = new ArrayList<>();
		for (BigNumber bigNumber : bigNumbers) {
			submit(resolvers, () -> {
				processOperation(bigNumber);
			});
		}
		wait(resolvers);
	}

	public void resolveIndependentInputs(Collection<BigNumber> bigNumbers) {
		List<Future<?>> resolvers = new ArrayList<>();
		for (BigNumber bigNumber : bigNumbers) {
			submit(resolvers, () -> {
				abstractResolve(bigNumber);
			});
		}
		wait(resolvers);
	}

	@Override
	protected void abstractResolve(BigNumber bigNumber) {
		List<Future<?>> inputResolvers = new ArrayList<>();
		for (BigNumber input : bigNumber.getInputs()) {
			if (!input.isResolved()) {
				submit(inputResolvers, () -> {
					resolve(input);
				});
			}
		}

		wait(inputResolvers);

		processOperation(bigNumber);
	}

	private void submit(List<Future<?>> futures, Runnable task) {
		futures.add(service.submit(task));
	}

	private void wait(List<Future<?>> futures) {
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
