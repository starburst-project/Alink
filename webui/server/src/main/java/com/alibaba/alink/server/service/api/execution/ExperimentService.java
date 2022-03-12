package com.alibaba.alink.server.service.api.execution;

import com.alibaba.alink.server.domain.Edge;
import com.alibaba.alink.server.domain.Experiment;
import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.excpetion.InvalidExperimentIdException;
import com.alibaba.alink.server.excpetion.InvalidEdgeIdException;
import com.alibaba.alink.server.excpetion.InvalidNodeIdException;

import java.util.List;

public interface ExperimentService {

	Experiment secureCheckAndGetExperiment(Long experimentId) throws InvalidExperimentIdException;

	Node secureCheckAndGetNode(Long experimentId, Long nodeId)
		throws InvalidExperimentIdException, InvalidNodeIdException;

	Edge secureCheckAndGetEdge(Long experimentId, Long edgeId)
		throws InvalidExperimentIdException, InvalidEdgeIdException;

	void runExperiment(Long experimentId) throws Exception;

	List <String> exportScripts(Long experimentId) throws ClassNotFoundException;
}
