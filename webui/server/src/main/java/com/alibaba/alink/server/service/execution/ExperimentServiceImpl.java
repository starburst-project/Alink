package com.alibaba.alink.server.service.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.alink.server.common.ExportScriptUtils;
import com.alibaba.alink.server.domain.Edge;
import com.alibaba.alink.server.domain.Experiment;
import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.domain.NodeParam;
import com.alibaba.alink.server.excpetion.InvalidEdgeIdException;
import com.alibaba.alink.server.excpetion.InvalidExperimentIdException;
import com.alibaba.alink.server.excpetion.InvalidNodeIdException;
import com.alibaba.alink.server.mapper.EdgeMapper;
import com.alibaba.alink.server.mapper.ExperimentMapper;
import com.alibaba.alink.server.mapper.NodeMapper;
import com.alibaba.alink.server.mapper.NodeParamMapper;
import com.alibaba.alink.server.service.api.execution.ExecutionService;
import com.alibaba.alink.server.service.api.execution.ExperimentService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class ExperimentServiceImpl implements ExperimentService {

	@Autowired
	NodeMapper nodeMapper;

	@Autowired
	EdgeMapper edgeMapper;

	@Autowired
	NodeParamMapper nodeParamMapper;

	@Autowired
	ExperimentMapper experimentMapper;

	@Autowired
	ExecutionService executionService;

	@Override
	public Experiment secureCheckAndGetExperiment(Long experimentId) throws InvalidExperimentIdException {
		Experiment experiment = experimentMapper.selectByPrimaryKey(experimentId);

		if (experiment == null) {
			throw new InvalidExperimentIdException(experimentId);
		}

		return experiment;
	}

	@Override
	public Node secureCheckAndGetNode(Long experimentId, Long nodeId)
		throws InvalidExperimentIdException, InvalidNodeIdException {

		Experiment experiment = secureCheckAndGetExperiment(experimentId);

		Node node = nodeMapper.selectByPrimaryKey(nodeId);

		if (node == null) {
			throw new InvalidNodeIdException(nodeId);
		}

		if (!node.getExperimentId().equals(experiment.getExperimentId())) {
			throw new IllegalArgumentException(
				String.format("Node [%d] not belongs to Experiment [%d]!", nodeId, experimentId));
		}
		return node;
	}

	@Override
	public Edge secureCheckAndGetEdge(Long experimentId, Long edgeId)
		throws InvalidExperimentIdException, InvalidEdgeIdException {

		Edge edge = edgeMapper.selectByPrimaryKey(edgeId);

		if (edge == null) {
			throw new InvalidEdgeIdException(edgeId);
		}

		Experiment experiment = secureCheckAndGetExperiment(experimentId);

		if (!edge.getExperimentId().equals(experiment.getExperimentId())) {
			throw new IllegalArgumentException(
				String.format("Edge [%d] not belongs to Experiment [%d]!", edgeId, experimentId));
		}

		return edge;
	}

	@Override
	public void runExperiment(Long experimentId) throws Exception {
		Experiment experiment = secureCheckAndGetExperiment(experimentId);

		List <Node> nodes = nodeMapper.selectByExperimentId(experimentId);
		List <Edge> edges = edgeMapper.selectByExperimentId(experimentId);
		List <NodeParam> nodeParams = nodeParamMapper.selectByExperimentId(experimentId);

		String configStr = experiment.getConfig();
		if (StringUtils.isBlank(configStr)) {
			configStr = "{}";
		}
		Map <String, String> config = new Gson().fromJson(configStr,
			new TypeToken <Map <String, String>>() {}.getType());
		executionService.run(nodes, edges, nodeParams, config);
	}

	@Override
	public List <String> exportScripts(Long experimentId) throws ClassNotFoundException {
		Experiment experiment = secureCheckAndGetExperiment(experimentId);

		List <Node> nodes = nodeMapper.selectByExperimentId(experimentId);
		List <Edge> edges = edgeMapper.selectByExperimentId(experimentId);
		List <NodeParam> nodeParams = nodeParamMapper.selectByExperimentId(experimentId);

		String configStr = experiment.getConfig();
		if (StringUtils.isBlank(configStr)) {
			configStr = "{}";
		}
		Map <String, String> config = new Gson().fromJson(configStr,
			new TypeToken <Map <String, String>>() {}.getType());

		List <String> lines = executionService.getScriptHeader(config);
		lines.add("\n");
		lines.addAll(ExportScriptUtils.generateDAGScript(nodes, edges, nodeParams));
		return lines;
	}
}
