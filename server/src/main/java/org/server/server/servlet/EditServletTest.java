package org.server.server.servlet;

import com.common.model.Customer;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.server.dao.jdbc.CustomerJdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class EditServletTest extends CustomServlet {
    private static final long serialVersionUID = 3986847631241537204L;
    private static final String PATH = "/edit";
    private static final Logger log = LoggerFactory.getLogger(EditServletTest.class);
    
    @Autowired
    CustomerJdbcDao customerDao;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("doGet Method activated, request - {}", req.getQueryString());
        Map<String, List<String>> stringListMap = HttpUtils.splitQuery(req.getQueryString());
        stringListMap.forEach((key, value) -> {
            log.info("Key - {}, value - {}", key, value);
        });
        Customer current = EditCustomerServlet.current;
        if (current != null) {
            log.info("Current customer - {}", current.getCustomerName());
            current.setSsn(stringListMap.get("ssn").get(0));
            current.setCustomerName(stringListMap.get("customerName").get(0));
            current.setAddress(stringListMap.get("address").get(0));
            customerDao.update(current);
            resp.sendRedirect("/list");
        }
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("doPost Method activated, request - {}", req.getQueryString());
        Map<String, List<String>> stringListMap = HttpUtils.splitQuery(req.getQueryString());
        stringListMap.forEach((key, value) -> {
            log.info("Key - {}, value - {}", key, value);
        });
        Customer current = EditCustomerServlet.current;
        if (current != null) {
            log.info("Current customer - {}", current.getCustomerName());
            current.setSsn(stringListMap.get("ssn").get(0));
            current.setCustomerName(stringListMap.get("customerName").get(0));
            current.setAddress(stringListMap.get("address").get(0));
            customerDao.update(current);
            resp.sendRedirect("/list");
        }
    }
    
    @Override
    public String getPath() {
        return PATH;
    }
}
