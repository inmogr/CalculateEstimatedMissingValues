package org.inmogr.java.jar.data.missing.calculator.main;

import java.util.Scanner;

import org.inmogr.java.jar.data.missing.calculator.executionNew.ExecuteNewPre;
import org.inmogr.java.jar.data.missing.calculator.executionOld.ExecutePre;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Would you like to run the most recent version? (y/N)");
		String algo = scanner.nextLine();
		if (algo != null) {
			boolean theNew = algo.toLowerCase().equals("y") || algo.toLowerCase().equals("yes");
			if (theNew) {
				new ExecuteNewPre();
			}
			else {
				new ExecutePre();
			}
		}
		scanner.close();
	}
	
}
