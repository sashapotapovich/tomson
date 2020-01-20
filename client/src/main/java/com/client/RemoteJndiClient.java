package com.client;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RemoteJndiClient {
    
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
        remoteContext.unbind("java:comp/env/test");
        Object lookup2 = remoteContext.lookup("java:comp/env/test");
        System.out.println(lookup2.getClass());
    }
}
