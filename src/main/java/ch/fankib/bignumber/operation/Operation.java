package ch.fankib.bignumber.operation;

import java.io.Serializable;
import java.math.BigInteger;

public abstract class Operation implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract BigInteger calculate(BigInteger[] inputs);

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
