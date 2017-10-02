package ch.fankib.bignumber.engine.hazlecast;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class Producer {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		HazelcastInstance h = Hazelcast.newHazelcastInstance();
		IExecutorService ex = h.getExecutorService("distributed");

		int a = 0;
		int b = 0;

		RoundRobinDistributor rrd = new RoundRobinDistributor(h.getCluster().getLocalMember(), h.getCluster().getMembers());

		while (true) {

			a++;
			b++;

			Future<Integer> future = ex.submitToMember(new MessagePrinter(a, b), rrd.next());
			Integer result = future.get();

			System.out.println("resolved to " + result);

			Thread.sleep(2000);

		}
	}

	static class MessagePrinter implements Callable<Integer>, Serializable {

		private int base;
		private int addend;

		public MessagePrinter(int base, int addend) {
			this.base = base;
			this.addend = addend;
		}

		@Override
		public Integer call() throws Exception {
			System.out.println("calculate " + base + " + " + addend);
			return base + addend;
		}

	}
}
