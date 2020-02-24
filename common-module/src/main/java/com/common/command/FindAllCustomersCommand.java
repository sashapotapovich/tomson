package com.common.command;

import com.common.model.Customer;
import java.rmi.RemoteException;
import java.util.List;

public interface FindAllCustomersCommand extends BatchCommand {
    List<Customer> execute() throws RemoteException;
}
