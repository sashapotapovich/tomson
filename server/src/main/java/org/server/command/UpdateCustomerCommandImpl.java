package org.server.command;

import com.common.command.UpdateCustomerCommand;
import com.common.model.Customer;
import java.rmi.RemoteException;
import org.server.repository.CustomerRepositoryImpl;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class UpdateCustomerCommandImpl implements UpdateCustomerCommand {
    private static final long serialVersionUID = 1938379621504740423L;

    @Autowired
    private CustomerRepositoryImpl customerRepository;

    public UpdateCustomerCommandImpl() throws RemoteException {
    }

    public Customer execute(Customer obj) throws RemoteException {
        return customerRepository.update(obj);
    }
}
