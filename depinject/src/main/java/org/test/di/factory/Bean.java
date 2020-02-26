package org.test.di.factory;

public class Bean {

    private int priority;
    private String beanName;
    private Object instance;
    
    public Bean(int priority, String beanName, Object instance) {
        this.priority = priority;
        this.beanName = beanName;
        this.instance = instance;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
    
}
