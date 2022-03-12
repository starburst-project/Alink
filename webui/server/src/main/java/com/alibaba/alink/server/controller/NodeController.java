package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.alink.server.domain.Experiment;
import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.mapper.NodeMapper;
import com.alibaba.alink.server.service.api.execution.ExperimentService;
import com.alibaba.alink.server.service.api.identifier.IdentifierGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@Api
public class NodeController {

	@Autowired
	ExperimentService experimentService;

	@Autowired
	NodeMapper nodeMapper;

	@Autowired
	IdentifierGeneratorService identifierGeneratorService;

	/**
	 * Add a node
	 */
	@ApiOperation(value = "Add node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/add",
		method = RequestMethod.POST,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public AddNodeResponse addNode(@Valid @RequestBody NodeDTO request) {
		Experiment experiment = experimentService.secureCheckAndGetExperiment(request.experimentId);
		Node node = new Node();
		node.setNodeId(identifierGeneratorService.nextId());
		node.setType(request.nodeType.getType());
		node.setName(request.nodeName);
		node.setPositionX(request.positionX);
		node.setPositionY(request.positionY);
		node.setClassName(request.className);
		node.setExperimentId(experiment.getExperimentId());
		nodeMapper.insertSelective(node);
		return new AddNodeResponse(node.getNodeId());
	}

	/**
	 * Delete a node
	 *
	 * @param experimentId Experiment ID
	 * @param nodeId       Node ID
	 */
	@ApiOperation(value = "Delete node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/del",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse deleteNode(@RequestParam(value = "experiment_id") Long experimentId,
									@RequestParam("node_id") Long nodeId) {
		Node node = experimentService.secureCheckAndGetNode(experimentId, nodeId);
		nodeMapper.deleteByPrimaryKey(node.getNodeId());
		return BasicResponse.success();
	}

	/**
	 * Update the coordinates or name of a node
	 *
	 * @param experimentId Experiment ID
	 * @param nodeId       Node ID
	 * @param name         name
	 * @param x            x coordinate
	 * @param y            y coordinate
	 */
	@ApiOperation(value = "Update the node in the graph.")
	@RequestMapping(
		value = "/api/v1/node/update",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse updateNode(@RequestParam(value = "experiment_id") Long experimentId,
									@RequestParam("node_id") Long nodeId,
									@Nullable @RequestParam(value = "name", required = false) String name,
									@Nullable @RequestParam(value = "position_x", required = false) Double x,
									@Nullable @RequestParam(value = "position_y", required = false) Double y) {
		Node node = experimentService.secureCheckAndGetNode(experimentId, nodeId);
		if (null != name) {
			node.setName(name);
		}
		if (null != x) {
			node.setPositionX(x);
		}
		if (null != y) {
			node.setPositionY(y);
		}
		nodeMapper.updateByPrimaryKey(node);
		return BasicResponse.success();
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
	public GetNodeResponse getNode(@RequestParam(value = "experiment_id") Long experimentId,
								   @RequestParam("node_id") Long nodeId) {
		Node node = experimentService.secureCheckAndGetNode(experimentId, nodeId);
		return new GetNodeResponse(new NodeDTO(node));
	}

	static class NodeDTO {
		/**
		 * Experiment ID
		 */
		@NotNull
		public Long experimentId;

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

		public NodeDTO() {
		}

		public NodeDTO(Node node) {
			this.experimentId = node.getExperimentId();
			this.nodeType = NodeType.values()[node.getType()];
			this.nodeName = node.getName();
			this.positionX = node.getPositionX();
			this.positionY = node.getPositionY();
			this.className = node.getClassName();
		}

		public enum NodeType {
			SOURCE((short) 0),
			FUNCTION((short) 1),
			SINK((short) 2);

			private final Short type;

			NodeType(Short type) {
				this.type = type;
			}

			public Short getType() {
				return type;
			}
		}
	}

	static class AddNodeResponse extends BasicResponse {
		/**
		 * data
		 */
		public DataT data = new DataT();

		public AddNodeResponse(Long id) {
			super(true);
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

		public GetNodeResponse(NodeDTO node) {
			super(true);
			this.data.node = node;
		}

		@ApiModel(value = "GetNodeResponseDataT")
		static class DataT {
			/**
			 * Node
			 */
			public NodeDTO node;
		}
	}
}
