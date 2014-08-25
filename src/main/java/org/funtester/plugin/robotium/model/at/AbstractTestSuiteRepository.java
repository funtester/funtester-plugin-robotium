package org.funtester.plugin.robotium.model.at;

import org.funtester.common.at.AbstractTestSuite;


/**
 * Repository for a {@code AbstractTestSuite}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface AbstractTestSuiteRepository {

	/**
	 * Returns the first {@link AbstractTestSuite}.
	 * @return
	 * @throws Exception
	 */
	AbstractTestSuite first() throws Exception;
	
}
