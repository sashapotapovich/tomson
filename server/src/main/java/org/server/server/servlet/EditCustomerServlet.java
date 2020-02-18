package org.server.server.servlet;

import com.common.model.Customer;
import org.server.dao.jdbc.CustomerJdbcDao;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class EditCustomerServlet extends HttpServlet implements CustomServlet {
    private static final long serialVersionUID = 6172910834743905988L;
    private final String PATH = "/customer";

    @Autowired
    CustomerJdbcDao customerDao;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String ssn = request.getRequestURI().substring(PATH.length() + 1);
        System.out.println(ssn);
        Customer bySsn = customerDao.findBySsn(ssn);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Customers</title></head>");
            out.println("<body>");
            if (bySsn != null) {
                out.println("<h1>Customers " + bySsn.getCustomerName() + "</h1>");
                out.print("<form action=\"/edit\">\n" +
                                  "   <p><input name=\"customerName\" value=\"" + bySsn.getCustomerName() + "\"> " +
                                  "<input name=\"address\" value=\"" + bySsn.getAddress() +"\"> " +"></p>\n" +
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

    public String getPath() {
        return PATH;
    }
}