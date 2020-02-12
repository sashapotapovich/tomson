package com.server.dao.jdbc;


import com.common.model.Customer;
import com.server.dao.CustomerDao;
import com.server.dao.helper.JdbcDaoSupport;
import com.server.dao.helper.RowMapper;
import com.server.dao.helper.RowMapperImpl;
import com.server.dao.helper.SqlBuilder;
import java.util.List;
import org.test.di.annotations.Component;

@Component
public class CustomerJdbcDao extends JdbcDaoSupport implements CustomerDao {

	private RowMapper<Customer> rowMap = new RowMapperImpl<>(new Customer());
	private SqlBuilder builder = new SqlBuilder();

	@Override
	public List<Customer> findAll() {
		return selectList(builder.getSelectSQL(Customer.class), rowMap, null);
	}

    @Override
    public Customer findBySsn(String ssn) {
	    String SQL = "Select * from public.customer where ssn = " + ssn;
        return read(getId(SQL));
    }

    @Override
	public void create(final Customer obj) {
		create(builder.getInsertSQL(obj), obj);
	}

	@Override
	public Customer read(final Long id) {
		return selectOne("", rowMap, id);
	}

	@Override
	public void update(final Customer obj) {
	}

	@Override
	public void delete(final Long id) {
		delete(builder.getDeleteSQL(Customer.class), id);
	}
}
