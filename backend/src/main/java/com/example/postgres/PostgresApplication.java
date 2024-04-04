package com.example.postgres;

import com.example.postgres.classes.User;
import com.example.postgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PostgresApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(PostgresApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner run () {
//		return args -> {
//			System.out.println("Hello from PostgresApplication");
//			User test = User.builder()
//					.name("Anshul")
//					.username("anshul003")
//					.password("password")
//					.email("anshul2003@gmail.com")
//					.build();
//
//			userRepository.save(test);
//
//			userRepository.findAll().forEach(System.out::println);
//		};
//	}
}
