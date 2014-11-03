package org.funtester.plugin.robotium.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestCase;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestMethod;
import org.funtester.common.at.AbstractTestOracleStep;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.common.at.AbstractTestSuite;
import org.funtester.plugin.code.HelperMethod;
import org.funtester.plugin.code.TestAnnotation;
import org.funtester.plugin.code.TestCase;
import org.funtester.plugin.code.TestMethod;
import org.funtester.plugin.code.Variable;

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

	public void generate( AbstractTestSuite suite, String mainClass, String sourcePackage, int timeOutToBeVisible, String outputDirectory ){

		Translator translator = new Translator( sourcePackage );
		TestAnnotation annotation = createAnnotation();
		List< AbstractTestCase > testCases = suite.getTestCases();
		
		for( AbstractTestCase abstractTestCase : testCases ){
			try {
				writeTest( createTestCase( abstractTestCase, translator, timeOutToBeVisible, mainClass, sourcePackage ), annotation, outputDirectory );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		
	}
	
	private TestAnnotation createAnnotation(){
		TestAnnotation annotation = new TestAnnotation();
		
		annotation.withTestCase( "" );
		annotation.withTestMethod( "" );
		
		annotation.withSetUpOnce( "" );
		annotation.withSetUp( "@Override" );
		annotation.withTearDown( "@Override" );
		
		return annotation;
	}
	
	/**
	 * Creates a {@link TestCase} based on a {@link AbstractTestCase}.
	 * @param abstractTestCase
	 */
	private TestCase createTestCase( AbstractTestCase abstractTestCase, Translator translator, int timeout, String mainClass, String packageName ){
		/*
		 * mainClass = com.example.app.Main
		 * mainClassPackage = com.example.app
		 * mainClassWithoutPackage = Main
		 */
		String mainClassPackage = mainClass.substring( 0, mainClass.lastIndexOf( "." ) );
		String mainClassWithoutPackage = mainClass.substring( mainClass.lastIndexOf( "." ) + 1 );
		
		TestCase testCase = new TestCase();
		
		testCase.addHeaderComment( "Code generated automatically by Robotium plugin for Funtester." );
		testCase.addHeaderComment( "More info at http://funtester.org/" );
		
		testCase.withNamespace( packageName );

		testCase.addImport( "android.test.ActivityInstrumentationTestCase2" );
		testCase.addImport( "android.widget.*" );
		testCase.addImport( "com.robotium.solo.Solo" );
		testCase.addImport( "com.robotium.solo.Timeout" );
		testCase.addImport( mainClass );
		testCase.addImport( mainClassPackage + ".R" );
		
		testCase.withName( abstractTestCase.getName() + " extends ActivityInstrumentationTestCase2< " + mainClassWithoutPackage + " >" );
		testCase.addAttribute( new Variable( "Solo", "solo" ) );
		
		//Constructor:
		HelperMethod constructor = new HelperMethod();
		constructor.withName( abstractTestCase.getName() );
		constructor.withReturnType( "" );
		constructor.addCommand( "super( " + mainClassWithoutPackage + ".class );" );
		testCase.addHelperMethod( constructor );
		
		//Setup commands:
		testCase.addSetUpCommand( "Timeout.setLargeTimeout( " + timeout + " );" );
		testCase.addSetUpCommand( "solo = new Solo( getInstrumentation(), getActivity() );" );
		
		//TearDown commands:
		testCase.addTearDownCommand( "solo.finishOpenedActivities();" );		
		
		//Adding the test methods:
		for( AbstractTestMethod abstractTestMethod : abstractTestCase.getTestMethods() ){
			TestMethod testMethod = new TestMethod();
			
			testMethod.withName( "test_" + abstractTestMethod.getName() );
			
			for( AbstractTestStep abstractTestStep : abstractTestMethod.getSteps() ){
				if( abstractTestStep instanceof AbstractTestActionStep ){
					AbstractTestActionStep actionStep = ( AbstractTestActionStep ) abstractTestStep;
					for( AbstractTestElement abstractElement : actionStep.getElements() ){
						testMethod.addCommand( translator.translateActionStep( actionStep, abstractElement ) + "//id=" + abstractTestStep.getId() + "|" + abstractTestStep.getStepId() );
					}
				}else{
					AbstractTestOracleStep oracleStep = ( AbstractTestOracleStep ) abstractTestStep;
					testMethod.addCommand( translator.translateOracleStep( oracleStep ) );
				}
					
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
	private void writeTest( TestCase testCase, TestAnnotation annotation, String outputDirectory ) throws IOException{	
		//Configuration...
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading( getClass(), "/template" );
		cfg.setIncompatibleImprovements( new Version( 2, 3, 20 ) );
		cfg.setDefaultEncoding( "UTF-8" );
		cfg.setLocale( Locale.US );
		cfg.setTemplateExceptionHandler( TemplateExceptionHandler.HTML_DEBUG_HANDLER );

		Map< String, Object > input = new HashMap< String, Object >();
		input.put( "test", testCase );
		input.put( "annotation", annotation );
		
		Template template;
		try {
			template = cfg.getTemplate( "java.ftl" );
			String testCaseFileName = testCase.getName().split( " " )[0] + ".java";
			File outputFile = new File( outputDirectory + "/" + testCaseFileName );
			FileWriter fileWriter = new FileWriter( outputFile );
			BufferedWriter bufferedWriter = new BufferedWriter( fileWriter );
			template.process( input, bufferedWriter );
		}catch( IOException ex ){
			ex.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
