package org.server.dao.jdbc;


import com.common.model.Customer;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.server.dao.CustomerDao;
import org.server.dao.helper.JdbcDaoSupport;
import org.server.dao.helper.RowMapper;
import org.server.dao.helper.RowMapperImpl;
import org.server.dao.helper.SqlBuilder;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class CustomerJdbcDao extends JdbcDaoSupport implements CustomerDao {

	private RowMapper<Customer> rowMap = new RowMapperImpl<>(new Customer());
	private SqlBuilder builder = new SqlBuilder();
	private  final StandardServiceRegistry dbRegistry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml").build();

	@Override
	public List<Customer> findAll() {
		return selectList(builder.getSelectSQL(Customer.class), rowMap, null);
	}

    @Override
    public Customer findBySsn(String ssn) {
	    String SQL = "Select * from public.customer where customer.ssn = '" + ssn + "'";
        return selectOne(SQL, rowMap, 1L);
    }

    @Override
	public void create(final Customer obj) {
        SessionFactory sessionFactory = null;

        try {
            sessionFactory = new MetadataSources(dbRegistry).buildMetadata()
                                                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(dbRegistry);
            log.error("cannot create sessionFactory", e);
            System.exit(1);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(obj);
        transaction.commit();
        session.close();
	}

	@Override
	public Customer read(final Long id) {
		return selectOne("", rowMap, id);
	}

	@Override
	public void update(final Customer obj) {
	    log.error("Updating Customer - {}", obj.getCustomerName());
        Customer bySsn = findBySsn(obj.getSsn());
        bySsn.setCustomerName(obj.getCustomerName());
        bySsn.setAddress(obj.getAddress());
        SessionFactory sessionFactory = null;

        try {
            sessionFactory = new MetadataSources(dbRegistry).buildMetadata()
                                                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(dbRegistry);
            log.error("cannot create sessionFactory", e);
            System.exit(1);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(obj);
        transaction.commit();
        session.close();
    }

	@Override
	public void delete(final Long id) {
		delete(builder.getDeleteSQL(Customer.class), id);
	}
}
