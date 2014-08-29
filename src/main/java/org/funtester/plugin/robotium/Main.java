package org.funtester.plugin.robotium;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.at.AbstractTestSuite;
import org.funtester.plugin.robotium.model.TransformException;
import org.funtester.plugin.robotium.model.Transformer;
import org.funtester.plugin.robotium.model.at.AbstractTestSuiteRepository;
import org.funtester.plugin.robotium.model.at.JsonAbstractTestSuiteRepository;

/**
 * Launches the Robotium plugin for Funtester.
 * 
 * @author Matheus Eller Fagundes
 * 
 */
public class Main {

	private static final String FAT_FILE_DIRECTORY = "C:/Program Files/funtester-0.7c.1/examples/funtester.fat";
	
	//TODO: LER DO ARQUIVO DE CONFIGURAÇÃO DEPOIS:
	private static final String OUTPUT_DIRECTORY = "C:/Program Files/funtester-0.7c.1/examples/output";
	private static final String MAIN_CLASS = "MainActivity";
	private static final String PACKAGE_NAME = "com.appname.tests";
	private static final int TIME_OUT_IN_MS = 5000;

	public static void main( String[] args ) {
		
		//STEP 1:
		//System.out.println("> Step 1: Reading the abstratc tests from: " + FAT_FILE_DIRECTORY + "...");

		AbstractTestSuiteRepository abstractTestSuiteRepository = new JsonAbstractTestSuiteRepository(
				FAT_FILE_DIRECTORY);
		
		AbstractTestSuite testSuite = null;
		
		try {
			testSuite = abstractTestSuiteRepository.first();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println( "> Step 1 done!\n" );
		
		//STEP 2:
		//System.out.println( "> Step 2: Transforming the abstract tests into Robotium test code..." );
		
		Transformer transformer = new Transformer();
		List< String > fileNames = new ArrayList< String >();
		
		try {			
			List< String > files = transformer.transform( testSuite, OUTPUT_DIRECTORY, MAIN_CLASS, PACKAGE_NAME, TIME_OUT_IN_MS );
			
			fileNames.addAll( files );
			
		} catch ( TransformException e ) {
			e.printStackTrace();
			return;
		}
		transformer = null;
		
		//System.out.println("> Step 2 done!");
		
	}

}
