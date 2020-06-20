package com.rms2307.ecommerce.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rms2307.ecommerce.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatebase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
}
