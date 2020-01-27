package org.test.di.registry;

public class AutowireBeanRegistry {
    
    private String fieldType;
    private String beanToSet;
    private String beanComponent;

    public AutowireBeanRegistry(String fieldType, String beanToSet, String beanComponent) {
        this.fieldType = fieldType;
        this.beanToSet = beanToSet;
        this.beanComponent = beanComponent;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getBeanToSet() {
        return beanToSet;
    }

    public String getBeanComponent() {
        return beanComponent;
    }
}
