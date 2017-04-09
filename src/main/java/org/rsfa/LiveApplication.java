package org.rsfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class LiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveApplication.class, args);
	}
}
