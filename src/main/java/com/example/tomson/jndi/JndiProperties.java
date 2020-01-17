package com.example.tomson.jndi;

import java.util.HashMap;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class JndiProperties {
    
    private HashMap<String, Object> props;
    
    @PostConstruct
    private void init(){
        props = new HashMap<>();
    }
    
    public Object getJndiProperty(String name){
        return props.get(name);
    }
    
}
