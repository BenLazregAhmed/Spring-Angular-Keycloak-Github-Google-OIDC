package com.example.customers_app_with_thymleaf.repository;

import com.example.customers_app_with_thymleaf.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
