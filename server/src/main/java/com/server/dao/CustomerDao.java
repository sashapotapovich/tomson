package com.server.dao;

import com.common.model.Customer;

import java.util.List;

public interface CustomerDao extends CrudDao<Long, Customer> {

	List<Customer> findAll();
	Customer findBySsn(String ssn);
	
}
