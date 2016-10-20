package com.erstegroup.interview.test1.modifythis;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.erstegroup.interview.test1.dontmodifythis.AbstractTransactionUtil;
import com.erstegroup.interview.test1.exceptions.UnexpectedException;

public class ConcreteTransactionWrapper extends AbstractTransactionUtil {
	

	/** 
	 * Assignment:</br>
	 * 1. Compute a sum of distinct valid numbers, that are present in both collections</br>
	 * 2. Following must apply for string to be evaluated as a valid number:</br>
	 * 	-	String is in Standard decimal format (no special notation)</br>
	 * 	-	There are no spaces and white characters</br>
	 * 	-	Decimal separator is '.'</br>
	 * 	-	Maximal and minimal allowed values are defined by constants MIN_VALUE and MAX_VALUE in AbstractTransactionUtil</br>
	 *	-	Leading or trailing zeros ARE VALID</br>
	 * 	-	Negative numbers are allowed</br>
	 *  -	Number must start with a digit or with '-' followed by a digit
	 * 3. Strings which transformation doesn't match these rules are ignored</br>
	 * 4. Duplicate number values are counted only once (remember: 1.1 is the same as 1.10)</br>
	 * 5. Return the result as a string in following format:</br>
	 * 	-	String is in Standard decimal format (no special notation)</br>
	 * 	-	Leading or trailing zeros ARE REMOVED</br>
	 * 	-	There are no spaces and white characters</br>
	 *	-	Decimal separator is '.'</br>
	 *	-	No changes in precision (therefore no rounding is needed)</br>
	 *	-	Number must start with a digit or with '-' followed by a digit
	 * 6. Count number of method invocation on the object by executing method AbstractTransactionUtil#increaseInvocationNumber()</br>
	 * </br>
	 * Bonus tasks:</br>
	 * 	a. Ensure that calling of method <i>increaseInvocationNumber</i> is thread safe</br>
	 *  b. If either of method arguments is null, return null</br>
	 *  c. Create class com.erstegroup.interview.test1.exceptions.UnexpectedException.</br>
	 * 		Instance 'e' of this class meets following statement: "e instanceof java.lang.Exception"</br>
	 * 	d. If there were NO common values found (task 1.), method calculateCommonTransactionSum must throw this exception</br>
	 */
	public String calculateCommonTransactionSum(Collection<String> transactions1, Collection<String> transactions2) {
		synchronized(this){
			increaseInvocationNumber();
		}
		if(transactions1 == null || transactions2 == null){
			return null;
		}
		Set<BigDecimal> pairedValues = new HashSet<BigDecimal>();
		Set<BigDecimal> validNumbers1= new HashSet<BigDecimal>();
		Set<BigDecimal> validNumbers2= new HashSet<BigDecimal>();
		
		BigDecimal sum = new BigDecimal(0);

		for (String s : transactions1) {
			BigDecimal currentValue;
			if(s == null){
				continue;
			}
			if(!s.matches("^-??\\d+\\.??\\d*$")){
				continue;
			}
			try {
				currentValue = new BigDecimal(s);
			} catch (Exception e) {
				continue;
			}
			validNumbers1.add(currentValue.stripTrailingZeros());
		}
		for (String s : transactions2) {
			BigDecimal currentValue;
			if(s == null){
				continue;
			}
			if(!s.matches("^-??\\d+\\.??\\d*$")){
				continue;
			}
			try {
				currentValue = new BigDecimal(s);
			} catch (Exception e) {
				continue;
			}
			validNumbers2.add(currentValue.stripTrailingZeros());
		}
		for (BigDecimal bd:validNumbers1){			
			if (validNumbers2.contains(bd) && bd.compareTo(new BigDecimal(MIN_VALUE)) >= 0
					&& bd.compareTo(new BigDecimal(MAX_VALUE)) <= 0) {
				pairedValues.add(bd);
			}
		}	
		if (pairedValues.size() == 0) {
			throw new UnexpectedException();
		}
		for(BigDecimal bd:pairedValues){
			sum = sum.add(bd);
		}
		
		return sum.stripTrailingZeros().toString();
	}

}
