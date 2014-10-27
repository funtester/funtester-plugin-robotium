package org.funtester.plugin.robotium;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.funtester.common.at.AbstractTestSuite;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.generation.TestGenerationConfigurationRepository;
import org.funtester.plugin.robotium.model.TransformException;
import org.funtester.plugin.robotium.model.Transformer;
import org.funtester.plugin.robotium.model.at.AbstractTestSuiteRepository;
import org.funtester.plugin.robotium.model.at.JsonAbstractTestSuiteRepository;
import org.funtester.plugin.robotium.model.configuration.JsonTestGenerationConfigurationRepository;

/**
 * Launches the Robotium plugin for Funtester.
 * 
 * @author Matheus Eller Fagundes
 * 
 */
public class Main {

	private static final String FAT_FILE_DIRECTORY = "C:/Program Files/funtester-0.7c.1/userapp/userapp.fat";
	private static final String PCFG_FILE_DIRECTORY = "C:/Program Files/funtester-0.7c.1/userapp/userapp.pcfg";

	public static void main( String[] args ) throws Exception {
		
		//STEP 0:
		//System.out.println("> Step 0: Reading the configurations from: " + FAT_FILE_DIRECTORY + "...");
		File configurationFile = new File( PCFG_FILE_DIRECTORY );
		if ( ! configurationFile.exists() ) {
			//TODO: Ver qual é a melhor maneira de informar isso ao usuário depois...
			throw new Exception( "O arquivo de configuração não foi encontrado no diretório informado." );
		}
		
		TestGenerationConfigurationRepository configurationRepository =
				createConfigurationRepository( PCFG_FILE_DIRECTORY );
			
		TestGenerationConfiguration cfg = null;
		try {
			cfg = configurationRepository.first();
		} catch ( Exception e1 ) {			
			e1.printStackTrace();
		}
		
		//Getting the configurations from the configurations repository
		final String OUTPUT_DIRECTORY = cfg.getOutputDirectory();
		final String MAIN_CLASS = cfg.getMainClass();
		final String PACKAGE_NAME = cfg.getPackageName();
		final int TIME_OUT_IN_MS = cfg.getTimeoutInMS();
			
		//System.out.println("> Step 0 done!");
		
		//STEP 1:
		//System.out.println("> Step 1: Reading the abstract tests from: " + FAT_FILE_DIRECTORY + "...");

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
	
	private static TestGenerationConfigurationRepository createConfigurationRepository(
			final String fileName) {
		return new JsonTestGenerationConfigurationRepository( fileName );
	}

}
