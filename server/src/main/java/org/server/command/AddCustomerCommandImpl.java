package org.server.command;

import com.common.command.AddCustomerCommand;
import com.common.model.Customer;
import com.common.model.CustomerTO;
import org.server.annotation.CrearecBeanState;
import org.server.annotation.CrearecStateType;
import org.server.dao.CustomerDao;
import org.server.dao.jdbc.CustomerJdbcDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrearecBeanState(CrearecStateType.STATELESS)
public class AddCustomerCommandImpl extends UnicastRemoteObject implements AddCustomerCommand {

	private static final long serialVersionUID = -5947057176466325544L;

	private CustomerDao customerDao = new CustomerJdbcDao();

	public AddCustomerCommandImpl() throws RemoteException {
	}

	public CustomerTO execute(CustomerTO obj) throws RemoteException {
		customerDao.create(new Customer(obj.getSsn(), obj.getCustomerName(), obj.getAddress()));
		return obj;
	}
}
