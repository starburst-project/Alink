package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.alink.server.domain.Edge;
import com.alibaba.alink.server.mapper.EdgeMapper;
import com.alibaba.alink.server.service.api.execution.ExperimentService;
import com.alibaba.alink.server.service.api.identifier.IdentifierGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Api
public class EdgeController {

	@Autowired
	ExperimentService experimentService;

	@Autowired
	EdgeMapper edgeMapper;

	@Autowired
	IdentifierGeneratorService identifierGeneratorService;

	/**
	 * Add an edge
	 */
	@ApiOperation(value = "Add edge of the graph.")
	@RequestMapping(
		value = "/api/v1/edge/add",
		method = RequestMethod.POST,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public AddEdgeResponse addEdge(@Valid @RequestBody EdgeDTO request) {
		experimentService.secureCheckAndGetExperiment(request.experimentId);
		Edge edge = new Edge();
		edge.setEdgeId(identifierGeneratorService.nextId());
		edge.setExperimentId(request.experimentId);
		edge.setSrcNodeId(request.srcNodeId);
		edge.setSrcNodePort(request.srcNodePort);
		edge.setDstNodeId(request.dstNodeId);
		edge.setDstNodePort(request.dstNodePort);

		edgeMapper.insertSelective(edge);
		return new AddEdgeResponse(edge.getEdgeId());
	}

	/**
	 * Delete an edge
	 *
	 * @param experimentId Experiment ID
	 * @param edgeId       Edge ID
	 */
	@ApiOperation(value = "Delete edge of the graph.")
	@RequestMapping(
		value = "/api/v1/edge/del",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse deleteEdge(@RequestParam(value = "experiment_id") Long experimentId,
									@RequestParam("edge_id") Long edgeId) {
		Edge edge = experimentService.secureCheckAndGetEdge(experimentId, edgeId);
		edgeMapper.deleteByPrimaryKey(edge.getEdgeId());
		return BasicResponse.success();
	}

	/**
	 * Request for add an edge
	 */
	static class EdgeDTO {
		/**
		 * Experiment ID
		 */
		@NotNull
		public Long experimentId;

		/**
		 * Source node ID
		 */
		@NotNull
		public Long srcNodeId;

		/**
		 * Source node port
		 */
		@NotNull
		public Short srcNodePort;

		/**
		 * Destination node ID
		 */
		@NotNull
		public Long dstNodeId;

		/**
		 * Destination node port
		 */
		@NotNull
		public Short dstNodePort;
	}

	static class AddEdgeResponse extends BasicResponse {
		/**
		 * data
		 */
		public DataT data = new DataT();

		public AddEdgeResponse(Long id) {
			super(true);
			this.data.id = id;
		}

		@ApiModel(value = "AddEdgeResponseDataT")
		static class DataT {
			/**
			 * Edge ID
			 */
			public Long id;
		}
	}
}
