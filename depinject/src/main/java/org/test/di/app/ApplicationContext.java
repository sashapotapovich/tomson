package org.test.di.app;

import org.test.di.factory.Bean;
import org.test.di.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.factory.BeanFactoryFacade;

public class ApplicationContext {
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);
    
    private BeanFactoryFacade beanFactoryFacade = new BeanFactoryFacade();

    public ApplicationContext(String basePackage) {
        log.info("          ==========   Welcome   ==========          ");
        beanFactoryFacade.initiate(basePackage);
    }

    public Object getBean(String beanName){
        Bean bean = beanFactoryFacade.getBeanFactory().getBean(beanName);
        return bean.getInstance();
    }
    
    public BeanFactory getBeanFactory(){
        return beanFactoryFacade.getBeanFactory();
    }
    
    public void close() {
        beanFactoryFacade.getBeanFactory().close();
    }
}