package com.example.tomson;

import lombok.SneakyThrows;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RemoteJndiClient {
    @SneakyThrows
    public static void main(String args[]) throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.tomson.jndi.RemoteInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http://localhost:8080");
        StringBuilder java = new StringBuilder("java");
        Context remoteContext = new InitialContext(env);
        remoteContext.bind("java:comp/env/test", java);
        Thread.sleep(5000);
        System.out.println((String)remoteContext.lookup("java:comp/env/test"));
    }
}
