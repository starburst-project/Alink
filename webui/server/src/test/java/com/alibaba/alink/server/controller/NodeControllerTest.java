package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.alibaba.alink.server.controller.ExperimentController.GetExperimentGraphResponse;
import com.alibaba.alink.server.controller.NodeController.AddNodeResponse;
import com.alibaba.alink.server.controller.NodeController.NodeDTO;
import com.alibaba.alink.server.domain.Node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
class NodeControllerTest {
	protected Gson gson = new GsonBuilder()
		.disableHtmlEscaping()
		.setPrettyPrinting()
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
		.create();

	@Autowired
	protected MockMvc mvc;

	@Test
	public void addNode() throws Exception {
		NodeDTO req = new NodeDTO();
		req.experimentId = 1L;
		req.nodeName = "shuffle";
		req.positionX = 100.;
		req.positionY = 200.;
		req.className = "com.alibaba.alink.ShuffleBatchOp";
		req.nodeType = NodeDTO.NodeType.FUNCTION;
		mvc.perform(post("/api/v1/node/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(req)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.id").isNumber());
	}

	@Test
	public void deleteNode() throws Exception {
		NodeDTO req = new NodeDTO();
		req.experimentId = 1L;
		req.nodeName = "abc";
		req.positionX = 100.;
		req.positionY = 200.;
		req.className = "com.alibaba.alink.ShuffleBatchOp";
		req.nodeType = NodeDTO.NodeType.FUNCTION;
		MvcResult mvcResult = mvc.perform(post("/api/v1/node/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(req)))
			.andReturn();
		AddNodeResponse response = gson.fromJson(mvcResult.getResponse().getContentAsString(), AddNodeResponse.class);
		Long id = response.data.id;
		mvc.perform(get("/api/v1/node/del")
				.queryParam("experiment_id", "1")
				.queryParam("node_id", id.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	public void deleteNodeWithIllegalNodeId() throws Exception {
		mvc.perform(get("/api/v1/node/del")
				.queryParam("experiment_id", "1")
				.queryParam("node_id", "100"))
			.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	public void updateNode() throws Exception {
		Long id;
		{
			NodeDTO req = new NodeDTO();
			req.experimentId = 1L;
			req.nodeName = "abc";
			req.positionX = 100.;
			req.positionY = 200.;
			req.className = "com.alibaba.alink.ShuffleBatchOp";
			req.nodeType = NodeDTO.NodeType.FUNCTION;
			MvcResult mvcResult = mvc.perform(post("/api/v1/node/add")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(gson.toJson(req)))
				.andReturn();
			AddNodeResponse response = gson.fromJson(mvcResult.getResponse().getContentAsString(),
				AddNodeResponse.class);
			id = response.data.id;
		}

		String newName = "bca";
		{
			mvc.perform(get("/api/v1/node/update")
					.param("experiment_id", "1")
					.param("node_id", String.valueOf(id))
					.param("name", newName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
		}

		MvcResult mvcResult = mvc.perform(get("/api/v1/experiment/get_graph")
				.param("experiment_id", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andReturn();
		GetExperimentGraphResponse getExperimentResponse = gson
			.fromJson(mvcResult.getResponse().getContentAsString(), GetExperimentGraphResponse.class);
		for (Node node : getExperimentResponse.data.nodes) {
			if (node.getNodeId().equals(id)) {
				Assertions.assertEquals(newName, node.getName());
			}
		}
	}
}
