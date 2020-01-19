package com.example.tomson;

import org.test.di.app.ApplicationContext;
import org.test.di.factory.BeanFactory;

class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.example.tomson");
        //BeanFactory beanFactory = applicationContext.getBeanFactory();
        /*Server server = (Server) beanFactory.getBean("server");
        server.init(8080);*/
    }

}