package com.learn.companyms;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CompanymsApplication implements CommandLineRunner {

	@Value("${spring.zipkin.baseUrl}")
	private String zipkin;

	public static void main(String[] args) {
		SpringApplication.run(CompanymsApplication.class, args);


	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(zipkin);
	}
}
