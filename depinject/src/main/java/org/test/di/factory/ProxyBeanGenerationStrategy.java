package org.test.di.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.service.Cache;

public class ProxyBeanGenerationStrategy implements BeanGenerationStrategy {
    
    private static final Logger log = LoggerFactory.getLogger(ProxyBeanGenerationStrategy.class);
    
    @Override
    public Object getBean(String beanName) {
        log.info("Trying to get Bean as Proxy");
        Object bean = Cache.getInstance().getBean(beanName);
        Class<?> beanClass = bean.getClass();
        Class<?>[] genericInterfaces = beanClass.getInterfaces();
        int countMethods = 0;
        for (Class<?> genericInterface : genericInterfaces) {
            Method[] methods = genericInterface.getMethods();
            if (methods.length != 0) {
                countMethods++;
            }
        }
        if (countMethods != 0) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "Hello From dynamic Proxy!";
                } else {
                    return method.invoke(proxy, args);
                }
            });
        } else {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "Hello From CGLIB Proxy!";
                } else {
                    return proxy.invokeSuper(obj, args);
                }
            });
            return enhancer.create();
        }
    }
}
