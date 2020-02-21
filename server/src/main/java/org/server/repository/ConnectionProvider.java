package org.server.repository;

import com.common.model.Customer;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Component
public class ConnectionProvider {
    
    private SessionFactory sessionFactory;
    private String dialect="org.hibernate.dialect.PostgreSQL82Dialect";
    private String driver_class="org.postgresql.Driver";
    private String provider_class="org.hibernate.c3p0.internal.C3P0ConnectionProvider";
    private String username="postgres";
    private String password="postgres";
    private String url="jdbc:postgresql://127.0.0.1:5432/postgres";

    private String min_size="5";
    private String max_size="20";
    private String acquire_increment="5";
    private String timeout="1800";

    private String format_sql="true";
    private String use_sql_comments="true";
    
    @PostConstruct
    public void init(){
/*        final StandardServiceRegistry dbRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources(dbRegistry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(dbRegistry);
            log.error("cannot create sessionFactory", e);
            System.exit(1);
        }*/
        try {
            Configuration configuration = new Configuration();
            // Hibernate settings equivalent to hibernate.cfg.xml's properties
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, driver_class);
            settings.put(Environment.URL, url);
            settings.put(Environment.USER, username);
            settings.put(Environment.PASS, password);
            settings.put(Environment.DIALECT, dialect);
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.CONNECTION_PROVIDER, provider_class);
            settings.put(Environment.FORMAT_SQL, format_sql);
            settings.put(Environment.USE_SQL_COMMENTS, use_sql_comments);
            
            settings.put(Environment.C3P0_MIN_SIZE, min_size);
            settings.put(Environment.C3P0_MAX_SIZE, max_size);
            settings.put(Environment.C3P0_ACQUIRE_INCREMENT, acquire_increment);
            settings.put(Environment.C3P0_TIMEOUT, timeout);
            configuration.setProperties(settings);
            configuration.addPackage("com.common.model");
            configuration.addAnnotatedClass(Customer.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Session getSession(){
        return sessionFactory.openSession();
    }

/*    public void init2(){
        try (InputStream is = getClass().getResourceAsStream("/db.properties")) {
            Properties props = new Properties();
            props.load(is);
            host = props.getProperty("jms.host");
            port = Integer.parseInt(props.getProperty("jms.port"));
            userID = props.getProperty("jms.userID");
            password = props.getProperty("jms.password");
            queueManager = props.getProperty("jms.queueManager");
            channel = props.getProperty("jms.channel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
}
