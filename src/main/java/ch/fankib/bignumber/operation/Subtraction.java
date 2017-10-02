package ch.fankib.bignumber.operation;

import java.math.BigInteger;

public class Subtraction extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		return inputs[0].subtract(inputs[1]);
	}

}
