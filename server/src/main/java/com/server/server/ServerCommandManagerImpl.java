package com.server.server;


import com.common.command.Command;
import com.common.command.ServerCommandManager;
import com.common.model.TransferObject;
import com.server.executor.RejectedExecutionHandlerImpl;
import lombok.Setter;
import org.test.di.annotations.WebServlet;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.*;

public class ServerCommandManagerImpl implements ServerCommandManager {

	private static final long serialVersionUID = -379232848482999023L;

	private static final ExecutorService EXECUTOR_SERVICE;

	@Setter
	private Map<Class, Command> commands;

	static  {
		RejectedExecutionHandler rejectionHandler = new RejectedExecutionHandlerImpl();
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		EXECUTOR_SERVICE = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), threadFactory, rejectionHandler);

	}

	@Override
	public <T extends Command, D extends TransferObject> D execute(final Class<T> clazz, D obj) throws RemoteException, ExecutionException, InterruptedException {
		Command command = commands.get(clazz);
		Class<? extends Command> implClass = command.getClass();
		switch (implClass.getAnnotation(WebServlet.class).value()) {
			case "A":
				return (D) command.execute(obj);
			case "": //TODO: Implement
			default:
				try {
					return EXECUTOR_SERVICE.submit(new Worker<D>(implClass.newInstance(), obj)).get();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
		}
	}
}
