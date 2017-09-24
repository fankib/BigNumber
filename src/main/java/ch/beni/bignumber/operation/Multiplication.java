package ch.beni.bignumber.operation;

import java.math.BigInteger;

public class Multiplication extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		return inputs[0].multiply(inputs[1]);
	}

}
