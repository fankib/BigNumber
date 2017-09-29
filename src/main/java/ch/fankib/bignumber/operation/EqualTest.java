package ch.fankib.bignumber.operation;

import java.math.BigInteger;

public class EqualTest extends Operation {

	public static final BigInteger TRUE = BigInteger.valueOf(1);
	public static final BigInteger FALSE = BigInteger.valueOf(0);

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		if (inputs[0].equals(inputs[1])) {
			return TRUE;
		}
		return FALSE;
	}

}
