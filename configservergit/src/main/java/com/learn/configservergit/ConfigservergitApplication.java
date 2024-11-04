package com.learn.configservergit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigservergitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigservergitApplication.class, args);
	}

}

