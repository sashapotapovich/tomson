package com.server.server.servlet;

import javax.servlet.http.HttpServlet;

public class EditController extends HttpServlet implements CustomServlet {
    private static final String PATH = "/edit";
    
    
    @Override
    public String getPath() {
        return PATH;
    }
}
