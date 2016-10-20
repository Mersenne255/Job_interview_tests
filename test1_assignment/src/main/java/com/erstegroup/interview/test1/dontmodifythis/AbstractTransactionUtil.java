package com.erstegroup.interview.test1.dontmodifythis;

import java.util.Collection;
import java.util.Random;

public abstract class AbstractTransactionUtil {
	public final static String MIN_VALUE = "-999999.999999";
	public final static String MAX_VALUE = "999999.999999";

	private int numberOfInvocations;

	public abstract String calculateCommonTransactionSum(Collection<String> transactions1,
			Collection<String> transactions2);

	/**
	 * The optimally implemented method for counting number of invocations.
	 * (also used to mess with the concurrency if not properly implemented)
	 */
	protected void increaseInvocationNumber() {
		Random r = new Random();
		int frustrator;
		int looper = r.nextInt(1000000);
		for (int i = 0; i < looper; i++) {
			frustrator = r.nextInt(10000);
			numberOfInvocations += frustrator;
			numberOfInvocations -= frustrator;

		}
		numberOfInvocations = new Integer(Integer.toString(numberOfInvocations)) + 1;
	}

	public int getNumberOfInvocations() {
		return numberOfInvocations;
	}

}
