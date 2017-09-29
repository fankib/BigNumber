/* 
 * Copyright E-Voting Group BFH, Bern University of Applied Sciences
 * Exclusively licensed to Valuetainment AG
 */
package ch.fankib.showcase.shuffle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;

import ch.fankib.bignumber.engine.BatchingNumberEngine;
import ch.fankib.bignumber.engine.BigNumber;
import ch.fankib.bignumber.engine.BigNumberEngine;

public class ShuffleProofShowcase {

	// Modulus
	public static final BigNumber P = new BigNumber(
			"16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762583194718667771801067716614802322659239302476074096777926805529798824879");

	// Order
	public static final BigNumber Q = new BigNumber(
			"8079251517827751825178719172167487990111025667428871008032586356881163784716972723299300352880728365922179490230474504873529889787622730273772038096612070780157719341825249022937549437597413026699014409596016892069198054660654939040459523584619042617645411463009076260721893972885266452151888099780982596380478583347417085605171243696641142373714044008831580514519451414832756548177115078537564648216044279181485900929615464339399587788075411476100924403308321807806781421177705052431289275431732830867419635645164174483761499317088249659553881291597359333885900533858307401161329619651238037048388963402764899412439");

	// Generator
	public static final BigNumber G = new BigNumber("4");

	// keys
	public static final BigNumber SK = Q.random().resolveNow();
	public static final BigNumber Y = G.modExp(SK, P).resolveNow();

	private BigNumberEngine engine = new BatchingNumberEngine();
	// private BigNumberEngine engine = new RecursiveNumberEngine();

	@Rule
	public final Stopwatch stopwatch = new Stopwatch() {
	};

	@Test
	public void randomTest() {
		BigNumber max = new BigNumber("10");
		for (int i = 0; i < 1000; i++) {
			int random = max.random().resolveNow().getValue().intValue();
			Assert.assertTrue(random >= 1);
			Assert.assertTrue(random < 10);
		}
	}

	@Test
	public void randomTest2() {
		BigNumber max = new BigNumber("10");
		for (int i = 0; i < 1000; i++) {
			int random = max.random(new BigNumber("5")).resolveNow().getValue().intValue();
			Assert.assertTrue(random >= 5);
			Assert.assertTrue(random < 10);
		}
	}

	@Test
	public void permutationTest() {
		List<Integer> permutation = generatePermutation(50);
		Integer sum = permutation.parallelStream().reduce(0, Integer::sum);
		Assert.assertEquals(Integer.valueOf(1275), sum);
	}

	@Test
	public void testShuffleProof() {

		List<Pair> ciphers = createCipherTexts();

		// reencrypt:
		List<Pair> reencryptions = reencrypt(ciphers);

		// permutate:

		List<Integer> permutation = generatePermutation(ciphers.size());

		List<Pair> shuffle = shuffle(reencryptions, permutation);

		engine.resolve(flat(shuffle));

		// generate proofs

	}

	private List<Pair> shuffle(List<Pair> reencryptions, List<Integer> permutation) {
		return permutation.stream().map(index -> {
			return reencryptions.get(index - 1);
		}).collect(Collectors.toList());
	}

	private List<Integer> generatePermutation(int size) {
		List<Integer> I = new IndexedList(size);

		BigNumber kGenerator = new BigNumber("" + size);

		List<Integer> permutation = new ArrayList<>(size);

		for (int i = 0; i < size; i++) {
			int k = kGenerator.random(new BigNumber("" + i)).resolveNow().getValue().intValue();
			permutation.add(I.get(k));
			I.set(k, I.get(i));
		}

		return permutation;
	}

	private BigNumber decrypt(Pair pair) {
		BigNumber a = pair.getFirst();
		BigNumber b = pair.getSecond();

		return b.modMult(a.modExp(Q.subtract(SK), P), P);
	}

	private List<Pair> reencrypt(List<Pair> ciphers) {
		return ciphers.parallelStream().map(cipher -> {

			BigNumber r = Q.random();
			BigNumber aPrime = G.modExp(r, P);
			BigNumber bPrime = Y.modExp(r, P);

			BigNumber a = aPrime.modMult(cipher.getFirst(), P);
			BigNumber b = bPrime.modMult(cipher.getSecond(), P);

			return new Pair(a, b);
		}).collect(Collectors.toList());
	}

	private List<BigNumber> flat(List<Pair> pairs) {
		List<BigNumber> flatList = new ArrayList<>();
		for (Pair pair : pairs) {
			flatList.add(pair.getFirst());
			flatList.add(pair.getSecond());
		}
		return flatList;
	}

	private List<Pair> createCipherTexts() {
		int messageCount = 800;

		return new IndexedList(messageCount).parallelStream().map((m) -> {
			// map Zq to Gq
			BigNumber m_z = new BigNumber("" + m);
			BigNumber m_q = null;
			if (m_z.add(BigNumber.ONE).modExp(Q, P).test(BigNumber.ONE)) {
				m_q = m_z.add(BigNumber.ONE);
			} else {
				m_q = P.subtract(m_z.add(BigNumber.ONE));
			}

			// encrypt:
			BigNumber r = Q.random();
			BigNumber a = G.modExp(r, P);
			BigNumber b = m_q.modMult(Y.modExp(r, P), P);

			return new Pair(a, b);
		}).collect(Collectors.toList());

	}

}
