package ch.beni.bignumber.operation;

import java.math.BigInteger;

public class ModularMultiplication extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		return inputs[0].multiply(inputs[1]).mod(inputs[2]);
	}

}
