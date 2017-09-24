package ch.beni.bignumber.operation;

import java.math.BigInteger;

public abstract class Operation {

	public abstract BigInteger calculate(BigInteger[] inputs);

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
