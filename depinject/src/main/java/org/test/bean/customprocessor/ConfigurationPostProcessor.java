package org.test.bean.customprocessor;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.Configuration;
import org.test.di.config.BeanPostProcessor;
import org.test.di.config.ConfigurationProvider;

@Component
public class ConfigurationPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(PostConstructPostProcessor.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean){
        log.info("Configuration Provider Post Processor Method After");
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Configuration.class)) {
                Configuration annotation = field.getAnnotation(Configuration.class);
                String prefix = annotation.prefix();
                if (!prefix.isBlank()){
                    prefix = prefix + ".";
                }
                String configurationValue = configurationProvider.getConfigurationValue(prefix + field.getName());
                if (!configurationValue.isBlank()) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    try {
                        switch (type.getSimpleName()) {
                            case "String":
                                field.set(bean, configurationValue);
                                break;
                            case "int":
                            case "Integer":
                                field.setInt(bean, Integer.parseInt(configurationValue));
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + type.getSimpleName());
                        }
                    } catch (IllegalAccessException e) {
                        log.error("Unable to set the value - {} to the field - {}",
                                  configurationValue, field.getDeclaringClass());
                    }
                    field.setAccessible(false);
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
