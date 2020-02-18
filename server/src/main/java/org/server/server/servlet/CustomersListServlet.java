package org.server.server.servlet;


import com.common.model.Customer;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.server.dao.jdbc.CustomerJdbcDao;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class CustomersListServlet extends CustomServlet {
	private static final long serialVersionUID = -835091317331676452L;
	private static final String PATH = "/list";
	
	@Autowired
    CustomerJdbcDao customerDao;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<title>Customers list</title></head>");
			out.println("<body>");
			out.println("<h1>Customers list</h1>");
			List<Customer> all = customerDao.findAll();
			all.forEach(item -> {
				out.println("<p><a href=/customer?" + item.getSsn() + ">" + item.getSsn() + "</a>"
                                    + " " + item.getCustomerName() + " " + item.getAddress() + "</p>");
			});
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