package ch.fankib.bignumber.engine;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import ch.fankib.bignumber.operation.EqualTest;
import ch.fankib.bignumber.operation.Operation;
import ch.fankib.bignumber.operation.Operations;
import ch.fankib.bignumber.operation.RandomValue;

/**
 * @author beni
 *
 */
public class BigNumber implements Serializable {

	public static class ConditionBuilder {

		private BigNumber test;
		private BigNumber equals;
		private BigNumber then;

		protected ConditionBuilder() {
		}

		public ConditionBuilder test(BigNumber test) {
			this.test = test;
			return this;
		}

		public ConditionBuilder equals(BigNumber equals) {
			this.equals = equals;
			return this;
		}

		public ConditionBuilder then(BigNumber then) {
			this.then = then;
			return this;
		}

		public BigNumber otherwise(BigNumber otherwise) {
			if (test == null || equals == null || then == null || otherwise == null) {
				throw new IllegalArgumentException();
			}
			return new ConditionalBigNumber(Operations.EQUAL_TEST, test, equals, then, otherwise);
		}

	}

	private static class ConditionalBigNumber extends BigNumber {

		private BigNumber then;
		private BigNumber otherwise;

		public ConditionalBigNumber(Operation operation, BigNumber test, BigNumber equals, BigNumber then, BigNumber otherwise) {
			super(operation, test, equals);
			this.then = then;
			this.otherwise = otherwise;
		}

		@Override
		void resolve(BigInteger value) {
			BigNumber toResolve = null;
			if (value.equals(EqualTest.TRUE)) {
				toResolve = then;
			} else {
				toResolve = otherwise;
			}
			super.resolve(toResolve.resolveNow().getValue());
		}

	}

	private static final long serialVersionUID = 1L;

	private static final BigNumberEngine DEFAULT_ENGINE = new RecursiveNumberEngine();

	public static final BigNumber ONE = new BigNumber("1");

	private BigInteger value;

	private BigNumber[] inputs;
	private Operation operation;

	public ConditionBuilder test(BigNumber equals) {
		ConditionBuilder builder = new ConditionBuilder();
		builder.test(this);
		builder.equals(equals);
		return builder;
	}

	public BigNumber(Operation operation, BigNumber... inputs) {
		this.inputs = inputs;
		this.operation = operation;
	}

	public BigNumber(String value) {
		this.value = new BigInteger(value);
	}

	public BigNumber(BigInteger value) {
		this.value = value;
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

	public BigNumber slowAdd(BigNumber addend) {
		return new BigNumber(Operations.SlOW_ADDITION, this, addend);
	}

	public BigNumber subtract(BigNumber subtrahend) {
		return new BigNumber(Operations.SUBTRACTION, this, subtrahend);
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

	public BigNumber random() {
		return new BigNumber(new RandomValue(new SecureRandom()), BigNumber.ONE, this);
	}

	public BigNumber random(BigNumber includedMinValue) {
		return new BigNumber(new RandomValue(new SecureRandom()), includedMinValue, this);
	}

	public BigNumber resolveNow() {
		defaultResolve(this);
		return this;
	}

	private void defaultResolve(BigNumber number) {
		if (!number.isResolved()) {
			DEFAULT_ENGINE.resolve(this);
		}
	}

	public boolean testNow(BigNumber other) {
		defaultResolve(this);
		defaultResolve(other);
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
