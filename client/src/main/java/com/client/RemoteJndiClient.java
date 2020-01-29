package com.client;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteJndiClient {
    private static final Logger log = LoggerFactory.getLogger(RemoteJndiClient.class);
    
    public static void main(String[] args) throws NamingException {
        Properties env = new Properties();
        String name = "java:comp/env/test";
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.server.jndi.RemoteInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http://localhost:8080");
        Context remoteContext = new InitialContext(env);
        String str = "value";
        remoteContext.bind(name, str);
        Object lookup = remoteContext.lookup(name);
        log.info("Lookup class - {}", lookup.getClass());
        log.info("Lookup object to String - {}", lookup);
        remoteContext.unbind(name);
        Object lookup2 = remoteContext.lookup(name);
        if (lookup2 != null){
            log.info("Lookup class for second invocation - {}", lookup2.getClass());
        }
    }
}
