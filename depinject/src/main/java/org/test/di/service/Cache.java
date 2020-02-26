package org.test.di.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.factory.Bean;

public class Cache {
    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    private static Cache instance = null;
    PriorityQueue<Bean> priorityBeans = new PriorityQueue<>(Comparator.comparingInt(Bean::getPriority));

    private Cache() {
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public Bean getBean(String beanName) {
        log.info("Trying to get Bean from Cache - {}", beanName);
        return priorityBeans.stream().filter(bean -> bean.getBeanName().equals(beanName))
                            .findFirst().get();
    }

    public Set<String> getAllBeanNames() {
        return priorityBeans.stream().map(Bean::getBeanName).collect(Collectors.toSet());
    }
    
    public Collection<Bean> getAllBeans(){
        return priorityBeans;
    }

    public void addBean(Bean bean) {
        log.info("Adding new Bean to Cache - {}", bean.getBeanName());
        priorityBeans.add(bean);
    }

    public void clear() {
        priorityBeans.clear();
    }
}
