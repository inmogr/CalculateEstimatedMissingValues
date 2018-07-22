package org.inmogr.java.jar.data.missing.calculator.executionNew;

import java.util.Scanner;

import org.inmogr.java.jar.data.missing.calculator.main.Timestamp;

public class ExecuteNewPre {
	
	public ExecuteNewPre() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please provide the path of the original (complete) data set (file format must be CSV):");
		String pathOriginal = scanner.nextLine();
		System.out.println("Please provide the path of the missing (incomplete) data set (file format must be CSV):");
		String pathWithMissing = scanner.nextLine();
		System.out.println("Please provide the path of the rows reference (file format must be CSV):");
		String pathRowsReference = scanner.nextLine();
		System.out.println("Type the column's (attribute's) name of the primary key:");
		String pk = scanner.nextLine();
		System.out.println("Type the column's (attribute's) name of the first related attribute:");
		String p1 = scanner.nextLine();
		System.out.println("Type the column's (attribute's) name of the second related attribute:");
		String p2 = scanner.nextLine();
		System.out.println("Please enter the margin of error you need (eg: 0.75)");
		double marginOfError = 0.75;
		try {
			String temp = scanner.nextLine();
			marginOfError = Double.parseDouble(temp);
			if (marginOfError < 0 || marginOfError > 1) marginOfError = 0.75;
		} catch (Exception e) {}
		// add a time stamp point to calculate time required to complete specific stage or list of stages
		Timestamp.addPoint();
		// prepare an array of attributes to be taken into consideration finding the relations
		String[] consideredAttributes = {pk, p1, p2};
		// run the algorithm
		ExecuteNewPost execute = new ExecuteNewPost(pathOriginal, pathWithMissing, pathRowsReference, marginOfError);
		execute.run(consideredAttributes);
		execute.checkRelativeError();
		execute.exportResults();
		double totalError = 0;
		for (double error : execute.totalError) {
			totalError += error;
		}
		double avgError = totalError / execute.totalError.size();
		System.out.println("The average relative error is " + avgError);
		// add a time stamp point to calculate time required to complete specific stage or list of stages
		Timestamp.addPoint();
		// show time required to complete the algorithm
		Timestamp.printTimeToExecuteAll();
		System.out.println("Press enter to exit");
		scanner.nextLine();
		scanner.close();
	}
	
}
