package org.server.repository;

import com.common.model.Customer;
import java.util.List;

public interface CustomerRepository {

    Customer save(Customer c);
    Customer findById(Long id);
    List<Customer> findAll();
    Customer delete(Customer c);
    Customer update(Customer c);
}
