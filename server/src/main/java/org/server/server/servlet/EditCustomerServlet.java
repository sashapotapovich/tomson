package org.server.server.servlet;

import com.common.model.Customer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
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
        log.info(ssn);
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
                out.print("<form method=\"post\" action=\"#\">\n" +
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

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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
            customerDao.update(current);
            resp.sendRedirect("/list");
        }
    }
    
    public String getPath() {
        return PATH;
    }
}
