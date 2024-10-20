package com.example.customers_app_with_thymleaf;

import com.example.customers_app_with_thymleaf.entity.Customer;
import com.example.customers_app_with_thymleaf.repository.CustomerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomersAppWithThymleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomersAppWithThymleafApplication.class, args);
	}
	@Bean
	CommandLineRunner start(CustomerRepo customerRepo)
	{
		return args -> {
			customerRepo.save(Customer.builder().name("ahmed").email("ahmed@gmail.com").build());
			customerRepo.save(Customer.builder().name("sabiha").email("sabiha@gmail.com").build());
			customerRepo.save(Customer.builder().name("kamel").email("kamel@gmail.com").build());
		};
	}
}
