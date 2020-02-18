package org.test.di.factory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.config.BeanPostProcessor;
import org.test.di.exceptions.BeanNotFoundException;
import org.test.di.service.Cache;
import org.test.di.service.ServiceLocator;
import org.test.di.utils.ClassUtil;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<String, List<Field>> autowireCandidates = new HashMap<>();
    private Cache context = Cache.getInstance();
    private PriorityQueue<BeanPostProcessor> postProcessors = new PriorityQueue<>(Comparator.comparing(BeanPostProcessor::getPriority));

    private void addPostProcessor(BeanPostProcessor postProcessor) {
        log.info("Registering Bean Post Processors");
        postProcessors.add(postProcessor);
    }

    private void registerBeans(Class<?> classObject, String className) {
        try {
            if (classObject.isAnnotationPresent(Component.class)) {
                log.info("Found new Component - {}", classObject);
                Object instance = classObject.getDeclaredConstructor().newInstance();
                if (instance instanceof BeanPostProcessor) {
                    BeanPostProcessor postProcessor = (BeanPostProcessor) instance;
                    addPostProcessor(postProcessor);
                    //return;
                }
                Component component = classObject.getAnnotation(Component.class);
                String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                log.info("Generated BEAN name - {}", beanName);
                context.addBean(beanName, instance);
                for (Field field : classObject.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        String classNameForAuto = field.getType().getSimpleName();
                        String fieldName = classNameForAuto.substring(0, 1).toLowerCase() + classNameForAuto.substring(1);
                        autowireCandidates.computeIfAbsent(fieldName, k -> new ArrayList<>())
                                          .add(field);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.toString());
        }
    }

    private void forJar(String path) {
        File jarFile = null;
        try {
            jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            log.error(e.toString());
        }
        String actualFile = jarFile.getAbsolutePath();
        log.info("jarFile is : {}", jarFile.getAbsolutePath());
        log.info("actulaFilePath is : {}", actualFile);
        try (JarFile jar = new JarFile(actualFile)) {
            List<String> collect = jar.stream()
                                      .filter(jarEntry -> jarEntry.getName().startsWith(path))
                                      .filter(jarEntry -> !jarEntry.isDirectory())
                                      .map(ZipEntry::getName)
                                      .collect(Collectors.toList());
            for (String className : collect) {
                className = className.replace("/", ".")
                                     .substring(0, className.length() - 6);
                log.info("JAR ClassName - {}", className);
                Class<?> classObject = Class.forName(className);
                className = className.substring(className.lastIndexOf('.') + 1);
                registerBeans(classObject, className);
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.toString());
        }
    }


    public void instantiate(String basePackage) {
        try {
            ClassLoader classLoader = ClassUtil.getClassLoader();
            String path = basePackage.replace('.', '/');
            log.info("Path created - {}", path);
            Enumeration<URL> resourceUrls = classLoader.getResources(path);
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                URI uri = url.toURI();
                if (uri.isOpaque()) {
                    log.info("We are in the JAR file, so will work with file system as with jar");
                    forJar(path);
                    return;
                }
                File file = new File(uri);
                for (File classFile : file.listFiles()) {
                    if (classFile.isDirectory()) {
                        log.info("We located subderictory - {}", classFile.getAbsolutePath());
                        instantiate(basePackage + "." + classFile.getName());
                        continue;
                    }
                    log.info("Running for the file - {}", classFile.getAbsolutePath());
                    String fileName = classFile.getName();
                    String className = null;
                    if (fileName.endsWith(".class")) {
                        className = fileName.substring(0, fileName.lastIndexOf('.'));
                    }
                    Class<?> classObject = Class.forName(basePackage + "." + className);
                    registerBeans(classObject, className);
                }
            }
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            log.error(e.toString());
        }
    }

    public void populateProperties() {
        autowireCandidates.keySet().forEach(value -> log.info("Autowire Candidates held in list - {}", value));
        for (String beanName : autowireCandidates.keySet()) {
            try {
                for (Field field : autowireCandidates.get(beanName)) {
                    log.info("Autowiring Field - {}", field.toGenericString());
                    log.error("Type Class - {}", field.getGenericType().getTypeName());
                    if(Collection.class.isAssignableFrom(field.getType())) {
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            Type[] parameters = ((ParameterizedType) genericType).getActualTypeArguments();
                            for (Type param : parameters) {
                                log.error("Type class - {}", param.getTypeName());
                                Class<?> aClass = Class.forName(param.getTypeName());
                                Collection<Object> allBeansForType = ServiceLocator.getAllBeansForType(aClass);
                                if(!allBeansForType.isEmpty()){
                                    String simpleName = field.getDeclaringClass().getSimpleName();
                                    simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                                    Object target = ServiceLocator.getBean(simpleName);
                                    field.setAccessible(true);
                                    field.set(target, allBeansForType);
                                    field.setAccessible(false);
                                }
                            }
                        }
                    } else {
                        field.setAccessible(true);
                        String simpleName = field.getDeclaringClass().getSimpleName();
                        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                        Object target = ServiceLocator.getBean(simpleName);
                        Object bean = ServiceLocator.getBean(beanName);
                        field.set(target, bean);
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException | BeanNotFoundException | ClassNotFoundException e) {
                log.error(e.toString());
            }
        }
    }

    public void processBeforeBeanInitialization() {
        if (!postProcessors.isEmpty()) {
            for (Object bean : context.getAllBeans()) {
                String name = bean.getClass().getSimpleName();
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                for (BeanPostProcessor postProcessor : postProcessors) {
                    postProcessor.postProcessBeforeInitialization(name, bean);
                }
            }
        }
    }

    public void processAfterBeanInitialization() {
        if (!postProcessors.isEmpty()) {
            for (Object bean : context.getAllBeans()) {
                String name = bean.getClass().getSimpleName();
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                for (BeanPostProcessor postProcessor : postProcessors) {
                    postProcessor.postProcessAfterInitialization(name, bean);
                }
            }
        }
    }

    public Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public Collection<Object> getAllBeans() {
        return context.getAllBeans();
    }

    public void close() {
        context.clear();
        postProcessors.clear();
    }
}