/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.bignumber.engine.hazlecast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hazelcast.core.Member;

public class RoundRobinDistributor {

	private List<Member> members = new ArrayList<>();

	private int index = 0;
	private int size;

	public RoundRobinDistributor(Member excludedMember, Set<Member> members) {
		for (Member member : members) {
			if (!member.equals(excludedMember)) {
				this.members.add(member);
			}
		}
		this.size = this.members.size();
	}

	public Member next() {
		return members.get(index++ % size);
	}

	public int getSize() {
		return size;
	}

}
