package com.common.model;

public interface CustomerTO extends TransferObject {

	String getSsn();

	void setSsn(String ssn);

	String getCustomerName();

	void setCustomerName(String name);

	String getAddress();

	void setAddress(String address);
}
