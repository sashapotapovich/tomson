package org.server.command;

import com.common.command.AddCustomerCommand;
import com.common.model.Customer;
import com.common.model.CustomerTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lombok.extern.slf4j.Slf4j;
import org.server.dao.jdbc.CustomerJdbcDao;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class AddCustomerCommandImpl extends UnicastRemoteObject implements AddCustomerCommand {

    private static final long serialVersionUID = -5947057176466325544L;

    @Autowired
    private CustomerJdbcDao customerDao;

    public AddCustomerCommandImpl() throws RemoteException {
    }

    public CustomerTO execute(CustomerTO obj) throws RemoteException {
        customerDao.create(new Customer(obj.getSsn(), obj.getCustomerName(), obj.getAddress()));
        return obj;
    }
}
