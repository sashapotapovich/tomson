package com.example.tomson;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RemoteJndiClient {

    public static void main(String args[]) throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.tomson.jndi.RemoteInitialContextFactory");
        env.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        Context remoteContext = new InitialContext(env);
        System.out.println((String)remoteContext.lookup("java:comp/env/test"));
    }
}
