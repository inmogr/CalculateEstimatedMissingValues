package org.inmogr.java.jar.data.missing.calculator.classes;

import java.util.ArrayList;
import java.util.List;

import org.inmogr.java.jar.data.missing.calculator.algorithms.Skyline;

public class DataSetSkylined extends DataSet {

	/**
	 * To Construct a DataSet the following is required
	 * 1) All attributes of a data set
	 * 2) All values of a data set in form of rows
	 **/
	public DataSetSkylined(String[] attrs, ArrayList<RowExt> list) {
		super(attrs, list);
	}

	/**
	 * Copies a DataSet object into another
	 **/
	public DataSetSkylined(DataSetSkylined dataSet) {
		this(dataSet.attributes, dataSet.rows);
	}
	
	/**
	 * Copies a DataSet object into another
	 **/
	public DataSetSkylined(DataSet dataSet) {
		this(dataSet.attributes, dataSet.rows);
	}

	/** Gets either the all rows with missing values as a new data set or all rows with NO missing values as a new data set */
	public void skyline() {
		for (int i = rows.size() - 1 ; i > 0 ; i--) {
			List<RowExt> tempRows = rows.subList(0, i - 1);
			if (Skyline.isSmallest(rows.get(i), tempRows))
				rows.remove(i);
		}
	}

}
