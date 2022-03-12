package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.alink.server.domain.Edge;
import com.alibaba.alink.server.domain.Experiment;
import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.mapper.EdgeMapper;
import com.alibaba.alink.server.mapper.ExperimentMapper;
import com.alibaba.alink.server.mapper.NodeMapper;
import com.alibaba.alink.server.mapper.NodeParamMapper;
import com.alibaba.alink.server.service.api.execution.ExperimentService;
import com.alibaba.alink.server.service.api.identifier.IdentifierGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Api
public class ExperimentController {

	private final static Logger LOG = LoggerFactory.getLogger(ExperimentController.class);

	@Autowired
	NodeMapper nodeMapper;

	@Autowired
	EdgeMapper edgeMapper;

	@Autowired
	ExperimentMapper experimentMapper;

	@Autowired
	NodeParamMapper nodeParamMapper;

	@Autowired
	ExperimentService experimentService;

	@Autowired
	IdentifierGeneratorService identifierGeneratorService;

	@ApiOperation(value = "Add a experiment.")
	@PostMapping(
		value = "/api/v1/experiment/add",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public AddExperimentResponse addExperiment(@RequestBody Experiment experiment) {
		experiment.setExperimentId(identifierGeneratorService.nextId());
		experimentMapper.insert(experiment);
		return new AddExperimentResponse(experiment.getExperimentId());
	}

	@ApiOperation(value = "Delete the experiment by experimentId.")
	@GetMapping(
		value = "/api/v1/experiment/del",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse deleteExperiment(@RequestParam(value = "experiment_id") Long experimentId) {
		experimentMapper.deleteByPrimaryKey(experimentId);

		return BasicResponse.success();
	}

	@ApiOperation(value = "Update the experiment.")
	@GetMapping(
		value = "/api/v1/experiment/update",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse updateExperiment(Experiment experiment) {
		experimentMapper.updateByPrimaryKey(experiment);

		return BasicResponse.success();
	}

	@ApiOperation(value = "Get the experiment and its configuration.")
	@GetMapping(
		value = "/api/v1/experiment/get",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public GetExperimentResponse getExperiment(@RequestParam(value = "experiment_id") Long experimentId) {
		Experiment experiment = experimentMapper.selectByPrimaryKey(experimentId);
		return new GetExperimentResponse(experiment);
	}

	/**
	 * Get content of an experiment
	 *
	 * @param experimentId Experiment ID
	 * @return
	 */
	@ApiOperation(value = "Get the entire graph.")
	@GetMapping(
		value = "/api/v1/experiment/get_graph",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public GetExperimentGraphResponse getExperimentContent(
		@RequestParam(value = "experiment_id") Long experimentId) {

		Experiment experiment = experimentService.secureCheckAndGetExperiment(experimentId);

		List<Node> nodes = nodeMapper.selectByExperimentId(experiment.getExperimentId());
		List<Edge> edges = edgeMapper.selectByExperimentId(experiment.getExperimentId());

		Set<Long> nodeIdSet = new HashSet<>();
		for (Node node : nodes) {
			nodeIdSet.add(node.getNodeId());
		}
		List<Edge> toDeleteEdges = new ArrayList<>();
		for (Edge edge : edges) {
			if (!nodeIdSet.contains(edge.getSrcNodeId()) || !nodeIdSet.contains(edge.getDstNodeId())) {
				toDeleteEdges.add(edge);
				LOG.info(String.format("About to delete invalid edge (%d.%d) -> (%d.%d)",
					edge.getSrcNodeId(), edge.getSrcNodePort(),
					edge.getDstNodeId(), edge.getDstNodePort())
				);
			}
		}

		if (toDeleteEdges.size() > 0) {
			for (Edge edge : toDeleteEdges) {
				edgeMapper.deleteByPrimaryKey(edge.getEdgeId());
			}
			edges = edgeMapper.selectByExperimentId(experiment.getExperimentId());
		}

		return new GetExperimentGraphResponse(nodes, edges);
	}

	/**
	 * Export PyAlink script
	 *
	 * @param experimentId Experiment ID
	 */
	@ApiOperation(value = "Export the graph to the pyalink script.")
	@GetMapping(
		value = "/api/v1/experiment/export_pyalink_script",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public ExportPyAlinkScriptResponse exportPyAlinkScript(
		@RequestParam(value = "experiment_id") Long experimentId) throws ClassNotFoundException {
		List<String> lines = experimentService.exportScripts(experimentId);
		return new ExportPyAlinkScriptResponse(lines);
	}

	/**
	 * Run an experiment
	 *
	 * @param experimentId Experiment ID
	 */
	@ApiOperation(value = "Run the experiment.")
	@GetMapping(
		value = "/api/v1/experiment/run",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse runExperiment(@RequestParam(value = "experiment_id") Long experimentId)
		throws Exception {
		experimentService.runExperiment(experimentId);
		return BasicResponse.success();
	}

	public static class GetExperimentGraphResponse extends BasicResponse {
		public DataT data = new DataT();

		public GetExperimentGraphResponse(List<Node> nodes, List<Edge> edges) {
			super(true);
			this.data.nodes = nodes;
			this.data.edges = edges;
		}

		@ApiModel(value = "GetExperimentGraphResponseDataT")
		static class DataT {
			/**
			 * Node list
			 */
			public List<Node> nodes;

			/**
			 * Edge list
			 */
			public List<Edge> edges;
		}
	}

	public static class ExportPyAlinkScriptResponse extends BasicResponse {
		public DataT data = new DataT();

		public ExportPyAlinkScriptResponse(List<String> lines) {
			super(true);
			this.data.lines = lines;
		}

		@ApiModel(value = "ExportPyAlinkScriptResponseDataT")
		static class DataT {
			/**
			 * Code lines
			 */
			public List<String> lines;
		}
	}

	public static class GetExperimentResponse extends BasicResponse {
		public DataT data = new DataT();

		public GetExperimentResponse(Experiment experiment) {
			super(true);
			this.data.experiment = experiment;
		}

		@ApiModel(value = "GetExperimentResponseDataT")
		static class DataT {
			/**
			 * Experiment
			 */
			public Experiment experiment;
		}
	}

	static class AddExperimentResponse extends BasicResponse {
		/**
		 * data
		 */
		public DataT data = new DataT();

		public AddExperimentResponse(Long experimentId) {
			super(true);
			this.data.experimentId = experimentId;
		}

		@ApiModel(value = "AddExperimentResponseDataT")
		static class DataT {
			/**
			 * Node ID
			 */
			public Long experimentId;
		}
	}
}
