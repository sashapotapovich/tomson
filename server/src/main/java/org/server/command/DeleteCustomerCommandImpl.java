package org.server.command;

import com.common.command.DeleteCustomerCommand;
import com.common.model.Customer;
import com.common.model.CustomerTO;
import java.rmi.RemoteException;
import lombok.extern.slf4j.Slf4j;
import org.server.repository.CustomerRepositoryImpl;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class DeleteCustomerCommandImpl implements DeleteCustomerCommand {
    private static final long serialVersionUID = 575894297941566196L;

    @Autowired
    private CustomerRepositoryImpl customerRepository;

    public DeleteCustomerCommandImpl() throws RemoteException {
    }

    public CustomerTO execute(CustomerTO obj) throws RemoteException {
        Customer deleted = customerRepository.findById(obj.getId());
        customerRepository.delete(deleted);
        return deleted;
    }
}
