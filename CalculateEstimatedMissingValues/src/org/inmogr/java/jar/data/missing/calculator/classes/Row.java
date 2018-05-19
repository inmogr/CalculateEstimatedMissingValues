package org.inmogr.java.jar.data.missing.calculator.classes;

import com.google.gson.JsonObject;

public class Row {
	
	/** an object which contains all data in a row associated with an attribute */
	protected JsonObject json;
	/** refers to the actual location in the original data set */
	protected int location = -1;
	
	/**
	 * To Construct a Row the following are required
	 * 1) all attributes (headers) of the data set
	 * 2) a single value for each attribute (header)
	 * 3) the actual location address of the row in the original data set
	 **/
	public Row(String[] attributes, String[] values, int location) {
		json = new JsonObject();
		for (int i = 0; i < values.length; i++) {
			json.addProperty(attributes[i], values[i]);
		}
		this.location = location;
	}
	
	/**
	 * To Construct a Row the following are required
	 * 1) a row in form of JSON
	 * 2) the actual location address of the row in the original data set
	 **/
	public Row(JsonObject jsonRow, int location) {
		json = jsonRow.deepCopy();
		this.location = location;
	}
	
	/**
	 * To Construct a Row the following are required
	 * 1) a row object to be used to generate a copy of it
	 **/
	public Row(Row row) {
		this(row.json, row.location);
	}
	
	/** Gets the value of a specific attribute */
	public Double getValue(String attributeName) {
		if (json.has(attributeName))
			return json.get(attributeName).getAsDouble();
		return -1.0;
		// return new Double(-1);
	}
	
	/** Gets the whole row in form of JSON */
	public JsonObject getJson() {
		return json.deepCopy();
	}
	
	public int getLocation() {
		return location;
	}
	
	/** Returns a string representation for Human UI */
	public String toString() {
		return json.toString();
	}
	
	/** returns a full new object copy of this object */
	public Row deepCopy() {
		return new Row(json, location);
	}
	
	/** updates specific attribute value (this is needed when we calculate the missing value) */
	public void updateValue(String attribute, double newValue) {
		json.addProperty(attribute, ""+newValue);
	}

}
