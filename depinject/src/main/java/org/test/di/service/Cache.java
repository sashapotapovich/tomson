package org.test.di.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.utils.Pair;

import java.util.HashMap;
import java.util.Set;

public class Cache {
    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    private static Cache instance = null;
    private final HashMap<String, Pair<Class<?>, Object>> beans = new HashMap<>();

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
        Class<?> cast = beans.get(beanName).getLeft();
        return cast.cast(beans.get(beanName).getRight());
    }

    public Set<String> getAllBeanNames() {
        return beans.keySet();
    }

    public void addBean(String beanName, Pair<Class<?>, Object> bean) {
        log.info("Adding new Bean to Cache - {}", beanName);
        beans.put(beanName, bean);
    }

    public void clear() {
        beans.clear();
    }
}
