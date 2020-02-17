package com.common.model;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteContextHolder extends Remote, Serializable {

    JmsConnectionFactory getConnection() throws RemoteException;
}
