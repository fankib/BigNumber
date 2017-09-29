package ch.fankib.bignumber.operation;

import java.math.BigInteger;

public class Addition extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		return inputs[0].add(inputs[1]);
	}

}
