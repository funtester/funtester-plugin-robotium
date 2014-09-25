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
	private static final String SEMICOLON = ";";
	
	public Translator( String packageName ) {
		//this.packageName = packageName;
	}
	
	public  String translateActionStep( AbstractTestActionStep step, AbstractTestElement element ){
		String actionName = step.getActionName();
		String elementType = element.getType();
		step.getId();//Id do passo de teste;
		step.getStepId();//Id do passo do caso de uso.
		
		//click
		if( actionName.equals( "click" ) ){
			//click + button
			if( elementType.equals( "button" ) ){
				return PREFIX + "clickOnButton" + insideParenthesis( insideQuotes( element.getName() ) ) + SEMICOLON;	
			}
			//click + menu
			if( elementType.equals( "menu" ) ){
				return PREFIX + "clickOnMenuItem" + insideParenthesis( insideQuotes( element.getName() ) ) + SEMICOLON;
			}
			//click + checkbox
			if( elementType.equals( "checkbox" ) ){
				return PREFIX + "clickOnCheckBox" + insideParenthesis( internalNameToId( element.getInternalName() ) ) + SEMICOLON;
			}
			//click + radio || click + radiobutton
			if( elementType.equals( "radio" ) || elementType.equals( "radiobutton" ) ){
				return PREFIX + "clickOnRadioButton" + insideParenthesis( internalNameToId( element.getInternalName() ) ) + SEMICOLON;
			}
		}
		
		//type
		if( actionName.equals( "type" ) ){
			//type + textbox
			if( elementType.equals( "textbox" ) ){
				String valueToType = "";
				if( element.getValue() != null ){
					valueToType = insideQuotes( element.getValue().toString() );
				}
				return PREFIX + "enterText" + insideParenthesis( PREFIX + "getEditText" + insideParenthesis( insideQuotes( element.getName() ) ) + ", " + valueToType ) + SEMICOLON;
			}
		}
		
		//select
		if( actionName.equals( "select" ) ){
			//select + combobox || select + spinner
			if( elementType.equals( "combobox" ) || elementType.equals( "spinner" ) ){
				return PREFIX + "pressSpinnerItem" + insideParenthesis( internalNameToId( element.getInternalName() ) + ", " + element.getValue() ) + SEMICOLON;
			}
			//select + checkbox
			if( elementType.equals( "checkbox" ) ){
				return PREFIX + "clickOnCheckBox" + insideParenthesis( internalNameToId( element.getInternalName() ) ) + SEMICOLON;
			}
			//select + radio || select + radio button
			if( elementType.equals( "radio" ) || elementType.equals( "radiobutton" ) ){
				return PREFIX + "clickOnRadioButton" + insideParenthesis( internalNameToId( element.getInternalName() ) ) + SEMICOLON;
			}
		}
		
		//choose
		if( actionName.equals( "choose" ) ){
			//choose + checkbox
			if( elementType.equals( "checkbox" ) ){
				return PREFIX + "clickOnCheckBox" + insideParenthesis( internalNameToId( element.getInternalName() ) ) + SEMICOLON;
			}
		}
		
		//show
		if( actionName.equals( "show" ) ){
			//show + frame
			if( elementType.equals( "frame" ) ){
				return PREFIX + "assertCurrentActivity( " + insideQuotes( "A tela " + element.getName() + " não foi exibida." ) + ", " + insideQuotes( element.getName() ) + " )" + SEMICOLON;
			}
			//show + dialog
			if( elementType.equals( "dialog" ) ){
				return assertTrue( PREFIX + "waitForView" + insideParenthesis( internalNameToId( element.getInternalName() ) ) ) + SEMICOLON;
			}
		}
		
		//close
		if( actionName.equals( "close" ) ){
			return "//The \"close\" action will be performed automatically by tearDown() method.";
		}
		
		return actionName + " " + elementType + " " + element.getName() + " " + element.getValue() + SEMICOLON;
	}
	
	public String translateOracleStep( AbstractTestOracleStep step ){
		String actionName = step.getActionName();
		
		//check
		if( actionName.equals( "check" ) ){
			return PREFIX + "waitForText" + insideParenthesis( insideQuotes( step.getMessages().get( 0 ) ) ) + SEMICOLON;
		}
		
		return step.getActionName() + SEMICOLON;
	}

	private static String insideParenthesis( String value ){
		return "( " + value + " )";
	}
	
	private static String insideQuotes( String value ){
		return "\"" + value + "\"";
	}
	
	/**
	 * Allows to get the ID of the element through its internal name.
	 * Useful when it's not possible to get the element through the displayed name
	 * in the user interface. 
	 * @param internalName - The internal name of the element. 
	 * @return - The Robotium code that, in runtime, will return the ID of the element.
	 */
	private String internalNameToId( String internalName ){
		String id = null;
		id = PREFIX + "getCurrentActivity().getResources().getIdentifier( " + insideQuotes( internalName ) + ", " + insideQuotes( "id" ) + ", " + PREFIX + "getCurrentActivity().getPackageName() )";	
		return id;
	}
	
	/**
	 * Wrap the parameter in a assertTrue command of Robotium.
	 * @param condition - The {@link String} to be wrapped.
	 * @return - The parameter wrapped in a assertTrue command.
	 */
	private String assertTrue( String condition ){
		return "assertTrue" + insideParenthesis( condition );
	}
	
	/*TODO: vou usar isso mesmo?
	/**
	 * Transform an abstract type of Funtester in a Robotium class.
	 * Example: If the type is "combobox", the return will be "Spinner". 
	 * @param type - {@link AbstractTestElement}.getType().
	 * @return - The correspondent class in Robotium/Android code.
	 *
	private String classByAbstractType( String type ){
		String elementClass = "";
		if( type.equals( "combobox" ) ){
			elementClass = "Spinner";
		}
		return elementClass;
	}
	*/
}
