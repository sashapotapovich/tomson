package test.server;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.test.di.app.ApplicationContext;

@RunWith(JUnit4.class)
public class TestApplication {
    
    static {
        ApplicationContext applicationContext = new ApplicationContext("com.example.server");
    }
    
    @Test
    public void test() throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.example.server.jndi.RemoteInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http://localhost:8080");
        Context remoteContext = new InitialContext(env);
        String str = "value";
        remoteContext.bind("java:comp/env/test", str);
        Object lookup = remoteContext.lookup("java:comp/env/test");
        Assert.assertEquals("value", lookup.toString());
    }
}
