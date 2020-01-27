package com.example.server.jndi;

import com.example.server.util.FastHashtable;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

public class RemoteInitialContextFactory implements InitialContextFactory {
    
    @Override
    @SuppressWarnings("unchecked")
    public Context getInitialContext(Hashtable<?, ?> environment) {
        return new UrlContext(new FastHashtable<>((Map<String, Object>) (Map) environment));
    }
}
