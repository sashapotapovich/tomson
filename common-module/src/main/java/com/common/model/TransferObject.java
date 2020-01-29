package com.common.model;

import java.io.Serializable;
import java.rmi.Remote;

public interface TransferObject extends Remote, Serializable {
	void setErrorMessage(String message);
}
