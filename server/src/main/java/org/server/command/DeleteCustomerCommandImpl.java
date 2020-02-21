package org.server.command;

import com.common.command.DeleteCustomerCommand;
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
public class DeleteCustomerCommandImpl extends UnicastRemoteObject implements DeleteCustomerCommand {
    private static final long serialVersionUID = 575894297941566196L;

    @Autowired
    private CustomerJdbcDao customerDao;

    public DeleteCustomerCommandImpl() throws RemoteException {
    }

    public CustomerTO execute(CustomerTO obj) throws RemoteException {
        Customer deleted = customerDao.findBySsn(obj.getSsn());
        customerDao.delete(deleted.getId());
        return deleted;
    }
}
