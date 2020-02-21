package org.test.di.service;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.exceptions.BeanNotFoundException;

public class ServiceLocator {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceLocator.class);

    private ServiceLocator() {
    }

    public static Object getBean(String beanName) throws BeanNotFoundException {
        Object bean = Cache.getInstance().getBean(beanName);
        if (bean != null) {
            return bean;
        }
        throw new BeanNotFoundException("Bean - " + beanName + "was not found!");
    }
    
    @SuppressWarnings("unchecked")
    public static Collection<Object> getAllBeansForType(Class<?> genericType) {
        Collection<Object> allBeans = Cache.getInstance().getAllBeans();
        Collection<Object> beans = new ArrayList<>();
        for (Object bean : allBeans) {
            if (genericType.isAssignableFrom(bean.getClass())){
                beans.add(bean);
            }
        }
        return beans;
    }
}
