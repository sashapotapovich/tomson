package org.server.server;


import com.common.command.BatchCommand;
import com.common.command.Command;
import com.common.command.ServerCommandManager;
import com.common.model.TransferObject;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.server.executor.RejectedExecutionHandlerImpl;

@Slf4j
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
    @SuppressWarnings("unchecked")
	public <T extends Command, D extends TransferObject> D execute(final Class<T> clazz, D obj) throws RemoteException, ExecutionException, InterruptedException {
	    log.info("Received new Command - {}", clazz.getName());
		Command command = commands.get(clazz);
		Class<? extends Command> implClass = command.getClass();
		if (command instanceof BatchCommand){
		    return (D) ((BatchCommand) command).execute();
        } else {
            try {
                return EXECUTOR_SERVICE.submit(new Worker<D>(implClass.newInstance(), obj)).get();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("execute exception - {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
	}
}
