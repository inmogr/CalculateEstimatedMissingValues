package org.inmogr.java.jar.data.missing.calculator.algorithms;

import java.util.ArrayList;

import org.inmogr.java.jar.data.missing.calculator.classes.Frequency;
import org.inmogr.java.jar.data.missing.calculator.classes.RowExt;

public class CalculateMissingValue {
	
	public RowExt completeRow(RowExt missing, ArrayList<RowExt> relatives) {
		RowExt newRow = new RowExt(missing);
		String[] attributesWithMissingValues = missing.getAttributesWithMissingValue();
		for (String attribute : attributesWithMissingValues) {
			double calculatedValue = calculateMissingValueBasedOnRelatives(attribute, relatives);
			newRow.updateValue(attribute, calculatedValue);
		}
		return newRow;
	}

	private double calculateMissingValueBasedOnRelatives(String attribute, ArrayList<RowExt> relatives) {
		ArrayList<Frequency> frequencies = getFrequencies(attribute, relatives);
		int highestOccurrence = getHighestOccurrence(frequencies);
		frequencies = getFrequenciesWithHighestOccurrence(frequencies, highestOccurrence);
		double calculatedValue = 0;
		for (Frequency frequencyDouble : frequencies) {
			calculatedValue += frequencyDouble.getValue(attribute);
		}
		calculatedValue /= frequencies.size();
		return calculatedValue;
	}

	private ArrayList<Frequency> getFrequenciesWithHighestOccurrence(ArrayList<Frequency> frequencies, int highestOccurrence) {
		ArrayList<Frequency> highest = new ArrayList<>();
		for (Frequency frequencyDouble : frequencies) {
			if (frequencyDouble.getOccurrence() == highestOccurrence)
				highest.add(frequencyDouble);
		}
		return highest;
	}

	private int getHighestOccurrence(ArrayList<Frequency> frequencies) {
		int highest = 0;
		for (Frequency frequencyDouble : frequencies) {
			if (highest < frequencyDouble.getOccurrence())
				highest = frequencyDouble.getOccurrence();
		}
		return highest;
	}

	private ArrayList<Frequency> getFrequencies(String attribute, ArrayList<RowExt> relatives) {
		ArrayList<Frequency> frequencies = new ArrayList<>();
		for (RowExt row : relatives) {
			boolean incremented = false;
			double value = row.getValue(attribute);
			for (int i = 0; i < frequencies.size(); i++) {
				if (value == frequencies.get(i).getValue(attribute)) {
					frequencies.get(i).increment();
					incremented = true;
					break;
				}
			}
			if (!incremented) {
				Frequency freq = new Frequency(row);
				frequencies.add(freq);
			}
		}
		return frequencies;
	}

}
