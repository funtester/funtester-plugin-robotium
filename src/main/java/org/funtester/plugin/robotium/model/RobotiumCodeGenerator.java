package org.funtester.plugin.robotium.model;

import java.util.List;

import org.funtester.common.at.AbstractTestCase;
import org.funtester.common.at.AbstractTestSuite;

/**
 * Generates Robotium test code.
 * @author Matheus Eller Fagundes
 *
 */
public class RobotiumCodeGenerator {

	public void generate( AbstractTestSuite suite, String mainClass, String sourcePackage, int timeOutToBeVisible ){
		List< AbstractTestCase > testCases = suite.getTestCases();
		for( AbstractTestCase testCase: testCases ){
			System.out.println( testCase.getName() );
			System.out.println( testCase.getTestMethods().get(0) );
		}
	}
	
}
