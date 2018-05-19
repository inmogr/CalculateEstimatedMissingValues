package org.inmogr.java.jar.data.missing.calculator.classes;

import java.util.ArrayList;

import com.google.gson.JsonObject;

public class RowExt extends Row {

	/**
	 * To Construct a Row the following are required
	 * 1) all attributes (headers) of the data set
	 * 2) a single value for each attribute (header)
	 * 3) the actual location address of the row in the original data set
	 **/
	public RowExt(String[] attributes, String[] values, int location) {
		super(attributes, values, location);
	}

	/**
	 * To Construct a Row the following are required
	 * 1) a row in form of JSON
	 * 2) the actual location address of the row in the original data set
	 **/
	public RowExt(JsonObject jsonRow, int location) {
		super(jsonRow, location);
	}

	/**
	 * To Construct a Row the following are required
	 * 1) a row object to be used to generate a copy of it
	 **/
	public RowExt(Row row) {
		super(row);
	}
	
	/** Gets all the values of this row */
	public Double[] getValues() {
		ArrayList<Double> values = new ArrayList<>();
		for (String attribute : json.keySet()) {
			values.add(getValue(attribute).doubleValue());
		}
		return values.toArray(new Double[values.size()]);
	}
	
	/** Checks if a Row contains a missing value */
	public boolean isComplete(String[] attributes) {
		for (String attribute : attributes) {
			//if (getValue(attribute).equals(new Double(0)))
			if (getValue(attribute).equals(0.0))
				return false;
		}
		return true;
	}
	
	/** Gets the indexes which contains the missing values */
	public String[] getAttributesWithMissingValue() {
		ArrayList<String> attributes = new ArrayList<>();
		for (String attribute : json.keySet()) {
			if (getValue(attribute).doubleValue() == 0)
				attributes.add(attribute);
		}
		return attributes.toArray(new String[attributes.size()]);
	}
	
	/** gets a row data in form of array list to prepare it for data export */
	public ArrayList<String> getExportableRow() {
		ArrayList<String> values = new ArrayList<>();
		for (String attribute : json.keySet()) {
			values.add(json.get(attribute).getAsString());
		}
		return values;
	}

}
