package org.server.command;

import com.common.command.FindCustomerByIdCommand;
import com.common.model.Customer;
import java.rmi.RemoteException;
import org.server.repository.CustomerRepositoryImpl;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class FindCustomerByIdCommandImpl implements FindCustomerByIdCommand {
    private static final long serialVersionUID = 6600435704160810343L;

    @Autowired
    private CustomerRepositoryImpl customerRepository;

    public FindCustomerByIdCommandImpl() throws RemoteException {
    }

    public Customer execute(Customer obj) throws RemoteException {
        return customerRepository.findById(obj.getId());
    }
}
