package org.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Component
public class ConnectionProvider {
    
    private SessionFactory sessionFactory;
    private Session session;
    
    @PostConstruct
    private void init(){
        final StandardServiceRegistry dbRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources(dbRegistry).buildMetadata().buildSessionFactory();
            session = sessionFactory.openSession();
            session.setHibernateFlushMode(FlushMode.COMMIT);
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(dbRegistry);
            log.error("cannot create sessionFactory", e);
            System.exit(1);
        }
    }
    
    public Session getSession(){
        return session;
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
