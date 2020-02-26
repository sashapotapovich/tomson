package org.web.servlet;

import com.common.command.DeleteCustomerCommand;
import com.common.model.Customer;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.web.RegistryHolder;

@Slf4j
@Component
public class DeleteCustomerServlet extends CustomServlet{
    private static final long serialVersionUID = -8525654863157019345L;
    private static final String PATH = "/delete";
    
    @Autowired
    private RegistryHolder registryHolder;
    private DeleteCustomerCommand deleteCustomerCommand;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            deleteCustomerCommand = (DeleteCustomerCommand)
                    registryHolder.getRegistry().lookup(DeleteCustomerCommand.class.getSimpleName());
        } catch (NotBoundException | RemoteException e) {
            resp.sendError(503, "RMI server not found");
        }
        String id = req.getRequestURI().substring(PATH.length() + "?id=".length());
        log.debug("Customer's ID - {}", id);
        Customer customer = new Customer();
        customer.setId(Long.valueOf(id));
        deleteCustomerCommand.execute(customer);
        resp.sendRedirect("/list");
    }

    @Override
    public String getPath() {
        return PATH;
    }
}
