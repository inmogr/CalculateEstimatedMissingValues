package org.inmogr.java.jar.data.missing.calculator.classes;

import java.util.ArrayList;

public class DataSet {

	/** Contains all headers of the data set */
	protected String[] attributes;
	/** Contains all values of the data set in form of rows */
	protected ArrayList<RowExt> rows = new ArrayList<>();

	/**
	 * To Construct a DataSet the following is required
	 * 1) All attributes of a data set
	 * 2) All values of a data set in form of rows
	 **/
	public DataSet(String[] attributes, ArrayList<RowExt> listOfRows) {
		this.attributes = attributes.clone();
		this.rows.addAll(listOfRows);
	}

	/**
	 * Copies a DataSet object into another
	 **/
	public DataSet(DataSet dataSet) {
		this(dataSet.attributes, dataSet.rows);
	}

	/** Gets all current attributes */
	public String[] getAttributes() {
		return attributes;
	}

	/** Gets all current rows */
	public ArrayList<RowExt> getRows() {
		return rows;
	}

	/** Gets either the all rows with missing values as a new data set or all rows with NO missing values as a new data set */
	public DataSet gDataSet(boolean complete) {
		DataSet dataSet = new DataSet(attributes, rows);
		for (int i = dataSet.rows.size() - 1; i > -1; i--) {
			boolean removeBczIncomplete = complete && !dataSet.rows.get(i).isComplete(attributes);
			boolean removeBczComplete = !complete && dataSet.rows.get(i).isComplete(attributes);
			if (removeBczIncomplete || removeBczComplete) {
				dataSet.rows.remove(i);
			}
		}
		return dataSet;
	}

}
