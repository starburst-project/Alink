package com.alibaba.alink.server.excpetion;

public class InvalidExperimentIdException extends IllegalArgumentException {

	public InvalidExperimentIdException(Long experimentId) {
		this(experimentId, null);
	}

	public InvalidExperimentIdException(Long experimentId, String message) {
		super(
			String.format("Experiment %d is not exists.%s", experimentId, message == null ? "" : " Reason: " + message)
		);
	}
}
