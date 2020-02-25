package org.test.bean.customprocessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;
import org.test.di.config.BeanPostProcessor;
import org.test.di.exceptions.InitializationException;

@Component
public class PostConstructPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(PostConstructPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        log.info("Post Construct Method After");
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
                } catch (IllegalAccessException e) {
                    log.error(e.toString());
                } catch (InvocationTargetException e) {
                    log.error(e.toString());
                    throw new InitializationException("Unable to initialize Bean - " + beanName, e);
                }
            }
        }
        return bean;
    }

    @Override
    public Integer getPriority() {
        return 1;
    }
}
