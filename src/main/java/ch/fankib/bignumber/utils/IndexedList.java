/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.bignumber.utils;

import java.util.ArrayList;

public class IndexedList extends ArrayList<Integer> {

	public IndexedList(int maxIndex) {
		for (int i = 1; i <= maxIndex; i++) {
			this.add(i);
		}
	}

}
