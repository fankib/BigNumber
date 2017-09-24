package ch.fankib.bignumber.engine;

import java.math.BigInteger;
import java.util.Arrays;

import ch.fankib.bignumber.operation.Operation;
import ch.fankib.bignumber.operation.Operations;

/**
 * @author beni
 *
 */
public class BigNumber {

	private static final BigNumberEngine DEFAULT_ENGINE = new RecursiveNumberEngine();

	private BigInteger value;

	private BigNumber[] inputs;
	private Operation operation;

	public BigNumber(Operation operation, BigNumber... inputs) {
		this.inputs = inputs;
		this.operation = operation;
	}

	public BigNumber(String value) {
		this.value = new BigInteger(value);
	}

	public boolean isResolved() {
		return this.value != null;
	}

	public BigInteger getValue() {
		return value;
	}

	BigNumber[] getInputs() {
		return inputs;
	}

	Operation getOperation() {
		return operation;
	}

	void resolve(BigInteger value) {
		this.value = value;
	}

	public BigNumber add(BigNumber addend) {
		return new BigNumber(Operations.ADDITION, this, addend);
	}

	public BigNumber mult(BigNumber factor) {
		return new BigNumber(Operations.MULTIPLICATION, this, factor);
	}

	public BigNumber modExp(BigNumber exponent, BigNumber modulus) {
		return new BigNumber(Operations.MOD_EXP, this, exponent, modulus);
	}

	public BigNumber modMult(BigNumber factor, BigNumber modulus) {
		return new BigNumber(Operations.MOD_MULT, this, factor, modulus);
	}

	public boolean test(BigNumber other) {
		// resolve first:
		if (!this.isResolved()) {
			DEFAULT_ENGINE.resolve(this);
		}
		if (!other.isResolved()) {
			DEFAULT_ENGINE.resolve(other);
		}
		return this.getValue().equals(other.getValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(inputs);
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigNumber other = (BigNumber) obj;
		if (!Arrays.equals(inputs, other.inputs))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (isResolved()) {
			return "BigNumber [value=" + value + "]";
		}
		return "BigNumber [operation=" + operation + ", inputs=" + Arrays.toString(inputs) + "]";
	}


}
