package com.alibaba.alink.server.controller;

import javax.validation.constraints.NotNull;

import com.alibaba.alink.server.excpetion.ErrorShowType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * https://umijs.org/zh-CN/plugins/plugin-request
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse implements Serializable {

	@NotNull
	private final boolean success;

	private final String errorCode;

	private final String errorMessage;

	private final Integer showType;

	private final String traceId;

	private final String host;

	public BasicResponse(boolean success) {
		this(success, null, null);
	}

	public BasicResponse(boolean success, String errorCode, String errorMessage) {
		this(success, errorCode, errorMessage, null, null, null);
	}

	public BasicResponse(
		boolean success, String errorCode, String errorMessage,
		Integer showType, String traceId, String host) {

		this.success = success;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.showType = showType;
		this.traceId = traceId;
		this.host = host;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Integer getShowType() {
		return showType;
	}

	public String getTraceId() {
		return traceId;
	}

	public String getHost() {
		return host;
	}

	public static BasicResponse success() {
		return new BasicResponse(true);
	}
}
