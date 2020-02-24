package org.web.servlet;

import com.common.command.FindCustomerByIdCommand;
import com.common.command.UpdateCustomerCommand;
import com.common.model.Customer;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.web.RegistryHolder;

@Slf4j
@Component
public class EditCustomerServlet extends CustomServlet {
    private static final long serialVersionUID = 6172910834743905988L;
    private final String PATH = "/customer";
    public static Customer current = null;
    
    @Autowired
    private RegistryHolder registryHolder;
    
    private FindCustomerByIdCommand findCustomerByIdCommand;
    private UpdateCustomerCommand updateCustomerCommand;
    
/*    @PostConstruct
    private void initialize() throws RemoteException, NotBoundException {
        findCustomerByIdCommand = (FindCustomerByIdCommand) 
                registryHolder.getRegistry().lookup(FindCustomerByIdCommand.class.getSimpleName());
        updateCustomerCommand = (UpdateCustomerCommand) 
                registryHolder.getRegistry().lookup(UpdateCustomerCommand.class.getSimpleName());
    }*/
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            findCustomerByIdCommand = (FindCustomerByIdCommand)
                    registryHolder.getRegistry().lookup(FindCustomerByIdCommand.class.getSimpleName());
        } catch (RemoteException | NotBoundException e) {
            response.sendError(503, "RMI server not found");
        }
        String id = request.getRequestURI().substring(PATH.length() + 1);
        log.debug("Customer's ID - {}", id);
        Customer customer = new Customer();
        customer.setId(Long.valueOf(id));
        current = findCustomerByIdCommand.execute(customer);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Customers</title></head>");
            out.println("<body>");
            if (current != null) {
                out.println("<h1>Customers " + current.getCustomerName() + "</h1>");
                out.println("<form method=\"post\" action=\"#\">\n");
                out.println("<label for=\"fname\">SSN:</label>");
                out.println("<label for=\"cname\">Customer Name:</label>");
                out.println("<label for=\"caddr\">Address:</label>");
                out.print(" <p><input name=\"ssn\" id=\"fname\" value=\"" + current.getSsn() + "\"> " +
                          "<input name=\"customerName\" id=\"cname\" value=\"" + current.getCustomerName() + "\"> " +
                          "<input name=\"address\" id=\"caddr\" value=\"" + current.getAddress() +"\"></p>\n" +
                          "   <p><input type=\"submit\"></p>");

            } else {
                out.println("<p> Not Found </p>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            updateCustomerCommand = (UpdateCustomerCommand)
                    registryHolder.getRegistry().lookup(UpdateCustomerCommand.class.getSimpleName());
        } catch (NotBoundException | RemoteException e) {
            resp.sendError(503, "RMI server not found");
        }
        String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, List<String>> stringListMap = HttpUtils.splitQuery(test);
        stringListMap.forEach((key, value) -> {
            log.info("Key - {}, value - {}", key, value);
        });
        if (current != null) {
            log.info("Current customer - {}", current.getCustomerName());
            current.setSsn(stringListMap.get("ssn").get(0));
            current.setCustomerName(stringListMap.get("customerName").get(0));
            current.setAddress(stringListMap.get("address").get(0));
            updateCustomerCommand.execute(current);
            resp.sendRedirect("/list");
        }
    }
    
    public String getPath() {
        return PATH;
    }
}
