package org.funtester.plugin.robotium.model;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.funtester.common.at.AbstractTestSuite;
import org.funtester.common.util.FilePathUtil;
import org.funtester.common.util.TextFileUtil;
import org.funtester.plugin.report.testng.TestNGXmlGenerator;

public class Transformer {
	
	private static final String JAVA_FILE_EXTENSION = ".java";

	public List< String > transform(
			final AbstractTestSuite suite,
			final String baseOutputDirectory,
			final String mainClass,
			final String packageName,
			final int timeoutToBeVisibleInMS
			) throws TransformException {		
		
		//
		// Generates folders according to the package structure
		//
		String sourceCodeDir = baseOutputDirectory;
		if ( generateDirStructure( baseOutputDirectory, packageName ) ) {
			sourceCodeDir = dirFromPackageName( baseOutputDirectory, packageName );
		}
		

		List< String > fileNames = null;
		try {
			fileNames = generateCode( suite, sourceCodeDir, mainClass,
					packageName, timeoutToBeVisibleInMS );

			//TODO: Parece que não preciso de nenhum arquivo de configuração pro Robotium.
			//generateConfiguration( suite, baseOutputDirectory, packageName );
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new TransformException( e );
		}
		
		return fileNames;
	}


	private boolean generateDirStructure(
			final String baseOutputDirectory,
			final String packageName
			) {
		File baseDir = new File( baseOutputDirectory );
		if ( ! baseDir.isDirectory() ) {
			throw new RuntimeException( baseOutputDirectory + " should be a directory." );
		}
		if ( ! baseDir.exists() ) {
			throw new RuntimeException( "The baseOutputDirectory should exists: " + baseOutputDirectory );
		}		
		String packagePath = dirFromPackageName( baseOutputDirectory, packageName );
		File packageDir = new File( packagePath );		
		if ( packageDir.exists() ) {
			return true; // Nothing to be done
		}
		return packageDir.mkdirs();	
	}
	
	private String dirFromPackageName(
			final String baseDir,
			final String packageName
			) {
		String packageDirs = packageName.replace( ".", File.separator );
		return FilePathUtil.directoryWithSeparator( baseDir ) + packageDirs;
	}
	
	private List< String > generateCode(
			final AbstractTestSuite suite,
			final String outputDirectory,
			final String mainClass,
			final String packageName,
			final int timeoutToBeVisibleInMS
			) throws IOException, CodeGenerationException {		
		if ( null == suite ) {
			throw new IllegalArgumentException( "suite can't be null." );
		}
		
		List< String > fileNames = new ArrayList< String >();
		
		//TODO: Teoricamente é aqui que as coisas começam a ficar diferentes entre os plugins
/*		// TESTNG + FEST SWING 
		FestSwingCodeGenerator codeGen = new FestSwingCodeGenerator();
		Map< String, StringBuilder > codeContent = codeGen.generate(
				suite, mainClass, packageName, timeoutToBeVisibleInMS );
		for ( Entry< String, StringBuilder > e : codeContent.entrySet() ) {
			String className = e.getKey();
			String fileName = FilePathUtil.makeFileName( outputDirectory, className + JAVA_FILE_EXTENSION );
			fileNames.add( fileName );
			TextFileUtil.saveContent( e.getValue().toString(), fileName );
		}*/		
		
		RobotiumCodeGenerator codeGenerator = new RobotiumCodeGenerator();
		codeGenerator.generate( suite, mainClass, packageName, timeoutToBeVisibleInMS );
		
		return fileNames;
	}	
	
	//TODO: Não devo precisar disso pro Robotium:
	/*
	private void generateConfiguration(
			final AbstractTestSuite suite,
			final String outputDirectory,
			final String packageName
			) throws IOException {		
		TestNGXmlGenerator xmlGen = new TestNGXmlGenerator();
		Map< String, StringBuilder > xmlContent = xmlGen.generate( packageName, suite );
		String fileName = FilePathUtil.makeFileName( outputDirectory,
				TestNGXmlGenerator.FILE_NAME );
		TextFileUtil.saveContent(
				xmlContent.get( suite.getName() ).toString(), fileName );
	}*/
}
