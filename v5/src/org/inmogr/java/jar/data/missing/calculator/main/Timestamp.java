package org.inmogr.java.jar.data.missing.calculator.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Timestamp {
	
	private static ArrayList<Long> timestamp = new ArrayList<>();
	
	public static void addPoint() {
		timestamp.add(new Date().getTime());
	}
	
	public static String gTimeToExecute(int fromTaskNum, int toTaskNum) {
		String time = "" + (timestamp.get(toTaskNum) - timestamp.get(fromTaskNum));
		return time;
	}
	
	public static void printTimeToExecute(int fromTaskNum, int toTaskNum) {
		System.out.println("Executed within : " + gTimeToExecute(fromTaskNum, toTaskNum) + " ms");
	}
	
	public static void printTimeToExecuteLastStep() {
		int last = timestamp.size() - 1;
		System.out.println("Executed within : " + gTimeToExecute(last-1, last) + " ms");
	}
	
	public static void printTimeToExecuteAll() {
		System.out.println("Executed within : " + gTimeToExecute(0, timestamp.size()-1) + " ms");
	}
	
	public static ArrayList<Long> getTimestamp() {
		return timestamp;
	}
	
	public static List<String> getRow(int index) {
		List<String> row = new ArrayList<>();
		row.add("" + index);
		row.add("" + timestamp.get(index));
		if (index > 0)
			row.add(gTimeToExecute(index-1, index));
		return row;
	}

}
