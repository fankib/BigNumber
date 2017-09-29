package ch.fankib.bignumber.engine;

import java.io.Serializable;

public class RecursiveNumberEngine extends BigNumberEngine implements Serializable {

	private static final long serialVersionUID = 1L;

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
