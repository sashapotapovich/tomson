package com.common.command;

import com.common.model.TransferObject;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Command<T extends TransferObject> extends Remote, Serializable {

    T execute(T obj) throws RemoteException;
}
