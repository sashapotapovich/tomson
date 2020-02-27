package org.test.di.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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
            List<String> collect = new BufferedReader(
                    new InputStreamReader(resourceAsStream)).lines().collect(Collectors.toList());
            collect.forEach(line -> {
                if (!line.isBlank() && line.contains(equalSign)) {
                    line = line.replaceFirst(equalSign, "@%");
                    String[] split = line.split("@%");
                    for (int i = 0; i < split.length; i++){
                        split[i] = split[i].strip().replaceAll("\"|'","");
                    }
                    configuration.put(split[0].strip(), split[1].strip());
                }
            });
        } else {
            log.info("application.properties file was not found in the /resources folder");
        }
    }
    
    public String getConfigurationValue(String name){
        return configuration.getOrDefault(name, "");
    }
}
