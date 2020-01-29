package com.server.server.servlet.wrapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Slf4j
public class HttpHandlerWithServletSupport implements HttpHandler {
	private HttpServlet servlet;

	public HttpHandlerWithServletSupport(HttpServlet servlet) {
		this.servlet = servlet;
	}

	@Override
	public void handle(final HttpExchange ex) throws IOException {
//		byte[] inBytes = getBytes(ex.getRequestBody());
//		ex.getRequestBody().close();
//		final ByteArrayInputStream newInput = new ByteArrayInputStream(inBytes);
//		final ServletInputStream is = new ServletInputStream() {
//			@Override
//			public int read() throws IOException {
//				return newInput.read();
//			}
//		};
//
//		Map<String, String[]> parsePostData = new HashMap<>();
//
//		try {
//			parsePostData.putAll(HttpUtils.parseQueryString(ex.getRequestURI().getQuery()));
//
//			// check if any postdata to parse
//			parsePostData.putAll(HttpUtils.parsePostData(inBytes.length, is));
//		} catch (IllegalArgumentException e) {
//			// no postData - just reset inputstream
//			newInput.reset();
//		}
//		final Map<String, String[]> postData = parsePostData;
//
//		RequestWrapper req = new RequestWrapper(createUnimplementAdapter(HttpServletRequest.class), ex, postData, is);
//
//		ResponseWrapper resp = new ResponseWrapper(createUnimplementAdapter(HttpServletResponse.class), ex);
//
//		try {
//			System.out.println("PRE SERVICE");
//			servlet.service(req, resp);
//			System.out.println("POST SERVICE");
//			resp.complete();
//		} catch (ServletException e) {
//			throw new IOException(e);
//		}
		log.info(ex.getRequestMethod());
		ex.sendResponseHeaders(200, 0);
		OutputStream responseBody = ex.getResponseBody();
		ex.setStreams(null, responseBody);
		responseBody.write("Hi There!".getBytes());
		responseBody.flush();
		responseBody.close();
	}

	private static byte[] getBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (true) {
			int r = in.read(buffer);
			if (r == -1) {
				break;
			}
			out.write(buffer, 0, r);
		}
		return out.toByteArray();
	}

	@SuppressWarnings("unchecked")
	private static <T> T createUnimplementAdapter(Class<T> httpServletApi) {
		class UnimplementedHandler implements InvocationHandler {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				throw new UnsupportedOperationException("Not implemented: " + method + ", args=" + Arrays.toString(args));
			}
		}

		return (T) Proxy.newProxyInstance(UnimplementedHandler.class.getClassLoader(), new Class<?>[] {httpServletApi}, new UnimplementedHandler());
	}
}