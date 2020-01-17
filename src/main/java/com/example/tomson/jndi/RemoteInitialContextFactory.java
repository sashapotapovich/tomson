package com.example.tomson.jndi;

import com.example.tomson.util.FastHashtable;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

;

public class RemoteInitialContextFactory implements InitialContextFactory {
    
    @Override
    @SuppressWarnings("unchecked")
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return new UrlContext(new FastHashtable<>((Map<String, Object>) (Map) environment));
    }
}
