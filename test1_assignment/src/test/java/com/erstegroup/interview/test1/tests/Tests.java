package com.erstegroup.interview.test1.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.erstegroup.interview.test1.dontmodifythis.AbstractTransactionUtil;
import com.erstegroup.interview.test1.modifythis.ConcreteTransactionWrapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	/**
	 * Important note: this test needs to run as first.. otherwise it behaves
	 * thread safely in every situation
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void aConcurrencyTest() throws InterruptedException {
		final int NUMBER_OF_THREADS = 10;
		final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
		final CountDownLatch latch2 = new CountDownLatch(NUMBER_OF_THREADS);
		Random r = new Random();
		class T extends Thread {
			ConcreteTransactionWrapper inst;
			Collection<String> transactions1;
			Collection<String> transactions2;

			public T(ConcreteTransactionWrapper inst, Collection<String> transactions1,
					Collection<String> transactions2) {
				this.inst = inst;
				this.transactions1 = transactions1;
				this.transactions2 = transactions2;
			}

			@Override
			public void run() {
				latch.countDown();
				inst.calculateCommonTransactionSum(transactions1, transactions2);
				latch2.countDown();
			}
		}

		List<String> a1 = new ArrayList<String>();
		List<String> a2 = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			a1.add(Integer.toBinaryString(i));
			a2.add(Integer.toBinaryString(i));
		}

		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			T t = new T(inst, a1, a2);
			t.start();
		}
		latch.await();
		latch2.await();
		assertEquals(NUMBER_OF_THREADS, inst.getNumberOfInvocations());
	}

	@Test
	public void basicFunctionalityTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("13.1");
		a1.add("15");
		List<String> a2 = new ArrayList<String>();
		a2.add("16.54353");
		a2.add("0.321312");
		a2.add("13.1");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		String result = inst.calculateCommonTransactionSum(a1, a2);
		assertTrue(result.equals("13.1"));
	}

	@Test
	public void counterTest() {
		final int NUMBER_OF_INVOCATIONS = 35;
		List<String> a1 = new ArrayList<String>();
		a1.add("13.1");
		a1.add("15");
		List<String> a2 = new ArrayList<String>();
		a2.add("16.54353");
		a2.add("0.321312");
		a2.add("13.1");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		for (int i = 0; i < NUMBER_OF_INVOCATIONS; i++) {
			inst.calculateCommonTransactionSum(a1, a2);
		}
		try {
			inst.calculateCommonTransactionSum(new ArrayList(), new ArrayList());
		} catch (Exception e) {
		}
		assertEquals(NUMBER_OF_INVOCATIONS + 1, inst.getNumberOfInvocations());
	}

	@Test
	public void formatingValidationTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("13,1");
		a1.add("0015.600");
		a1.add(" 3");
		List<String> a2 = new ArrayList<String>();
		a2.add("13,1");
		a2.add("0015.600");
		a2.add(" 3");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		String result = inst.calculateCommonTransactionSum(a1, a2);
		assertEquals("15.6", result);
	}

	@Test
	public void floatingPointTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("0.3");
		a1.add("-0.2");
		List<String> a2 = new ArrayList<String>();
		a2.add("0.3");
		a2.add("-0.2");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		String result = inst.calculateCommonTransactionSum(a1, a2);
		assertEquals("0.1", result);
	}

	@Test
	public void minMaxValidationTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("13.1");
		a1.add(AbstractTransactionUtil.MAX_VALUE + "1");
		a1.add(AbstractTransactionUtil.MIN_VALUE + "1");
		List<String> a2 = new ArrayList<String>();
		a2.add("13.1");
		a2.add(AbstractTransactionUtil.MAX_VALUE + "1");
		a2.add(AbstractTransactionUtil.MIN_VALUE + "1");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		String result = inst.calculateCommonTransactionSum(a1, a2);
		assertEquals("13.1", result);
	}

	@Test
	public void unexpectedExceptionTest() {
		boolean isExceptionThrown = false;
		try {
			List<String> a1 = new ArrayList<String>();
			List<String> a2 = new ArrayList<String>();
			ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
			inst.calculateCommonTransactionSum(a1, a2);
		} catch (Exception e) {
			if ("com.erstegroup.interview.test1.exceptions.UnexpectedException".equals(e.getClass().getName())) {
				isExceptionThrown = true;
			}
		}
		if (!isExceptionThrown) {
			fail("com.erstegroup.interview.test1.exceptions.UnexpectedException was not thrown when expected");
		}
	}

	@Test
	public void printLowestNumberTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("4322.42");
		a1.add("5.2");
		a1.add("13.1");
		List<String> a2 = new ArrayList<String>();
		a2.add("13.1");
		a2.add("5.2");
		a2.add("-48949.1545");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		inst.calculateCommonTransactionSum(a1, a2);
		assertEquals("-48949.1545", outContent.toString());
	}

	@Test
	public void zeroTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("0");
		List<String> a2 = new ArrayList<String>();
		a2.add("13,1");
		a2.add("0015.600");
		a2.add("0");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		String result = inst.calculateCommonTransactionSum(a1, a2);
		assertEquals("0", result);
	}

	@Test
	public void nullTest() {
		List<String> a1 = new ArrayList<String>();
		a1.add("14");
		a1.add(null);
		List<String> a2 = new ArrayList<String>();
		a2.add("13,1");
		a2.add(null);
		a2.add("14");
		ConcreteTransactionWrapper inst = new ConcreteTransactionWrapper();
		try {
			String result = inst.calculateCommonTransactionSum(a1, a2);
			assertEquals("14", result);
			inst.calculateCommonTransactionSum(null, null);
		} catch (Exception e) {
			fail();
			return;
		}
	}

}
