package com.vaadin.datasource;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;

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
        return null;
    }

}
