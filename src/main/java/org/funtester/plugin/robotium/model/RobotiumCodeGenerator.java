package org.funtester.plugin.robotium.model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.funtester.common.at.AbstractTestCase;
import org.funtester.common.at.AbstractTestMethod;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.common.at.AbstractTestSuite;
import org.funtester.plugin.code.TestCase;
import org.funtester.plugin.code.TestMethod;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * Generates Robotium test code.
 * @author Matheus Eller Fagundes
 *
 */
public class RobotiumCodeGenerator {

	public void generate( AbstractTestSuite suite, String mainClass, String sourcePackage, int timeOutToBeVisible ){
		List< AbstractTestCase > testCases = suite.getTestCases();
		//transforma em código de robotium aqui
		
		for( AbstractTestCase abstractTestCase : testCases ){
			try {
				writeTest( createTestCase( abstractTestCase ) );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Creates a {@link TestCase} based on a {@link AbstractTestCase}.
	 * @param abstractTestCase
	 */
	private TestCase createTestCase( AbstractTestCase abstractTestCase ){
		TestCase testCase = new TestCase();
		
		testCase.withName( abstractTestCase.getName() );
		
		//Adding the test methods:
		for( AbstractTestMethod abstractTestMethod : abstractTestCase.getTestMethods() ){
			TestMethod testMethod = new TestMethod();
			
			testMethod.withName( abstractTestMethod.getName() );
			for( AbstractTestStep abstractTestStep : abstractTestMethod.getSteps() ){
				testMethod.addCommand( abstractTestStep.getActionName() );
			}
			
			testCase.addMethod( testMethod );
		}
		return testCase;
	}
	
	/**
	 * Writes a {@link TestCase} into a file.
	 * @param testCase {@link TestCase} to be written.
	 * @throws IOException if a problem occurs to write .
	 */
	private void writeTest( TestCase testCase ) throws IOException{	
		//Configuration...
		Configuration cfg = new Configuration();	
		cfg.setDirectoryForTemplateLoading( new File( "src/main/resources" ) );
		cfg.setIncompatibleImprovements( new Version( 2, 3, 20 ) );
		cfg.setDefaultEncoding( "UTF-8" );
		cfg.setLocale( Locale.US );
		cfg.setTemplateExceptionHandler( TemplateExceptionHandler.HTML_DEBUG_HANDLER );

		Map< String, Object > input = new HashMap< String, Object >();
		input.put( "test", testCase );
		
		Template template;
		try {
			template = cfg.getTemplate( "robotium.ftl" );
			Writer consoleWriter = new OutputStreamWriter( System.out );
			template.process( input, consoleWriter );
		}catch( IOException ex ){
			ex.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
