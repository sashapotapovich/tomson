package com.server.server;


import java.io.IOException;
import java.rmi.AlreadyBoundException;
import org.test.di.app.ApplicationContext;

public class Main {

	public static void main(String[] args) throws AlreadyBoundException, IOException {
		System.out.println("Starting...");
		ApplicationContext applicationContext = new ApplicationContext("com.server");
        Initialize initialize = (Initialize) applicationContext.getBeanFactory().getBean("initialize");
        initialize.run();
        System.out.println("666 Running...");
	}
}
