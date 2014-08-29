package org.funtester.plugin.robotium.model;

import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestOracleStep;

/**
 * Translates the abstract methods to Robotium methods.
 * @author Matheus Eller Fagundes
 */
public class Translator {

	private static final String PREFIX = "solo."; 
	
	public static String translateActionStep( AbstractTestActionStep step, AbstractTestElement element ){
		//click
		if( step.getActionName().equals( "click" ) ){
			if( element.getType().equals( "button" ) ){
				return PREFIX + "clickOnButton" + insideParenthesis( insideQuotes( element.getName() ) );	
			}
			if( element.getType().equals( "menu" ) ){
				return PREFIX + "clickOnMenuItem" + insideParenthesis( insideQuotes( element.getName() ) );
			}
		}
		
		//type
		if( step.getActionName().equals( "type" ) ){
			if( element.getType().equals( "textbox" ) ){
				return PREFIX + "enterText" + insideParenthesis( PREFIX + "getEditText" + insideParenthesis( insideQuotes( element.getName() ) ) + ", " + element.getValueOption() );
			}
		}
		
		return step.getActionName() + " " + element.getType() + " " + element.getName() + " " + element.getValueOption();
	}
	
	public static String translateOracleStep( AbstractTestOracleStep step ){
		return "";
	}

	private static String insideParenthesis( String value ){
		return "( " + value + " )";
	}
	
	private static String insideQuotes( String value ){
		return "\"" + value + "\"";
	}
	
}
