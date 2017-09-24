package ch.beni.bignumber.engine;

import java.math.BigInteger;
import java.util.List;

public abstract class BigNumberEngine {

	public void resolve(BigNumber bigNumber) {
		abstractResolve(bigNumber);
	}

	public void resolve(List<BigNumber> bigNumbers) {
		bigNumbers.forEach(bigNumber -> {
			abstractResolve(bigNumber);
		});
	}

	protected abstract void abstractResolve(BigNumber bigNumber);

	void processOperation(BigNumber bigNumber) {
		if (bigNumber.isResolved()) {
			return;
		}

		BigInteger[] inputs = convertInputs(bigNumber);
		BigInteger result = bigNumber.getOperation().calculate(inputs);
		bigNumber.resolve(result);
	}

	BigInteger[] convertInputs(BigNumber bigNumber) {
		BigInteger[] result = new BigInteger[bigNumber.getInputs().length];
		for (int i = 0; i < result.length; i++) {
			result[i] = bigNumber.getInputs()[i].getValue();
		}
		return result;
	}

}
