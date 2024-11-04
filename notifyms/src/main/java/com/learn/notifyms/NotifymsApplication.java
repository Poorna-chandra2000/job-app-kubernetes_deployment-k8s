package com.learn.notifyms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotifymsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifymsApplication.class, args);
	}

}
