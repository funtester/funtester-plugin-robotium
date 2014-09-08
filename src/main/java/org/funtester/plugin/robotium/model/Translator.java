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
	private static final String RESOURCE_ID = "R.id.";
	private String packageName;
	
	public Translator( String packageName ) {
		this.packageName = packageName;
	}
	
	public  String translateActionStep( AbstractTestActionStep step, AbstractTestElement element ){
		//click
		if( step.getActionName().equals( "click" ) ){
			//click + button
			if( element.getType().equals( "button" ) ){
				return PREFIX + "clickOnButton" + insideParenthesis( insideQuotes( element.getName() ) );	
			}
			//click + menu
			if( element.getType().equals( "menu" ) ){
				return PREFIX + "clickOnMenuItem" + insideParenthesis( insideQuotes( element.getName() ) );
			}
		}
		
		//type
		if( step.getActionName().equals( "type" ) ){
			//type + textbox
			if( element.getType().equals( "textbox" ) ){
				return PREFIX + "enterText" + insideParenthesis( PREFIX + "getEditText" + insideParenthesis( insideQuotes( element.getName() ) ) + ", " + element.getValueOption() );
			}
		}
		
		//select
		if( step.getActionName().equals( "select" ) ){
			//select + combobox || select + spinner
			if( element.getType().equals( "combobox" ) || element.getType().equals( "spinner" ) ){
				return PREFIX + "pressSpinnerItem" + insideParenthesis( packageName + "." + RESOURCE_ID + element.getInternalName() + ", " + element.getValueOption() );
			}
		}
		
		return step.getActionName() + " " + element.getType() + " " + element.getName() + " " + element.getValueOption();
	}
	
	public String translateOracleStep( AbstractTestOracleStep step ){
		//check
		if( step.getActionName().equals( "check" ) ){
			return PREFIX + "waitForText" + insideParenthesis( insideQuotes( step.getMessages().get( 0 ) ) );
		}
		
		return step.getActionName();
	}

	private static String insideParenthesis( String value ){
		return "( " + value + " )";
	}
	
	private static String insideQuotes( String value ){
		return "\"" + value + "\"";
	}
	
}
