package org.web.servlet;

import com.common.command.AddCustomerCommand;
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
import org.web.controller.RegistryHolder;

@Slf4j
@Component
public class AddCustomerServlet extends CustomServlet {
    private static final long serialVersionUID = 2370356000651699145L;
    private static final String PATH = "/create";

    @Autowired
    private RegistryHolder registryHolder;
    private AddCustomerCommand addCustomerCommand;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Customers list</title></head>");
            out.println("<body>");
            out.println("<h1>Add new Customer</h1>");
            out.println("<form method=\"post\" action=\"#\">");
            out.print("<p><input name=\"ssn\" value=\"ssn\">");
            out.print("<input name=\"customerName\" value=\"Customer Name\">");
            out.print("<input name=\"address\" value=\"Address\"></p>\n");
            out.print("<p><input type=\"submit\"></p>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            log.error("Exception in {} - {}", CustomersListServlet.class, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            addCustomerCommand = (AddCustomerCommand)
                    registryHolder.getRegistry().lookup(AddCustomerCommand.class.getSimpleName());
        } catch (NotBoundException | RemoteException e) {
            resp.sendError(503, "RMI server not found");
        }
        String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, List<String>> stringListMap = HttpUtils.splitQuery(test);
        stringListMap.forEach((key, value) -> {
            log.info("Key - {}, value - {}", key, value);
        });
        Customer newCustomer = new Customer();
        newCustomer.setSsn(stringListMap.get("ssn").get(0));
        newCustomer.setCustomerName(stringListMap.get("customerName").get(0));
        newCustomer.setAddress(stringListMap.get("address").get(0));
        addCustomerCommand.execute(newCustomer);
        resp.sendRedirect("/list");
    }

    @Override
    public String getPath() {
        return PATH;
    }
}
