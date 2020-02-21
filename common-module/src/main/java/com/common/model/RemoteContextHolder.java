package com.common.model;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.ConnectionFactory;

public interface RemoteContextHolder extends Remote, Serializable {
    ConnectionFactory getConnection() throws RemoteException;
}
