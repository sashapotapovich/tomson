package com.example.server.postprocessor;

import com.example.server.annotations.Resource;
import com.example.server.jndi.RemoteInitialContextFactory;
import java.lang.reflect.Field;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.config.BeanPostProcessor;

@Component
public class ResourcePostProcessor implements BeanPostProcessor {
    private final Logger log = LoggerFactory.getLogger(ResourcePostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(String s, Object o) {
        log.info("Post Construct Method Before");
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Resource.class)) {
                try {
                    Resource annotation = field.getAnnotation(Resource.class);
                    String name = annotation.name();
                    Properties env = new Properties();
                    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.server.jndi.RemoteInitialContextFactory");
                    env.put(Context.PROVIDER_URL, "http://localhost:8080");
                    RemoteInitialContextFactory factory = new RemoteInitialContextFactory();
                    Object lookup = factory.getInitialContext(env).lookup(name);
                    field.setAccessible(true);
                    field.set(o, lookup);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException("Unable to set value for - " + s);
                } catch (NamingException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(String s, Object o) {
        return null;
    }
}
