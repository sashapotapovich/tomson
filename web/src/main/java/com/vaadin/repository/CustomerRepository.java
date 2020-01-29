package com.vaadin.repository;

import com.vaadin.entity.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findAllByFirstNameContainsOrFirstNameContains(String firstName, String firstName2);
    
    List<Customer> findAllByFirstNameContains(String firstName);
    
    
}
