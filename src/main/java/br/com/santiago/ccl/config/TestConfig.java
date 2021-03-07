package br.com.santiago.ccl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.santiago.ccl.services.DbService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DbService dbService;

	@Bean
	public boolean instantiateDatabase() {
		log.debug("[{}] [instantiateDatabase] [Info] - Starting database with test profile.",
				this.getClass().getSimpleName());
		this.dbService.initDataBase();

		return true;
	}
}
