package org.server.command;

import com.common.command.FindAllCustomersCommand;
import com.common.model.Customer;
import com.common.model.CustomerTO;
import java.rmi.RemoteException;
import java.util.List;
import org.server.repository.CustomerRepositoryImpl;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class FindAllCustomersCommandImpl implements FindAllCustomersCommand {
    private static final long serialVersionUID = -3870053718260209448L;

    @Autowired
    private CustomerRepositoryImpl customerRepository;

    public FindAllCustomersCommandImpl() throws RemoteException {
    }

    @Override
    public List<Customer> execute() throws RemoteException {
        return customerRepository.findAll();
    }

    @Override
    public CustomerTO execute(CustomerTO obj) throws RemoteException {
        return null;
    }
}
