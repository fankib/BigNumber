package ch.fankib.bignumber.engine.hazlecast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import ch.fankib.bignumber.engine.BigNumber;

public class ChannelAdapter {

	HazelcastInstance h = Hazelcast.newHazelcastInstance();

	IExecutorService ex;

	public ChannelAdapter() {
		ex = h.getExecutorService("distributed");
	}

	public void resolve(BigNumber number) {

	}

}
