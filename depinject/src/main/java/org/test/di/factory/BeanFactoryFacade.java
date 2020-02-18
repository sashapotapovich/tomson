package org.test.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanFactoryFacade {

    private static final Logger log = LoggerFactory.getLogger(BeanFactoryFacade.class);

    private BeanFactory beanFactory = new BeanFactory();


    public void initiate(String basePackage) {
        log.info("Context is under construction");
        beanFactory.instantiate(basePackage);
        log.info("Bean Post Processors before initialization Beans");
        beanFactory.processBeforeBeanInitialization();
        log.info("Initializing Beans");
        beanFactory.populateProperties();
        log.info("Bean Post Processors after initialization Beans");
        beanFactory.processAfterBeanInitialization();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
