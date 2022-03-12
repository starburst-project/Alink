package com.alibaba.alink.server.service.api.identifier;

public interface IdentifierGeneratorService {
	long nextId();

	String formatId(long id);
}
