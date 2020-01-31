package com.server.command;

import com.common.command.AddCustomerCommand;
import com.common.model.Customer;
import com.common.model.CustomerTO;
import com.server.dao.CustomerDao;
import com.server.dao.jdbc.CustomerJdbcDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Remote;

@Slf4j
@Remote
public class GetAllCustomersCommandImpl extends UnicastRemoteObject implements AddCustomerCommand {
    private static final long serialVersionUID = 575894297941566196L;

    private CustomerDao customerDao = new CustomerJdbcDao();

    public GetAllCustomersCommandImpl() throws RemoteException {
    }

    public CustomerTO execute(CustomerTO obj) throws RemoteException {
        
        customerDao.create(new Customer(obj.getSsn(), obj.getCustomerName(), obj.getAddress()));
        return obj;
    }
}
