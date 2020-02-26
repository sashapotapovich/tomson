package org.server.repository;

import com.common.model.Customer;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {
    
    @Autowired
    private ConnectionProvider connectionProvider;
    private Session session;
    
    @PostConstruct
    private void init(){
        session = connectionProvider.getSession();
    }
    
    @Override
    public Customer save(Customer c) {
        Transaction transaction = session.beginTransaction();
        session.save(c);
        transaction.commit();
        return c;
    }

    @Override
    public Customer findById(Long id) {
        return session.find(Customer.class, id);
    }

    @Override
    public List<Customer> findAll() {
        Query<Customer> select = session.createQuery("select c from Customer c", Customer.class);
        return select.getResultList();
    }

    @Override
    public Customer delete(Customer c) {
        Transaction transaction = session.beginTransaction();
        session.delete(c);
        transaction.commit();
        return c;
    }

    @Override
    public Customer update(Customer c) {
        Transaction transaction = session.beginTransaction();
        Customer customer = session.find(Customer.class, c.getId());
        customer.setSsn(c.getSsn());
        customer.setCustomerName(c.getCustomerName());
        customer.setAddress(c.getAddress());
        session.update(customer);
        transaction.commit();
        return customer;
    }
}
