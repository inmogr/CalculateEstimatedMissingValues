package org.inmogr.java.jar.data.missing.calculator.classes;

public class Frequency extends RowExt {
	
	protected int occurrence;
	
	public Frequency(Row row) {
		super(row);
		occurrence = 1;
	}
	
	public void increment() {
		occurrence++;
	}
	
	public int getOccurrence() {
		return occurrence;
	}

}