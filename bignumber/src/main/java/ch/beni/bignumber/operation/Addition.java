package ch.beni.bignumber.operation;

import java.math.BigInteger;

public class Addition extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		int result = 1;
		for (int i = 0; i < 100000; i++) {
			if (result % 2 == 0) {
				result -= i;
			} else {
				result += i;
			}
		}
		return inputs[0].add(inputs[1]);
	}

}
