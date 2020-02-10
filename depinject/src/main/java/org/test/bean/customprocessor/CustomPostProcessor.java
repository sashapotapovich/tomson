package org.test.bean.customprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.config.BeanPostProcessor;

@Component
public class CustomPostProcessor implements BeanPostProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(CustomPostProcessor.class);
    
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        log.info("Running CustomPostProcessor Before Init for Bean - {}", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        log.info("Running CustomPostProcessor After Init for Bean - {}", beanName);
        return bean;
    }

    @Override
    public Integer getPriority() {
        return 0;
    }
}