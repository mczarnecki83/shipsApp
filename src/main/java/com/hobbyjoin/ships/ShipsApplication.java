package com.hobbyjoin.ships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootApplication
public class ShipsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ShipsApplication.class);//own bug site
	}

	public static void main(String[] args) {
		SpringApplication.run(ShipsApplication.class, args);
	}

}
