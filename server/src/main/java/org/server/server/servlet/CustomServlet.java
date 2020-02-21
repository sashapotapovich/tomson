package org.server.server.servlet;

import javax.servlet.http.HttpServlet;

public abstract class CustomServlet extends HttpServlet{
    public abstract String getPath();
}
