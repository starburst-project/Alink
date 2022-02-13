package com.alibaba.alink.server.controller;

import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.domain.NodeType;
import com.alibaba.alink.server.repository.NodeRepository;
import com.alibaba.alink.server.service.ExperimentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@Api
public class NodeController {
	private final static Logger LOG = LoggerFactory.getLogger(NodeController.class);

	@Autowired
	ExperimentService experimentService;

	@Autowired
	NodeRepository nodeRepository;

	/**
	 * Add a node
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "Add node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/add",
		method = RequestMethod.POST,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public AddNodeResponse addNode(@Valid @RequestBody AddNodeRequest request) {
		experimentService.checkExperimentId(request.experimentId);
		Node node = new Node();
		node.setType(request.nodeType);
		node.setName(request.nodeName);
		node.setPositionX(request.positionX);
		node.setPositionY(request.positionY);
		node.setClassName(request.className);
		node.setExperimentId(request.experimentId);
		node = nodeRepository.saveAndFlush(node);
		return new AddNodeResponse(node.getId());
	}

	/**
	 * Delete a node
	 *
	 * @param experimentId Experiment ID
	 * @param nodeId       Node ID
	 * @return
	 */
	@ApiOperation(value = "Delete node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/del",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse deleteNode(@RequestParam(value = "experiment_id", defaultValue = "1") Long experimentId,
									@RequestParam("node_id") Long nodeId) {
		Node node = experimentService.secureGetNode(experimentId, nodeId);
		nodeRepository.delete(node);
		return BasicResponse.OK();
	}

	/**
	 * Update the coordinates or name of a node
	 *
	 * @param experimentId Experiment ID
	 * @param nodeId       Node ID
	 * @param name         name
	 * @param x            x coordinate
	 * @param y            y coordinate
	 * @return
	 */
	@ApiOperation(value = "Update the node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/update",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse updateNode(@RequestParam(value = "experiment_id", defaultValue = "1") Long experimentId,
									@RequestParam("node_id") Long nodeId,
									@Nullable @RequestParam(value = "name", required = false) String name,
									@Nullable @RequestParam(value = "position_x", required = false) Double x,
									@Nullable @RequestParam(value = "position_y", required = false) Double y) {
		Node node = experimentService.secureGetNode(experimentId, nodeId);
		if (null != name) {
			node.setName(name);
		}
		if (null != x) {
			node.setPositionX(x);
		}
		if (null != y) {
			node.setPositionY(y);
		}
		nodeRepository.saveAndFlush(node);
		return BasicResponse.OK();
	}

	/**
	 * Update the coordinates or name of a node
	 *
	 * @param experimentId Experiment ID
	 * @param nodeId       Node ID
	 * @return
	 */
	@ApiOperation(value = "Get the node information in the graph.")
	@RequestMapping(
		value = "/api/v1/node/get",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public GetNodeResponse getNode(@RequestParam(value = "experiment_id", defaultValue = "1") Long experimentId,
								   @RequestParam("node_id") Long nodeId) {
		Node node = experimentService.secureGetNode(experimentId, nodeId);
		return new GetNodeResponse(node);
	}

	static class AddNodeRequest {
		/**
		 * Experiment ID
		 */
		public Long experimentId = 1L;

		/**
		 * Node type
		 */
		@NotNull
		public NodeType nodeType;

		/**
		 * Node name
		 */
		@NotNull
		@Size(min = 1, max = 255)
		public String nodeName;

		/**
		 * X coordinate of the position
		 */
		@NotNull
		public Double positionX;

		/**
		 * Y coordinate of the position
		 */
		@NotNull
		public Double positionY;

		/**
		 * Algorithm class name
		 */
		@NotNull
		@Size(min = 1, max = 255)
		public String className;
	}

	static class AddNodeResponse extends BasicResponse {
		/**
		 * data
		 */
		public DataT data = new DataT();

		public AddNodeResponse(Long id) {
			super("OK");
			this.data.id = id;
		}

		@ApiModel(value = "AddNodeResponseDataT")
		static class DataT {
			/**
			 * Node ID
			 */
			public Long id;
		}
	}

	static class GetNodeResponse extends BasicResponse {
		/**
		 * data
		 */
		public DataT data = new DataT();

		public GetNodeResponse(Node node) {
			super("OK");
			this.data.node = node;
		}

		@ApiModel(value = "GetNodeResponseDataT")
		static class DataT {
			/**
			 * Node
			 */
			public Node node;
		}
	}
}
