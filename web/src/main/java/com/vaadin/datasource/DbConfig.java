package com.vaadin.datasource;

import javax.persistence.EntityManager;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.jpa.HibernatePersistenceProvider;

@Getter
@Setter
public class DbConfig {

    private boolean autoCommit;
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private String hibernateDialect;
    private boolean showSQL;

    public EntityManager entityManager() {
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        return null;
    }

}
