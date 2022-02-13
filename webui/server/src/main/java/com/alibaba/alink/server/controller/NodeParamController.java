package com.alibaba.alink.server.controller;

import com.alibaba.alink.server.domain.NodeParam;
import com.alibaba.alink.server.repository.NodeParamRepository;
import com.alibaba.alink.server.service.ExperimentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.Map.Entry;

@RestController
@Api
public class NodeParamController {

	@Autowired
	ExperimentService experimentService;

	@Autowired
	NodeParamRepository nodeParamRepository;

	/**
	 * Update parameters of a node
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "Update the node's param.")
	@RequestMapping(
		value = "/api/v1/param/update",
		method = RequestMethod.POST,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@Transactional
	public BasicResponse updateParam(@Valid @RequestBody NodeParamController.UpdateParamRequest request) {
		experimentService.secureGetNode(request.experimentId, request.nodeId);
		List<NodeParam> nodeParamList = nodeParamRepository
			.findByExperimentIdAndNodeId(request.experimentId, request.nodeId);

		Map<String, NodeParam> key2NodeParam = new HashMap<>();
		for (NodeParam nodeParam : nodeParamList) {
			key2NodeParam.put(nodeParam.getKey(), nodeParam);
		}

		HashSet<Long> toRemoveIdSet = new HashSet<>();
		for (NodeParam nodeParam : nodeParamList) {
			String key = nodeParam.getKey();
			if (request.paramsToDel.contains(key)) {
				toRemoveIdSet.add(nodeParam.getId());
			}
		}

		List<NodeParam> toUpdateList = new ArrayList<>();
		for (Entry<String, String> entry : request.paramsToUpdate.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null || StringUtils.isEmpty(value)) {
				// Note that `value` should be after jsonized.
				// So if `value` is an empty string, we treat `key` as "not set" and remove it.
				if (key2NodeParam.containsKey(key)) {
					NodeParam nodeParam = key2NodeParam.get(key);
					toRemoveIdSet.add(nodeParam.getId());
				}
				continue;
			}
			NodeParam nodeParam;
			if (key2NodeParam.containsKey(key)) {
				nodeParam = key2NodeParam.get(key);
				toRemoveIdSet.remove(nodeParam.getId());
			} else {
				nodeParam = new NodeParam();
				nodeParam.setExperimentId(request.experimentId);
				nodeParam.setNodeId(request.nodeId);
				nodeParam.setKey(key);
			}
			nodeParam.setValue(value);
			toUpdateList.add(nodeParam);
		}

		nodeParamRepository.saveAllAndFlush(toUpdateList);
		nodeParamRepository.deleteAllById(toRemoveIdSet);

		return BasicResponse.OK();
	}

	/**
	 * Get parameters of a node
	 *
	 * @param experimentId experiment ID
	 * @param nodeId       node ID
	 * @return
	 */
	@ApiOperation(value = "Get the node's params.")
	@RequestMapping(
		value = "/api/v1/param/get",
		method = RequestMethod.GET,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public GetNodeParamResponse getNodeParam(@RequestParam(value = "experiment_id", defaultValue = "1") Long experimentId,
											 @RequestParam("node_id") Long nodeId) {
		experimentService.secureGetNode(experimentId, nodeId);
		List<NodeParam> nodeParams = nodeParamRepository.findByExperimentIdAndNodeId(experimentId, nodeId);
		return new GetNodeParamResponse(nodeParams);
	}

	static class UpdateParamRequest {
		/**
		 * Experiment ID
		 */
		public Long experimentId = 1L;
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
			super("OK");
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
