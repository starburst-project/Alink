package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.alink.server.domain.Node;
import com.alibaba.alink.server.domain.NodeParam;
import com.alibaba.alink.server.mapper.NodeParamMapper;
import com.alibaba.alink.server.service.api.execution.ExperimentService;
import com.alibaba.alink.server.service.api.identifier.IdentifierGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RestController
@Api
public class NodeParamController {

	@Autowired
	ExperimentService experimentService;

	@Autowired
	NodeParamMapper nodeParamMapper;

	@Autowired
	IdentifierGeneratorService identifierGeneratorService;

	/**
	 * Update parameters of a node
	 */
	@ApiOperation(value = "Update the node's param.")
	@PostMapping(
		value = "/api/v1/param/update",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse updateParam(@Valid @RequestBody UpdateNodeParamDTO request) {
		experimentService.secureCheckAndGetNode(request.experimentId, request.nodeId);
		List<NodeParam> nodeParamList = nodeParamMapper
			.selectByExperimentIdAndNodeId(request.experimentId, request.nodeId);

		Map<String, NodeParam> key2NodeParam = new HashMap<>();
		for (NodeParam nodeParam : nodeParamList) {
			key2NodeParam.put(nodeParam.getKey(), nodeParam);
		}

		HashSet<Long> toRemoveIdSet = new HashSet<>();
		for (NodeParam nodeParam : nodeParamList) {
			String key = nodeParam.getKey();
			if (request.paramsToDel.contains(key)) {
				toRemoveIdSet.add(nodeParam.getNodeParamId());
			}
		}

		List<NodeParam> toUpdateList = new ArrayList<>();
		List<NodeParam> toInsertList = new ArrayList<>();

		for (Entry<String, String> entry : request.paramsToUpdate.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null || StringUtils.isEmpty(value)) {
				// Note that `value` should be after jsonized.
				// So if `value` is an empty string, we treat `key` as "not set" and remove it.
				if (key2NodeParam.containsKey(key)) {
					NodeParam nodeParam = key2NodeParam.get(key);
					toRemoveIdSet.add(nodeParam.getNodeParamId());
				}
				continue;
			}
			NodeParam nodeParam;
			if (key2NodeParam.containsKey(key)) {
				nodeParam = key2NodeParam.get(key);
				toRemoveIdSet.remove(nodeParam.getNodeParamId());
				nodeParam.setValue(value);
				toUpdateList.add(nodeParam);
			} else {
				nodeParam = new NodeParam();
				nodeParam.setNodeParamId(identifierGeneratorService.nextId());
				nodeParam.setExperimentId(request.experimentId);
				nodeParam.setNodeId(request.nodeId);
				nodeParam.setKey(key);
				nodeParam.setValue(value);
				toInsertList.add(nodeParam);
			}
		}

		for (NodeParam nodeParam : toInsertList) {
			nodeParamMapper.insertSelective(nodeParam);
		}

		for (NodeParam nodeParam : toUpdateList) {
			nodeParamMapper.updateByPrimaryKeySelective(nodeParam);
		}

		for (Long nodeParamId : toRemoveIdSet) {
			nodeParamMapper.deleteByPrimaryKey(nodeParamId);
		}

		return BasicResponse.success();
	}

	/**
	 * Get parameters of a node
	 *
	 * @param experimentId experiment ID
	 * @param nodeId       node ID
	 */
	@ApiOperation(value = "Get the node's params.")
	@GetMapping(
		value = "/api/v1/param/get",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public GetNodeParamResponse getNodeParam(@RequestParam(value = "experiment_id") Long experimentId,
											 @RequestParam("node_id") Long nodeId) {
		Node node = experimentService.secureCheckAndGetNode(experimentId, nodeId);
		List<NodeParam> nodeParams = nodeParamMapper.selectByExperimentIdAndNodeId(
			node.getExperimentId(), node.getNodeId()
		);
		return new GetNodeParamResponse(nodeParams);
	}

	static class UpdateNodeParamDTO {
		/**
		 * Experiment ID
		 */
		@NotNull
		public Long experimentId;

		/**
		 * Node ID
		 */
		@NotNull
		public Long nodeId;

		/**
		 * Node parameters need to update
		 */
		@NotNull
		public Map<String, String> paramsToUpdate;

		/**
		 * Node parameters to delete
		 */
		@NotNull
		public Set<String> paramsToDel;
	}

	static class GetNodeParamResponse extends BasicResponse {
		public DataT data = new DataT();

		GetNodeParamResponse(List<NodeParam> L) {
			super(true);
			this.data.parameters = L;
		}

		@ApiModel(value = "GetNodeParamResponseDataT")
		static class DataT {
			/**
			 * parameter list
			 */
			public List<NodeParam> parameters;
		}
	}
}
