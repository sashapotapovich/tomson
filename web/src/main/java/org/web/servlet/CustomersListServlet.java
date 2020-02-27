package org.web.servlet;


import com.common.command.FindAllCustomersCommand;
import com.common.model.Customer;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.web.controller.RegistryHolder;

@Slf4j
@Component
public class CustomersListServlet extends CustomServlet {
	private static final long serialVersionUID = -835091317331676452L;
	private static final String PATH = "/list";
    private FindAllCustomersCommand findAllCustomersCommand;
    @Autowired
    private RegistryHolder registryHolder;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            findAllCustomersCommand = (FindAllCustomersCommand)
                    registryHolder.getRegistry().lookup(FindAllCustomersCommand.class.getSimpleName());
        } catch (NotBoundException | RemoteException e) {
            resp.sendError(503, "RMI server not found");
        }
		resp.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = resp.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<head>\n" +
                                "<style>\n" +
                                "table {\n" +
                                "  font-family: arial, sans-serif;\n" +
                                "  border-collapse: collapse;\n" +
                                "  width: 100%;\n" +
                                "}\n" +
                                "\n" +
                                "td, th {\n" +
                                "  border: 1px solid #dddddd;\n" +
                                "  text-align: left;\n" +
                                "  padding: 8px;\n" +
                                "}\n" +
                                "\n" +
                                "tr:nth-child(even) {\n" +
                                "  background-color: #dddddd;\n" +
                                "}\n" +
                                "</style>\n" +
                                "</head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<title>Customers list</title></head>");
			out.println("<body>");
			out.println("<h1>Customers list</h1>");
            List<Customer> all = findAllCustomersCommand.execute();
            all.sort(Comparator.comparingLong(Customer::getId));
            out.println("<table>");
            out.println("<tr><th>ID</th><th>SSN</th><th>Customer Name</th><th>Address</th></tr>");
			all.forEach(item -> {
                out.println("<tr><td><a href=/customer?" + item.getId() + ">" + item.getId() + "</a></td>"
                                    + "<td>" + item.getSsn() + "</td><td>"
                                    + item.getCustomerName() + "</td><td>"
                                    + item.getAddress() + "</td></tr>");
            });
			out.println("</table><p>");
			out.println("<a href=\"#\"></a> ");
			out.println("</p><input type=\"submit\" value=\"Add New Customer\"\n" +
                                "    onclick=\"window.location='/create'\"/> ");
			out.println("</body>");
			out.println("</html>");
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

    public String getPath() {
	    return PATH;
    }
}