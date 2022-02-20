package com.alibaba.alink.server.excpetion;

import com.alibaba.alink.server.controller.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	void filterStackTrace(Exception ex) {
		ex.setStackTrace(
			Arrays.stream(ex.getStackTrace())
				.filter(d -> d.getClassName().startsWith("com.alibaba.alink"))
				.toArray(StackTraceElement[]::new)
		);
	}

	@ExceptionHandler(Exception.class)
	public BasicResponse handleException(HttpServletRequest req, Exception ex) {
		LOG.error("Exception host: {} url: {} error: {}", req.getRemoteHost(), req.getRequestURL(), ex);
		filterStackTrace(ex);
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer, true));
		return new BasicResponse(false, "alink-1000", writer.toString());
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class, IllegalArgumentException.class})
	public BasicResponse methodValidException(HttpServletRequest req, Exception ex) {
		LOG.warn("Argument validation error host: {} url: {} error: {}",
			req.getRemoteHost(), req.getRequestURL(), ex);
		filterStackTrace(ex);
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer, true));
		return new BasicResponse(false, "alink-1001", writer.toString());
	}
}
