package org.server.dao.jdbc;


import com.common.model.Customer;
import java.util.List;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.server.dao.CustomerDao;
import org.server.dao.helper.JdbcDaoSupport;
import org.server.repository.ConnectionProvider;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class CustomerJdbcDao extends JdbcDaoSupport implements CustomerDao {
    
	@Autowired
    private ConnectionProvider connectionProvider;

	@Override
	public List<Customer> findAll() {
        Session session = connectionProvider.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Customer> query = session.createQuery("select c from Customer c", Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findBySsn(String ssn) {
        Session session = connectionProvider.getSession();
        Transaction transaction = session.beginTransaction();
        TypedQuery<Customer> query = session.createQuery("Select c from Customer c where c.ssn = :ssn",
                                                         Customer.class);
        query.setParameter("ssn", ssn);
        return query.getSingleResult();
    }

    @Override
	public void create(final Customer obj) {
        Session session = connectionProvider.getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(obj);
        transaction.commit();
        session.close();
	}

	@Override
	public void update(final Customer obj) {
        Session session = connectionProvider.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(obj);
        session.flush();
        transaction.commit();
        session.close();
    }

	@Override
	public void delete(final Long id) {
        Session session = connectionProvider.getSession();
        Transaction transaction = session.beginTransaction();
        TypedQuery<Customer> query = session.createQuery("delete from Customer c where c.id = :id",
                                                         Customer.class);
        query.setParameter("id", id);
        query.executeUpdate();
	}
}
