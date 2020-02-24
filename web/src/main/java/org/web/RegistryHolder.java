package org.web;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component(priority = 5)
public class RegistryHolder {
    private Registry registry;
    
    @PostConstruct
    private void init() throws RemoteException {
         registry = LocateRegistry.getRegistry("localhost", 2005);
    }

    public Registry getRegistry() {
        return registry;
    }
}
