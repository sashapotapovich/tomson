package com.server.server.servlet.wrapper;

import com.sun.net.httpserver.HttpExchange;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public final class ResponseWrapper extends HttpServletResponseWrapper {
	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//	final ServletOutputStream servletOutputStream = new ServletOutputStream() {
//		@Override
//		public void write(int b) throws IOException {
//			outputStream.write(b);
//		}
//	};

	private final HttpExchange ex;
	private final PrintWriter printWriter = null;
	private int status = HttpServletResponse.SC_OK;

	public ResponseWrapper(HttpServletResponse response, HttpExchange ex) {
		super(response);
		this.ex = ex;
//		printWriter = new PrintWriter(servletOutputStream);
	}

	@Override
	public void setContentType(String type) {
		ex.getResponseHeaders().add("Content-Type", type);
	}

	@Override
	public void setHeader(String name, String value) {
		ex.getResponseHeaders().add(name, value);
	}

//	@Override
//	public javax.servlet.ServletOutputStream getOutputStream() throws IOException {
//		return servletOutputStream;
//	}

	@Override
	public void setContentLength(int len) {
		ex.getResponseHeaders().add("Content-Length", len + "");
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		this.status = sc;
		if (msg != null) {
			printWriter.write(msg);
		}
	}

	@Override
	public void sendError(int sc) throws IOException {
		sendError(sc, null);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return printWriter;
	}

	public void complete() throws IOException {
		try {
			printWriter.flush();
			ex.sendResponseHeaders(status, outputStream.size());
			if (outputStream.size() > 0) {
				ex.getResponseBody().write(outputStream.toByteArray());
			}
			ex.getResponseBody().flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ex.close();
		}
	}
}