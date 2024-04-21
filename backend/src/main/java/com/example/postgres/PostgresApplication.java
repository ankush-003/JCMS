package com.example.postgres;

import com.example.postgres.config.RSAKeyRecord;
import com.example.postgres.service.backend.UserService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
@Theme(value="jcms",variant = Lumo.DARK)
@EnableAsync
@Push
public class PostgresApplication implements AppShellConfigurator {
	@Autowired
	private  UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(PostgresApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner run () {
//		return args -> {
//			System.out.println("Hello from PostgresApplication");
//			for(User user: sampleUsers()) {
//				userService.saveUser(user);
//			}
//		};
//	}
//
// 	private List<User> sampleUsers() {
// 	   	return List.of(
// 				User.builder()
// 						.name("sample1")
// 						.username("sample1_user")
// 						.password("password")
// 						.email("sample1@gmail.com")
// 						.roles("ROLE_ADMIN")
// 						.build(),
// 				User.builder()
// 						.name("sample2")
// 						.username("sample2_user")
// 						.password("password")
// 						.email("sample2@gmail.com")
// 						.roles("ROLE_ADMIN")
// 						.build(),
// 				User.builder()
// 						.name("sample3")
// 						.username("sample3_user")
// 						.password("password")
// 						.email("sample3@gmail.com")
// 						.roles("ROLE_ADMIN")
// 						.build());
// 	}
}
