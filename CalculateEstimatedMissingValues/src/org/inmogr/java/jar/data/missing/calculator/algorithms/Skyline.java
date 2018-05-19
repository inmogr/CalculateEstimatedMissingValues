package org.inmogr.java.jar.data.missing.calculator.algorithms;

import java.util.List;

import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;

public class Skyline {
	
	/** Checks if the given row's values are less than any another row's values */
	public static boolean isSmallest(RowExt row, List<RowExt> rows) {
		for (RowExt row2 : rows) {
			if (checkValues(row.getValues(), row2.getValues()) == -1)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if row's double values are greater, smaller, or undetermined
	 * if the values (based on its indexes) are equal or higher then, number 1 will be returned
	 * else if the values (based on its indexes) are smaller then, number -1 will be returned
	 * else we cannot say that one of the values is higher or smaller then, then it is undetermined with value of 0 
	 **/
	private static int checkValues(Double[] values1, Double[] values2) {
		/**
		 * Identifier will identify the relation between the values of two rows
		 * 		whereby, if the first row is greater then identifier will be positive
		 * 				and if the second row is greater then identifier will be negative
		 * 				else identifier will be ZERO as cannot be determined
		 **/
		int identifier = isFirstValueGreater(values1[0], values2[0]);
		for (int i = 1; i < values1.length; i++) {
			int temp = isFirstValueGreater(values1[i], values2[i]);
			if (temp != identifier) {
				return 0;
			}
		}
		return identifier;
	}
	
	/**
	 * Checks if two double values are equal or higher then return 1
	 * else return -1
	 **/
	private static int isFirstValueGreater(double val1, double val2) {
		// check if first value is greater than the second value
		if (val1 >= val2)
			return 1;
		else return -1;
	}

}
