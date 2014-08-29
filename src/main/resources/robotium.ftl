// Generated by Funtester with Robotium plugin.
package org.funtester.targetapp.test;

import org.funtester.targetapp.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class ${test.name} extends ActivityInstrumentationTestCase2< MainActivity > {

	private Solo solo;
	
	public ${test.name}() {
	    super( MainActivity.class );
	}
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo( getInstrumentation(), getActivity() );
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	//Test methods:
	<#list test.methods as method>
	public void test_${method.name}() {
		<#list method.commands as cmd>
		${cmd};
		</#list>
	}
	
	</#list>
}