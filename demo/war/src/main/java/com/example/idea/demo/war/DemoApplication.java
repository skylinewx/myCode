package com.example.idea.demo.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("serve strating");
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("serve strated");
	}

}
