package com.server.server;


import org.h2.Driver;
import org.test.di.app.ApplicationContext;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class Main {

	public static void main(String[] args) throws AlreadyBoundException, IOException {
		System.out.println("Starting...");
		ApplicationContext applicationContext = new ApplicationContext("com.server");

		System.out.println("666 Running...");
	}
}
