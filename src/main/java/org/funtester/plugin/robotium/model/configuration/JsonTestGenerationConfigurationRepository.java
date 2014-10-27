package org.funtester.plugin.robotium.model.configuration;

import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.generation.TestGenerationConfigurationRepository;
import org.funtester.plugin.robotium.json.JsonMapper;

/**
 * JSON repository for a {@link TestGenerationConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonTestGenerationConfigurationRepository
		implements TestGenerationConfigurationRepository {

	private final String filePath;
	
	public JsonTestGenerationConfigurationRepository(final String filePath) {
		this.filePath = filePath;
	}
	
	public TestGenerationConfiguration first() throws Exception {
		return JsonMapper.readObject( filePath, TestGenerationConfiguration.class );
	}
	
	public void save(TestGenerationConfiguration obj) throws Exception {
		JsonMapper.writeObject( filePath, obj );
	}	

}
