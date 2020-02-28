package org.web;


import lombok.extern.slf4j.Slf4j;
import org.test.di.app.ApplicationContext;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Starting...");
        ApplicationContext.run();
    }

}
