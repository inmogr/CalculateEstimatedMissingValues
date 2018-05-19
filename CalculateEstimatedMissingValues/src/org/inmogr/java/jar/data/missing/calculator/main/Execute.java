package org.inmogr.java.jar.data.missing.calculator.main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import org.inmogr.java.jar.data.missing.calculator.algorithms.ReplacementAlgorithim;
import org.inmogr.java.jar.data.missing.calculator.classes.DataSet;
import org.inmogr.java.jar.data.missing.calculator.classes.DataSetSkylined;
import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;
import org.inmogr.java.jar.data.missing.calculator.utils.CsvReader;
import org.inmogr.java.jar.data.missing.calculator.utils.CsvWriter;

public class Execute {
	
	protected DataSet original, source, complete, incomplete;
	protected DataSetSkylined skylined;
	protected ReplacementAlgorithim algorithim;
	
	public Execute(String pathOriginal, String pathWithMissing) {
		// read CSV file and fetch all values and attributes
		initializeDataSetOriginal(pathOriginal);
		// read CSV file and fetch all values and attributes
		initializeDataSet(pathWithMissing);
		// get sub set which contains all complete rows
		complete = source.gDataSet(true);
		// get sub set which contains all incomplete rows
		incomplete = source.gDataSet(false);
	}
	
	public void run(String[] array) {
		algorithim = new ReplacementAlgorithim(complete, incomplete);
		/**
		 * goes through the DataSets and learn from its content to generate two DataSets
		 * The First: is the sub set which computer cannot provide calculated values (called 'crowdSource')
		 * The Second: is the sub set which computer could find relation and compute the missing values (called 'calculatedValues')
		 **/
		algorithim.findMissingValues(array);
		//exportResults();
	}
	
	public void runWithSkyline(String[] array) {
		// establish a copy of the complete data set to perform skyline later on
		skylined = new DataSetSkylined(complete);
		// get best of the complete data set by performing skyline algorithm
		skylined.skyline();
		// initiate replacement algorithm class by providing the DataSet with complete values and the DataSet with missing values
		algorithim = new ReplacementAlgorithim(skylined, incomplete);
		/**
		 * goes through the DataSets and learn from its content to generate two DataSets
		 * The First: is the sub set which computer cannot provide calculated values (called 'crowdSource')
		 * The Second: is the sub set which computer could find relation and compute the missing values (called 'calculatedValues')
		 **/
		algorithim.findMissingValues(array);
		//exportResults();
		//exportResultsOfSkyline();
	}
	
	protected void exportResults() {
		// General path of exporting
		String path = System.getProperty("user.home") + "/Desktop" + "/cmv_results/car_" + (new Date().getTime() % 100000);
		// custom file name for the completed one
		String completePath = "_complete.csv";
		// get sub set which contains all complete rows
		DataSet completeDataSet = complete;
		// custom file name for the incomplete one
		String incompletePath = "_incomplete.csv";
		// get sub set which contains all incomplete rows
		DataSet incompleteDataSet = incomplete;
		// custom file name for the incomplete sub set which to be sent to crowd source team
		String crowdSourcePath = "_crowd_source.csv";
		// get sub set which contains all incomplete rows to be sent to the crowd source team
		DataSet crowdSourceDataSet = algorithim.getCrowdSource();
		// custom file name for the complete (calculated) sub set
		String calculatedPath = "_calculated.csv";
		// get sub set which contains all complete (calculated) rows
		DataSet calculatedDataSet = algorithim.getCalculatedValues();
		//
		//
		exportDataSetToPath(completeDataSet, path+completePath);
		exportDataSetToPath(incompleteDataSet, path+incompletePath);
		exportDataSetToPath(crowdSourceDataSet, path+crowdSourcePath);
		exportDataSetToPath(calculatedDataSet, path+calculatedPath);
	}
	
	protected void exportResultsOfSkyline() {
		// General path of exporting
		String path = System.getProperty("user.home") + "/Desktop" + "/cmv_results/car_" + (new Date().getTime() % 100000);
		// custom file name for the completed one
		String skylinedPath = "_skylined.csv";
		// get sub set which contains all complete rows
		DataSet skylinedDataSet = skylined;
		exportDataSetToPath(skylinedDataSet, path+skylinedPath);
	}

	protected void exportDataSetToPath(DataSet dataSet, String path) {
		try {
			File file = new File(path);
			file.getParentFile().mkdirs();
	        FileWriter writer = new FileWriter(path);
	        CsvWriter.writeLine(writer, arrayToArrayList(dataSet.getAttributes()));
	        for (RowExt row : dataSet.getRows()) {
	        	CsvWriter.writeLine(writer, row.getExportableRow());
			}
	        writer.flush();
	        writer.close();
		} catch (Exception e) {}
	}

	protected ArrayList<String> arrayToArrayList(String[] array) {
		ArrayList<String> values = new ArrayList<>();
		for (String string : array) {
			values.add(string);
		}
		return values;
	}
	
	public void initializeDataSetOriginal(String path) {
		// pass the path to CSV Reader that will read the file and return DataSet object
		CsvReader reader = new CsvReader();
		reader.readCsv(path);
		original = reader.getDataSet();
	}
	
	public void initializeDataSet(String path) {
		// pass the path to CSV Reader that will read the file and return DataSet object
		CsvReader reader = new CsvReader();
		reader.readCsv(path);
		source = reader.getDataSet();
	}

}