package org.inmogr.java.jar.data.missing.calculator.executionOld;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.inmogr.java.jar.data.missing.calculator.classes.DataSet;
import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;

public class ExecutePost extends Execute {
	
	protected double marginOfError;
	protected DataSet finalCalculated, finalCrowdSource;
	protected List<Double> totalError = new ArrayList<>();

	public ExecutePost(String pathOriginal, String pathWithMissing, double marginOfError) {
		super(pathOriginal, pathWithMissing);
		this.marginOfError = marginOfError;
	}
	
	protected void checkRelativeError() {
		ArrayList<RowExt> rowsOri = this.original.getRows();
		ArrayList<RowExt> rowsMis = this.incomplete.getRows();
		ArrayList<RowExt> rowsCal = algorithim.getCalculatedValues().getRows();
		ArrayList<RowExt> rowsCalFinal = new ArrayList<>();
		ArrayList<RowExt> rowsCroAdditional = new ArrayList<>();
		for (int index = 0; index < rowsCal.size(); index++) {
			RowExt rowMis = new RowExt(rowsMis.get(index));
			RowExt rowCal = new RowExt(rowsCal.get(index));
			RowExt rowOri = new RowExt(rowsOri.get(rowCal.getLocation()));
			if (isCalculatedValueWithinMargin(rowMis, rowCal, rowOri)) {
				rowsCalFinal.add(rowCal);
			}
			else {
				rowsCroAdditional.add(rowMis);
			}
		}
		this.finalCalculated = new DataSet(this.original.getAttributes(), rowsCalFinal);
		rowsCroAdditional.addAll(this.algorithim.getCrowdSource().getRows());
		this.finalCrowdSource = new DataSet(this.original.getAttributes(), rowsCroAdditional);
	}
	
	protected void exportResults() {
		super.exportResults();
		exportResultsFinal();
	}

	protected void exportResultsFinal() {
		// General path of exporting
		String path = System.getProperty("user.home") + "/Desktop" + "/cmv_results/car_" + (new Date().getTime() % 100000);
		// custom file name for the completed one
		String finalCalculatedPath = "_final_calculated.csv";
		// get sub set which contains all final calculated rows
		DataSet finalCalculatedDataSet = finalCalculated;
		// custom file name for the completed one
		String finalCrowdSourcePath = "_final_crowd_source.csv";
		// get sub set which contains all final crowd source rows
		DataSet finalCrowdSourceDataSet = finalCrowdSource;
		//
		//
		exportDataSetToPath(finalCalculatedDataSet, path+finalCalculatedPath);
		exportDataSetToPath(finalCrowdSourceDataSet, path+finalCrowdSourcePath);
	}

	protected boolean isCalculatedValueWithinMargin(RowExt rowMis, RowExt rowCal, RowExt rowOri) {
		String[] attributes = rowMis.getAttributesWithMissingValue();
		for (int index = 0; index < attributes.length; index++) {
			double valueCal = rowCal.getValue(attributes[index]);
			double valueOri = rowOri.getValue(attributes[index]);
			//System.out.println(valueCal + " <---> " + valueOri);
			if (isBelowMarginOfError(valueCal, valueOri)) {
				return false;
			}
		}
		return true;
	}

	protected boolean isBelowMarginOfError(double v1, double v2) {
		double diff = Math.abs(v1 - v2);
		this.totalError.add(diff);
		if (v1 > v2)
			return (v2 / v1) < marginOfError;
		return (v1 / v2) < marginOfError;
	}

}
