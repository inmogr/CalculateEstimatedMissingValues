package org.inmogr.java.jar.data.missing.calculator.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.inmogr.java.jar.data.missing.calculator.classes.DataSet;
import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;

public class CsvReader {
	
	/** Contains the full data set of a CSV file associated with the path given */
	private DataSet dataSet;
	
	public void readCsv(String path) {
		/** Contains all headers of the data set */
		String[] attributes = null;
		/** Contains all rows values of the data set */
		ArrayList<RowExt> rows = new ArrayList<>();
		BufferedReader br = null;
		//
		//
		String line = "";
		try {
			int counter = 0;
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				if (attributes == null) {
					attributes = line.split(",");
				}
				else {
					rows.add(new RowExt(attributes, line.split(","), counter));
					counter++;
				}
			}
			dataSet = new DataSet(attributes, rows);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}

}
