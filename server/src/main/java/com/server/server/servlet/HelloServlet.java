package com.server.server.servlet;


import com.common.model.Customer;
import com.server.annotation.CrearecWebServlet;
import com.server.dao.CustomerDao;
import com.server.dao.jdbc.CustomerJdbcDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.test.di.annotations.Component;

@CrearecWebServlet("/test")
@Component
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = -835091317331676452L;
	
	CustomerDao customerDao = new CustomerJdbcDao();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<title>Hello, World</title></head>");
			out.println("<body>");
			out.println("<h1>Hello, world!</h1>");  // says Hello
			// Echo client's request information
			List<Customer> all = customerDao.findAll();
			all.forEach(item -> {
				out.println("<p>" + item.getSsn() + " " + item.getCustomerName() + " " + item.getAddress() + "</p>");
			});
			out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
			// Generate a random number upon each request
			out.println("<p>A Random Number: <strong>" + Math.random() + "</strong></p>");
			out.println("</body>");
			out.println("</html>");
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}