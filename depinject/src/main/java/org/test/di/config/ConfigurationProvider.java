package org.test.di.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component(priority = 1)
public class ConfigurationProvider {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationProvider.class);
    
    private static final String equalSign = "=";
    private HashMap<String, String> configuration;
    
    @PostConstruct
    public void init(){
        InputStream resourceAsStream = ConfigurationProvider.class
                .getClassLoader().getResourceAsStream("application.properties");
        if (resourceAsStream != null){
            log.info("Application properties file was found, Collecting values");
            configuration = new HashMap<>();
            new BufferedReader(
                    new InputStreamReader(resourceAsStream)).lines()
                                                            .parallel()
                                                            .filter(line -> !line.isBlank() && line.contains(equalSign))
                                                            .forEach(line -> {
                                                                int index = line.indexOf(equalSign);
                                                                String key = line.substring(0, index)
                                                                                 .strip().replaceAll("\"|'","");
                                                                String value = line.substring(index + 1)
                                                                                   .strip().replaceAll("\"|'","");
                                                                log.info("Configuration found - {} : {}", key, value);
                                                                configuration.put(key, value);
                                                            });

            }
        }
    
    public String getConfigurationValue(String name){
        return configuration.getOrDefault(name, "");
    }
}
