package ch.fankib.bignumber.engine;

public class RecursiveNumberEngine extends BigNumberEngine {

	@Override
	protected void abstractResolve(BigNumber bigNumber) {
		for (BigNumber input : bigNumber.getInputs()) {
			if (!input.isResolved()) {
				resolve(input);
			}
		}
		processOperation(bigNumber);
	}


}
