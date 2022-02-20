package com.alibaba.alink.server.excpetion;

/**
 * https://umijs.org/zh-CN/plugins/plugin-request
 */
public enum ErrorShowType {

	SILENT(0),
	WARN_MESSAGE(1),
	ERROR_MESSAGE(2),
	NOTIFICATION(4),
	REDIRECT(9);

	private final int type;

	ErrorShowType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}

}
