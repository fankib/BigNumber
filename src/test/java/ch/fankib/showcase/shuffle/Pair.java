/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.showcase.shuffle;

import ch.fankib.bignumber.engine.BigNumber;

public class Pair {

	private BigNumber first;

	private BigNumber second;

	public Pair(BigNumber first, BigNumber second) {
		this.first = first;
		this.second = second;
	}

	public BigNumber getFirst() {
		return first;
	}

	public BigNumber getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return "Pair [" + first + ", " + second + "]";
	}

}
