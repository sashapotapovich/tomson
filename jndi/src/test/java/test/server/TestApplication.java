package test.server;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import lombok.SneakyThrows;
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
    
    @SneakyThrows
    @Test
    public void test(){
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
