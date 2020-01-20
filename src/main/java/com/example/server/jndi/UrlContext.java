package com.example.server.jndi;

import com.example.server.manager.ConnectionManager;
import com.example.server.util.FastHashtable;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                return new Gson().fromJson(lookup.toString(), Object.class);
            } catch (ProtocolException e) {
                log.error("{}", e.getMessage());
            } catch (IOException e) {
                log.error("Unable to send data to the server, {}", e.getMessage());
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
                parameters.put(name, obj);
                Gson gson = new Gson();
                String s = gson.toJson(parameters, HashMap.class);
                log.debug("Converted JSON - {}", s);
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(s);
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
            }
        }
    }

    @Override
    public void rebind(Name name, Object obj) {
        bind(name, obj);
    }

    @Override
    public void rebind(String name, Object obj) {
        bind(name, obj);
    }

    @Override
    public void unbind(Name name) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> stringIterator = name.getAll().asIterator();
        stringIterator.forEachRemaining(str -> sb.append(str).append('.'));
        unbind(sb.toString());
    }

    @Override
    public void unbind(String name) {
        HttpURLConnection con = manager.getConnection(urlFormString + "/jndi?name=" + name);
        if (con != null) {
            try {
                con.setRequestMethod("DELETE");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder lookup = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    lookup.append(inputLine);
                }
                in.close();
                con.disconnect();
                log.debug("Result - {}", lookup);
            } catch (ProtocolException e) {
                log.error("{}", e.getMessage());
            } catch (IOException e) {
                log.error("Unable to send data to the server, {}", e.getMessage());
            }
        }
    }

    @Override
    public void rename(Name oldName, Name newName) {
        //Not implemented yet
    }

    @Override
    public void rename(String oldName, String newName) {
        //Not implemented yet
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
        //Not implemented yet
    }

    @Override
    public void destroySubcontext(String name) {
        //Not implemented yet
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
        environment.clear();
    }

    @Override
    public String getNameInNamespace() {
        return null;
    }
}
