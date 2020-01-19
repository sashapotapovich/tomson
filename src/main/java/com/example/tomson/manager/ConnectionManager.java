package com.example.tomson.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    public HttpURLConnection getConnection(String url) {
        HttpURLConnection con = null;
        try {
            URL address = new URL(url);
            con = (HttpURLConnection) address.openConnection();
        } catch (IOException e) {
            log.error("Could not open new connection, error - {}", e.getMessage());
        }
        return con;
    }
}
