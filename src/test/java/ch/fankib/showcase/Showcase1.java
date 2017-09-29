package ch.fankib.showcase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.fankib.bignumber.engine.BatchingNumberEngine;
import ch.fankib.bignumber.engine.BigNumber;
import ch.fankib.bignumber.engine.BigNumberEngine;
import ch.fankib.bignumber.engine.ParallelNumberEngine;
import ch.fankib.bignumber.engine.RecursiveNumberEngine;

public class Showcase1 {

	private BigNumber order = new BigNumber("83");
	private BigNumber modulus = new BigNumber("167");
	private BigNumber generator = new BigNumber("4");

	@Test
	public void showcase1_1() {
		BigNumber one = new BigNumber("1");

		BigNumber inGroup = new BigNumber("16");
		BigNumber notInGroup = new BigNumber("17");

		Assert.assertTrue(inGroup.modExp(order, modulus).testNow(one));
		Assert.assertFalse(notInGroup.modExp(order, modulus).testNow(one));
		Assert.assertTrue(generator.modExp(order, modulus).testNow(one));
	}

	@Test
	public void showcaseParallel() {
		BigNumber a = new BigNumber("1");
		BigNumber b = new BigNumber("2");

		BigNumber c = a.slowAdd(b);

		BigNumber d = new BigNumber("3");

		BigNumber result = c.slowAdd(d);

		BigNumberEngine parallelEngine = new ParallelNumberEngine();
		parallelEngine.resolve(result);
		Assert.assertEquals("6", result.getValue().toString());
	}

	@Test
	public void showcaseParallelIndependentInputs() {
		List<BigNumber> result = createTestSet1();
		ParallelNumberEngine engine = new ParallelNumberEngine();
		engine.resolveIndependentInputs(result);
	}

	@Test
	public void showcaseParallelSharedInput() {
		List<BigNumber> result = createTestSet1();
		ParallelNumberEngine engine = new ParallelNumberEngine();
		engine.resolveSharedInputs(result);
	}

	@Test
	public void showcaseParallelBaseline() {
		List<BigNumber> result = createTestSet1();
		RecursiveNumberEngine engine = new RecursiveNumberEngine();
		engine.resolve(result);
	}

	@Test
	public void showcaseParallelBatch() {
		List<BigNumber> result = createTestSet1();
		BatchingNumberEngine engine = new BatchingNumberEngine();
		engine.resolve(result);
	}

	private List<BigNumber> createTestSet1() {
		BigNumber a = new BigNumber("1");
		List<BigNumber> result = new ArrayList<>();

		for (int i = 0; i < 100000; i++) {
			BigNumber nrI = new BigNumber("" + i);
			result.add(a.slowAdd(nrI));
		}

		return result;
	}

}
