package org.test.di.registry;

public interface AnnotationConfigRegistry {
    
    void register(Class<?>... annotatedClasses);
    
    void scan(String... basePackages);

}
