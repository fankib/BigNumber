package ch.fankib.bignumber.operation;

import java.math.BigInteger;

import com.squareup.jnagmp.Gmp;

public class ModularExponation extends Operation {

	@Override
	public BigInteger calculate(BigInteger[] inputs) {
		BigInteger b = inputs[0];
		BigInteger e = inputs[1];
		BigInteger m = inputs[2];

		if (m.testBit(0)) {
			return Gmp.modPowSecure(b, e, m);
		} else {
			return Gmp.modPowInsecure(b, e, m); // Gmp.modPowSecure requires
												// modulus to be odd
		}
	}

}
