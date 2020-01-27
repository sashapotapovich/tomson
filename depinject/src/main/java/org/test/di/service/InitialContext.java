package org.test.di.service;

public class InitialContext {

    public Object lookup(String beanName) {
        return new Object(); //we don't want to create objects with new
    }

}
