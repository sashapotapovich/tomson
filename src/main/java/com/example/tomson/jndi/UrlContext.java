package com.example.tomson.jndi;

import com.example.tomson.util.FastHashtable;
import com.example.tomson.util.ParameterStringBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class UrlContext implements Context {

    private static final Logger log = LoggerFactory.getLogger(UrlContext.class);
    private final FastHashtable<String, Object> environment;

    public UrlContext(final FastHashtable<String, Object> environment) {
        this.environment = environment;
    }

    @Override
    public Object lookup(Name name) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> stringIterator = name.getAll().asIterator();
        stringIterator.forEachRemaining(str -> sb.append(str).append('.'));
        return lookup(sb.toString().substring(0, sb.length() - 1));
    }

    @SneakyThrows
    @Override
    public Object lookup(String name) {
        String urlFormString = (String) environment.get("java.naming.provider.url");
        URL url = new URL(urlFormString + "/jndi?name=" + name);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
    }

    @Override
    public void bind(Name name, Object obj) {

    }

    @SneakyThrows
    @Override
    public void bind(String name, Object obj) {
        String urlFormString = (String) environment.get("java.naming.provider.url");
        URL url = new URL(urlFormString + "/jndi");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
