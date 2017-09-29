/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.bignumber.engine.hazlecast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;

import ch.fankib.bignumber.utils.IndexedList;

public class Worker {

	public static void main(String[] args) {
		Config config = new Config();

		new IndexedList(2).parallelStream().forEach(index -> {
			Hazelcast.newHazelcastInstance(config);
		});
	}

}
