package com.alibaba.alink.common.exceptions;

public class AkIllegalOperatorParameterException extends ExceptionWithErrorCode {
	public AkIllegalOperatorParameterException(String message) {
		super(ErrorCode.ILLEGAL_OPERATOR_PARAMETER, message);
	}

	public AkIllegalOperatorParameterException(String message, Throwable cause) {
		super(ErrorCode.ILLEGAL_OPERATOR_PARAMETER, message, cause);
	}
}
