package org.test.di.service;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.exceptions.BeanNotFoundException;
import org.test.di.factory.Bean;

public class ServiceLocator {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceLocator.class);

    private ServiceLocator() {
    }

    public static Bean getBean(String beanName) throws BeanNotFoundException {
        Bean bean = Cache.getInstance().getBean(beanName);
        if (bean != null) {
            return bean;
        }
        throw new BeanNotFoundException("Bean - " + beanName + " was not found!");
    }
    
    @SuppressWarnings("unchecked")
    public static Collection<Object> getAllBeansForType(Class<?> genericType) {
        Collection<Bean> allBeans = Cache.getInstance().getAllBeans();
        Collection<Object> beans = new ArrayList<>();
        for (Bean bean : allBeans) {
            if (genericType.isAssignableFrom(bean.getInstance().getClass())){
                beans.add(bean.getInstance());
            }
        }
        return beans;
    }
}
