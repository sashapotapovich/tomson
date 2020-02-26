package com.common.command;

import com.common.model.Customer;
import com.common.model.CustomerTO;
import java.rmi.RemoteException;
import java.util.List;

public interface BatchCommand extends Command<CustomerTO> {
    List<Customer> execute() throws RemoteException;
}
