package com.example.server;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lombok.SneakyThrows;

public class RemoteJndiClient {
    @SneakyThrows
    public static void main(String[] args) throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.server.jndi.RemoteInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http://localhost:8080");
        Context remoteContext = new InitialContext(env);
        String str = "value";
        remoteContext.bind("java:comp/env/test", str);
        Object lookup = remoteContext.lookup("java:comp/env/test");
        System.out.println(lookup.getClass());
        System.out.println(lookup);
    }
}
