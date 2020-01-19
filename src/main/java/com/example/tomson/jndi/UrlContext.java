package com.example.tomson.jndi;

import com.example.tomson.manager.ConnectionManager;
import com.example.tomson.util.FastHashtable;
import com.example.tomson.util.ParameterStringBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class UrlContext implements Context {

    private static final Logger log = LoggerFactory.getLogger(UrlContext.class);
    private final FastHashtable<String, Object> environment;
    private final String urlFormString;
    private ConnectionManager manager;

    public UrlContext(final FastHashtable<String, Object> environment) {
        this.environment = environment;
        manager = new ConnectionManager();
        urlFormString = (String) environment.get("java.naming.provider.url");
    }

    @Override
    public Object lookup(Name name) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> stringIterator = name.getAll().asIterator();
        stringIterator.forEachRemaining(str -> sb.append(str).append('.'));
        return lookup(sb.toString().substring(0, sb.length() - 1));
    }
    
    @Override
    public Object lookup(String name) {
        HttpURLConnection con = manager.getConnection(urlFormString + "/jndi?name=" + name);
        if (con != null) {
            try {
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder lookup = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    lookup.append(inputLine);
                }
                in.close();
                con.disconnect();
                return lookup.toString();
            } catch (ProtocolException e) {
                log.error("{}", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error("Unable to send data to the server, {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void bind(Name name, Object obj) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> stringIterator = name.getAll().asIterator();
        stringIterator.forEachRemaining(str -> sb.append(str).append('.'));
        bind(sb.toString(), obj);
    }
    
    @Override
    public void bind(String name, Object obj) {
        HttpURLConnection con = manager.getConnection(urlFormString + "/jndi");
        if (con != null) {
            try {
                con.setRequestMethod("POST");
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("key", name);
                parameters.put("value", obj);
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
                out.flush();
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder lookup = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    lookup.append(inputLine);
                }
                log.info("Bind - {}", lookup);
                in.close();
                con.disconnect();
            } catch (IOException e) {
                log.error("{}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rebind(Name name, Object obj) {

    }

    @Override
    public void rebind(String name, Object obj) {

    }

    @Override
    public void unbind(Name name) {

    }

    @Override
    public void unbind(String name) {

    }

    @Override
    public void rename(Name oldName, Name newName) {

    }

    @Override
    public void rename(String oldName, String newName) {

    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) {
        return null;
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) {
        return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) {
        return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) {
        return null;
    }

    @Override
    public void destroySubcontext(Name name) {

    }

    @Override
    public void destroySubcontext(String name) {

    }

    @Override
    public Context createSubcontext(Name name) {
        return null;
    }

    @Override
    public Context createSubcontext(String name) {
        return null;
    }

    @Override
    public Object lookupLink(Name name) {
        return null;
    }

    @Override
    public Object lookupLink(String name) {
        return null;
    }

    @Override
    public NameParser getNameParser(Name name) {
        return null;
    }

    @Override
    public NameParser getNameParser(String name) {
        return null;
    }

    @Override
    public Name composeName(Name name, Name prefix) {
        return null;
    }

    @Override
    public String composeName(String name, String prefix) {
        return null;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) {
        return null;
    }

    @Override
    public Object removeFromEnvironment(String propName) {
        return null;
    }

    @Override
    public Hashtable<?, ?> getEnvironment() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public String getNameInNamespace() {
        return null;
    }
}
