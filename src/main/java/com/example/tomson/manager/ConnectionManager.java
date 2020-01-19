package com.example.tomson.manager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionManager {

    public HttpURLConnection getConnection(String url) throws IOException {
        URL adress = new URL(url);
        HttpURLConnection con = (HttpURLConnection) adress.openConnection();
        return con;
    }
}
