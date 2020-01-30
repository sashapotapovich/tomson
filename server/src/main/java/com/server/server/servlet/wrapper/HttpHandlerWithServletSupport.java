package com.server.server.servlet.wrapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

public class HttpHandlerWithServletSupport implements HttpHandler {
    private HttpServlet servlet;

    public HttpHandlerWithServletSupport(HttpServlet servlet) {
        this.servlet = servlet;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void handle(final HttpExchange ex) throws IOException {
        byte[] inBytes = getBytes(ex.getRequestBody());
        ex.getRequestBody().close();
        final ByteArrayInputStream newInput = new ByteArrayInputStream(inBytes);
        final ServletInputStream is = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return newInput.read();
            }
        };

        Map<String, String[]> parsePostData = new HashMap<>();

        try {
            parsePostData.putAll(HttpUtils.parseQueryString(ex.getRequestURI().getQuery()));

            // check if any postdata to parse
            parsePostData.putAll(HttpUtils.parsePostData(inBytes.length, is));
        } catch (IllegalArgumentException e) {
            // no postData - just reset inputstream
            newInput.reset();
        }
        final Map<String, String[]> postData = parsePostData;

        RequestWrapper req = new RequestWrapper(
                createUnimplementAdapter(HttpServletRequest.class), ex,
                postData, is);

        ResponseWrapper resp = new ResponseWrapper(
                createUnimplementAdapter(HttpServletResponse.class), ex);

        try {
            servlet.service(req, resp);
            resp.complete();
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    private static byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int r = in.read(buffer);
            if (r == -1)
                break;
            out.write(buffer, 0, r);
        }
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private static <T> T createUnimplementAdapter(Class<T> httpServletApi) {
        class UnimplementedHandler implements InvocationHandler {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                throw new UnsupportedOperationException("Not implemented: "
                                                                + method + ", args=" + Arrays.toString(args));
            }
        }

        return (T) Proxy.newProxyInstance(
                UnimplementedHandler.class.getClassLoader(),
                new Class<?>[] { httpServletApi },
                new UnimplementedHandler());
    }
}