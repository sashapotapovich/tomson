package org.test.bean.customprocessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;
import org.test.di.config.BeanPostProcessor;

@Component
public class PostConstructPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(PostConstructPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        log.info("Post Construct Method Before");
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                if (method.getParameterTypes().length != 0) {
                    log.error("Initialization method should not accept parameters, Bean name - {}", beanName);
                    throw new ExceptionInInitializerError("Initialization method should not accept parameters");
                }
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                    method.setAccessible(false);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.toString());
                    throw new RuntimeException("Unable to initialize Bean - " + beanName);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        return bean;
    }
}
