package br.com.santiago.ccl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.santiago.ccl.services.DbService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DbService dbService;

	@Bean
	public boolean instantiateDatabase() {
		this.dbService.initDataBase();
		return true;
	}
}
