package org.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Component;

@Slf4j
@Component
public class AddCustomerServlet extends CustomServlet{
    private static final long serialVersionUID = 2370356000651699145L;
    private static final String PATH = "/create";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Customers list</title></head>");
            out.println("<body>");
            out.println("<h1>Customers list</h1>");

            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            log.error("Exception in {} - {}", CustomersListServlet.class, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        
    }

    @Override
    public String getPath() {
        return PATH;
    }
}
