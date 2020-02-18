package org.test.di.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cache {
    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    private static Cache instance = null;
    private final HashMap<String, Object> beans = new HashMap<>();

    private Cache() {
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public Object getBean(String beanName) {
        log.info("Trying to get Bean from Cache - {}", beanName);
        return beans.get(beanName);
    }

    public Set<String> getAllBeanNames() {
        return beans.keySet();
    }
    
    public Collection<Object> getAllBeans(){
        return beans.values();
    }

    public void addBean(String beanName, Object bean) {
        log.info("Adding new Bean to Cache - {}", beanName);
        beans.put(beanName, bean);
    }

    public void clear() {
        beans.clear();
    }
}
