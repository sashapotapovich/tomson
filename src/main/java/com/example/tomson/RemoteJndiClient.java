package example.tomson;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteJndiClient {

    public static void main(String args[]) throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        InitialContext remoteContext = new InitialContext(env);
        System.out.println((String)remoteContext.lookup("test"));
    }
}
