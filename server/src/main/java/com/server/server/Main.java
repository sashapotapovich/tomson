package com.server.server;


import com.common.model.Customer;
import com.server.command.AddCustomerCommandImpl;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import org.test.di.app.ApplicationContext;

public class Main {

	public static void main(String[] args) throws AlreadyBoundException, IOException {
		System.out.println("Starting...");
		System.setProperty("java.rmi.server.codebase", "http://localhost:2005");
        System.out.println(System.getProperty("java.security.policy"));
        ApplicationContext applicationContext = new ApplicationContext("com.server");
        Initialize initialize = (Initialize) applicationContext.getBeanFactory().getBean("initialize");
        initialize.run();
        AddCustomerCommandImpl addCustomerCommand = new AddCustomerCommandImpl();
        addCustomerCommand.execute(new Customer("123", "ASD", "ZXC"));
        System.out.println("666 Running...");
	}
}
