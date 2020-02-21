package org.test.di.exceptions;

public class BeanNotFoundException extends Exception {
    
    public BeanNotFoundException(){
        super();
    }

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
