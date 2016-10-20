package com.erstegroup.interview.test1.modifythis;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.erstegroup.interview.test1.dontmodifythis.AbstractTransactionUtil;
import com.erstegroup.interview.test1.exceptions.UnexpectedException;

public class ConcreteTransactionWrapper extends AbstractTransactionUtil {
	

	/** 
	 * Assignment:</br>
	 * 1. Find which strings do the two collections have in common (using equals function)</br>
	 * 2. Transform these strings to decimal numbers. Transformation must follow these instructions:</br>
	 * 	-	Strings are in Standard decimal format (no special notation)</br>
	 * 	-	There are no spaces and white characters</br>
	 * 	-	Decimal separator is '.'</br>
	 * 	-	Maximal and minimal allowed values are defined by constants MIN_VALUE and MAX_VALUE in AbstractTransactionUtil</br>
	 *	-	Leading or trailing zeros ARE AVAILABLE</br>
	 * 	-	Negative numbers are allowed</br>
	 * 3. Strings which transformation doesn't match these rules are ignored as well as null values</br>
	 * 4. Create a sum of the rest of the numbers - count duplicate values only once </br>
	 * 5. Return the result as a string in following format:</br>
	 * 	-	Strings are in Standard decimal format (no special notation)</br>
	 * 	-	Leading or trailing zeros ARE REMOVED</br>
	 * 	-	There are no spaces and white characters</br>
	 *	-	Decimal separator is '.'</br>
	 *	-	No changes in precision or rounding necessary</br>
	 * 6. Count number of method invocation on the instance including method {@link AbstractTransactionUtil#increaseInvocationNumber()}</br>
	 * </br>
	 * Bonus tasks:</br>
	 * 	a. Create class com.erstegroup.interview.test1.exceptions.UnexpectedException.</br>
	 * 		For instance 'e' of this class is following statement true: "e instanceof java.lang.Exception"</br>
	 * 	b. If there were NO common strings found, method calculateCommonTransactionSum must throw this exception</br>
	 *  c. Ensure that calling of method <i>increaseInvocationNumber</i> is thread safe</br>
	 *  d. If UnexpectedException was not thrown, print single non-escaped lowest valid number from both transactions to System.out stream</br>
	 *  
	 * 
	 * 
	 */
	public String calculateCommonTransactionSum(Collection<String> transactions1, Collection<String> transactions2) {
		synchronized (this) {
			increaseInvocationNumber();
		}
		if(transactions1 == null || transactions2 == null){
			System.err.print("Null error");
		}
		BigDecimal minValue = new BigDecimal(MAX_VALUE);
		Set<String> pairedStrings = new HashSet<String>();
		Set<BigDecimal> pairedValues = new HashSet<BigDecimal>();
		BigDecimal currentValue;
		BigDecimal sum = new BigDecimal(0);

		for (String s : transactions1) {
			if(transactions2.contains(s)){
				pairedStrings.add(s);
			}
			try {
				currentValue = new BigDecimal(s);
			} catch (Exception e) {
				continue;
			}
			if (currentValue.compareTo(new BigDecimal(MIN_VALUE)) >= 0
					&& currentValue.compareTo(new BigDecimal(MAX_VALUE)) <= 0 && transactions2.contains(s)) {
				sum = sum.add(currentValue);
				pairedValues.add(currentValue.stripTrailingZeros());
				if (currentValue.compareTo(minValue) < 0) {
					minValue = currentValue;
				}
			}
		}
		if (pairedStrings.size() == 0) {
			throw new UnexpectedException();
		}
		for (String s : transactions2) {
			try {
				currentValue = new BigDecimal(s);
			} catch (Exception e) {
				continue;
			}
			if (currentValue.compareTo(new BigDecimal(MIN_VALUE)) >= 0
					&& currentValue.compareTo(new BigDecimal(MAX_VALUE)) <= 0 && currentValue.compareTo(minValue) < 0
					&& !pairedValues.contains(currentValue)) {
				minValue = currentValue;
			}
		}
		System.out.print(minValue);
		return sum.stripTrailingZeros().toString();
	}

}
