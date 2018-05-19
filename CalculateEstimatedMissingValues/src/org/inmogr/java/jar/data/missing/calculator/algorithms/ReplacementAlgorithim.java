package org.inmogr.java.jar.data.missing.calculator.algorithms;

import java.util.ArrayList;

import org.inmogr.java.jar.data.missing.calculator.classes.DataSet;
import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;

public class ReplacementAlgorithim {
	
	/** Used to determine the main attributes to be used for comparison */
	private String[] comparativeAttributes;
	
	/**
	 * REQUIRED
	 * this is to define which attributes to be taken into consideration to find relations between rows
	 **/
	private void setComparativeAttributes(String[] comparativeAttributes) {
		this.comparativeAttributes = comparativeAttributes;
	}
	
	/** The given complete and incomplete data sets */
	private DataSet complete, incomplete;
	/** The algorithmic generated incomplete (crowdSource) and complete (calculatedValues) data sets */
	private DataSet crowdSource, calculatedValues;
	
	public DataSet getComplete() {
		return complete;
	}
	
	public DataSet getIncomplete() {
		return incomplete;
	}
	
	public DataSet getCrowdSource() {
		return crowdSource;
	}
	
	public DataSet getCalculatedValues() {
		return calculatedValues;
	}
	
	/**
	 * The Constructor requires knowing the complete and incomplete data sets
	 **/
	public ReplacementAlgorithim(DataSet complete, DataSet incomplete) {
		this.complete = complete;
		this.incomplete = incomplete;
	}
	
	/**
	 * Learns from the relations in the complete data set, to replace missing values with estimated values
	 **/
	public void findMissingValues(String[] attrs) {
		// holds the relatives of each row depending of the 1 to 1 relations from the attributes {@see attrs} given
		ArrayList<ArrayList<RowExt>> relationsHolder = new ArrayList<>();
		// iterate over attributes to find relations in terms of two specific attributes
		for (int i = 1; i < attrs.length; i++) {
			// identify the two attributes to compare two rows' values
			String[] array = {attrs[0], attrs[i]};
			// inform class of the comparative attributes
			setComparativeAttributes(array);
			// if this is the first iteration the holder still empty so just fill it up
			if (i == 1)
				relationsHolder.addAll(findRelatedRowsForRows());
			// next iterations, combine the previous relations with the current ones without changing the order
			else {
				// create a temporary {@see temp} holder to prepare to combine the two holders
				ArrayList<ArrayList<RowExt>> temp = findRelatedRowsForRows();
				// Check which relation is better an take it as the only relation
				relationsHolder = compareTwoRelations(relationsHolder, temp);
			}
		}
		/**
		 * The holder will hold all relatives of a row with missing value
		 * 		But probably there is NO rows related to a row with missing value
		 * 		Therefore, we cannot calculate the possible value of the missing value
		 * We user the average {@see avg} to set a minimum number of occurences to accept a row's relations
		 * 		By accepting a row's relations, we will replace missing values with a generated calculated value
		 * 		and by rejecting a row's relations, means we will keep the value missing and
		 * 			generate a CSV file to be given to a crowd source people who will find the missing value
		 **/
		int avg = 0;
		for (ArrayList<RowExt> list : relationsHolder) {
			// first sum all occurrences for all relations
			avg += list.size();
		}
		// get the average by dividing on the number of rows (remember each index of holder contains relations for a single row)
		avg = avg / relationsHolder.size();
		/**
		 * Real average causes eliminating acceptable number of relations
		 * 		therefore, we will accept all rows that has at least X number of relations
		 * 		where X is half of the REAL AVERAGE 
		 **/
		int min = (int) (avg * 0.5);
		// generates a data set to be sent to the crowd source, due the lack of information required to calculate the missing value
		generateCroudSourceDataSet(relationsHolder, min);
		// for all accepted rows calculate the missing value and generate a new data set
		generateCalculatedValuesDataSet(attrs, relationsHolder, min);
	}
	
	private ArrayList<ArrayList<RowExt>> compareTwoRelations(ArrayList<ArrayList<RowExt>> r1, ArrayList<ArrayList<RowExt>> r2) {
		int overallR1 = 0, overallR2 = 0;
		for (int i = 0; i < r1.size(); i++) {
			overallR1 += r1.get(i).size();
			overallR2 += r2.get(i).size();
		}
		overallR1 /= r1.size();
		overallR2 /= r2.size();
		if (overallR2 > overallR1)
			return r2;
		else
			return r1;
	}

	private void generateCalculatedValuesDataSet(String[] attrs, ArrayList<ArrayList<RowExt>> holder, int min) {
		CalculateMissingValue cmv = new CalculateMissingValue();
		// the list of rows with the calculated missing values
		ArrayList<RowExt> rows = new ArrayList<>();
		for (int index = 0; index < holder.size(); index++) {
			// ignore all rows that been sent to the crowd source from being further processed
			if (holder.get(index).size() < min)
				continue;
			// get the row with missing value
			RowExt missing = new RowExt(incomplete.getRows().get(index));
			// pass it with its relatives to calculate the missing value
			rows.add(cmv.completeRow(missing, holder.get(index)));
		}
		// create a new data set with the calculated missing values
		calculatedValues = new DataSet(incomplete.getAttributes(), rows);
	}

	private void generateCroudSourceDataSet(ArrayList<ArrayList<RowExt>> holder, int min) {
		// the list of rows with the missing values, which could not be calculated
		ArrayList<RowExt> rows = new ArrayList<>();
		for (int index = 0; index < holder.size(); index++) {
			// because it is lower than the minimum required number of relations 
			if (holder.get(index).size() < min) {
				// since this row has less than the minimum required relations take pass it to the crowd source
				RowExt row = new RowExt(incomplete.getRows().get(index));
				rows.add(row);
			}
		}
		// generate the crowd source data set
		crowdSource = new DataSet(incomplete.getAttributes(), rows);
	}

	/**
	 * Scan missing rows (row by row) and find all relations for each row
	 **/
	public ArrayList<ArrayList<RowExt>> findRelatedRowsForRows() {
		ArrayList<ArrayList<RowExt>> rows = new ArrayList<>();
		for (RowExt row : incomplete.getRows()) {
			rows.add(findRelatedRowsForSingleRow(row));
		}
		return rows;
	}
	
	/**
	 * Scan all complete rows (row by row) and find all relations for a specific row
	 **/
	public ArrayList<RowExt> findRelatedRowsForSingleRow(RowExt rowM) {
		ArrayList<RowExt> relative = new ArrayList<>();
		for (RowExt rowC : complete.getRows()) {
			if (hasRelation(rowM, rowC))
				relative.add(rowC);
		}
		return relative;
	}
	
	/**
	 * Check if two rows has a relation if their values are relative within selected attributes {@see comparativeAttributes}
	 **/
	public boolean hasRelation(RowExt row1, RowExt row2) {
		for (String attributeName : comparativeAttributes) {
			// get the value from the first row at specific attribute
			double value1 = row1.getValue(attributeName);
			// get the value from the second row at the same specific attribute
			double value2 = row2.getValue(attributeName);
			// compare the two values, whether it is close or not
			if (isRelativeValues(value1, value2) == false)
				return false;
		}
		// since we arrived then all values within the selected attributes are close to each other
		// therefore, the two rows has a relation between each other
		return true;
	}
	
	/**
	 * Checks if two double values are close to each other with some margin of error defined as {@see estimatedCloser}
	 **/
	private boolean isRelativeValues(double val1, double val2) {
		// reflects the margin of error accepted between two values to consider it close to each other
		double estimatedCloser = 0.99;
		// if any of the values are ZERO then we cannot compare so just return true
		if (val1 == 0 || val2 == 0) return true;
		// check if first value is greater than the second value
		if (val1 > val2)
			// if greater then divide the smaller on the greater and compare with the margin of error 
			return (val2 / val1) >= estimatedCloser;
		// check if first value is smaller than the second value
		else if (val1 < val2)
			// if smaller then divide the smaller on the greater and compare with the margin of error
			return (val1 / val2) >= estimatedCloser;
		// arriving to this point means the two values are identical or equal
		else return true;
	}

}
