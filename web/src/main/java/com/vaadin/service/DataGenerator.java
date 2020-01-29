package com.vaadin.service;

import com.vaadin.entity.Customer;
import com.vaadin.repository.CustomerRepository;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Component
public class DataGenerator {

    @Autowired
    private CustomerRepository customerRepository;

    @PostConstruct
    public void commandLineRunner() {
        Customer customer1 = new Customer("Customer1", "Address1");
        Customer customer2 = new Customer("Customer2", "Address2");
        Customer customer3 = new Customer("Customer3", "Address3");
        Customer customer4 = new Customer("Customer4", "Address4");
        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3, customer4));
    }
}
