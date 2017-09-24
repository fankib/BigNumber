package ch.beni.bignumber.operation;

import java.math.BigInteger;

public class ModularExponation extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		return inputs[0].modPow(inputs[1], inputs[2]);
	}

}
