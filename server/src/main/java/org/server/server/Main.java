package org.server.server;


import com.common.command.AddCustomerCommand;
import com.common.model.Customer;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.test.di.app.ApplicationContext;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        log.info("Starting...");
        ApplicationContext applicationContext = new ApplicationContext("org");
        AddCustomerCommand addCustomerCommandImpl = (AddCustomerCommand) applicationContext
                .getBean("addCustomerCommandImpl");
        int ssn = ThreadLocalRandom.current().nextInt();
        addCustomerCommandImpl.execute(new Customer(String.valueOf(ssn), "Test", "Address Test"));
        log.info("Running...");
    }
    
}
