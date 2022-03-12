package com.alibaba.alink.server.service.api.identifier;

public interface MachineIdentifierService {
	long nextId();

	String format(int identifier);
}
