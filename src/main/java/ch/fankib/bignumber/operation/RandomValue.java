/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.bignumber.operation;

import java.math.BigInteger;
import java.util.Random;

public class RandomValue extends Operation {

	Random random;

	public RandomValue(Random random) {
		this.random = random;
	}

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		BigInteger includedMinValue = inputs[0];
		BigInteger excludedMaxValue = inputs[1];
		BigInteger r = null;
		do {
			r = new BigInteger(excludedMaxValue.bitLength(), random);
		} while (includedMinValue.compareTo(r) > 0 || r.compareTo(excludedMaxValue) >= 0);
		return r;
	}

}
