package com.alibaba.alink.server.controller;

import com.alibaba.alink.server.domain.Edge;
import com.alibaba.alink.server.repository.EdgeRepository;
import com.alibaba.alink.server.service.ExperimentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Api
public class EdgeController {

	@Autowired
	ExperimentService experimentService;

	@Autowired
	EdgeRepository edgeRepository;

	/**
	 * Add an edge
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "Add edge of the graph.")
	@RequestMapping(
		value = "/api/v1/edge/add",
		method = RequestMethod.POST,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public AddEdgeResponse addEdge(@Valid @RequestBody AddEdgeRequest request) {
		experimentService.checkExperimentId(request.experimentId);
		Edge edge = new Edge();
		edge.setExperimentId(request.experimentId);
		edge.setSrcNodeId(request.srcNodeId);
		edge.setSrcNodePort(request.srcNodePort);
		edge.setDstNodeId(request.dstNodeId);
		edge.setDstNodePort(request.dstNodePort);
		edge = edgeRepository.saveAndFlush(edge);
		return new AddEdgeResponse(edge.getId());
	}

	/**
	 * Delete an edge
	 *
	 * @param experimentId Experiment ID
	 * @param edgeId       Edge ID
	 * @return
	 */
	@ApiOperation(value = "Delete edge of the graph.")
	@RequestMapping(
		value = "/api/v1/edge/del",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse deleteEdge(@RequestParam(value = "experiment_id", defaultValue = "1") Long experimentId,
									@RequestParam("edge_id") Long edgeId) {
		Edge edge = experimentService.secureGetEdge(experimentId, edgeId);
		edgeRepository.delete(edge);
		return BasicResponse.success();
	}

	/**
	 * Request for add an edge
	 */
	static class AddEdgeRequest {
		/**
		 * Experiment ID
		 */
		public Long experimentId = 1L;

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
