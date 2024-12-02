package com.checkhouse.core;

import org.springframework.boot.SpringApplication;

public class TestCoreApplication {

	
	public static void main(String[] args) {
		SpringApplication.from(CoreApplication::main).run(args);
	}

}
