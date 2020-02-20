package org.server.server.servlet;

import com.common.model.Customer;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.server.dao.jdbc.CustomerJdbcDao;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class EditCustomerServlet extends CustomServlet {
    private static final long serialVersionUID = 6172910834743905988L;
    private final String PATH = "/customer";
    public static Customer current = null;
    
    @Autowired
    CustomerJdbcDao customerDao;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String ssn = request.getRequestURI().substring(PATH.length() + 1);
        current = customerDao.findBySsn(ssn);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Customers</title></head>");
            out.println("<body>");
            if (current != null) {
                out.println("<h1>Customers " + current.getCustomerName() + "</h1>");
                out.print("<form method=\"post\" action=\"/edit\">\n" +
                                  "   <p><input name=\"ssn\" value=\"" + current.getSsn() + "\"> " +
                                  "<input name=\"customerName\" value=\"" + current.getCustomerName() + "\"> " +
                                  "<input name=\"address\" value=\"" + current.getAddress() +"\"></p>\n" +
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
