package com.vaadin.repository;

import com.vaadin.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.test.di.annotations.Component;

@Component
public class CustomerRepository{

    public List<Customer> findAllByFirstNameContains(String firstName) {
        return null;
    }
    
    public Optional<Customer> findById(long longValue) {
        return null;
    }

    public List<Customer> findAll() {
        return null;
    }

    public void delete(Customer customer) {
        
    }

    public boolean save(Customer customer) {
        return true;
    }

    public boolean saveAll(List<Customer> customers) {
        return false;
    }
}
