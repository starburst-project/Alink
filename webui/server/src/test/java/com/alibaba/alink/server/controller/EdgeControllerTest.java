package com.alibaba.alink.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.alibaba.alink.server.controller.EdgeController.EdgeDTO;
import com.alibaba.alink.server.controller.EdgeController.AddEdgeResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
class EdgeControllerTest {

	protected Gson gson = new GsonBuilder()
		.disableHtmlEscaping()
		.setPrettyPrinting()
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
		.create();

	@Autowired
	protected MockMvc mvc;

	@Test
	public void addEdge() throws Exception {
		EdgeDTO edge = new EdgeDTO();
		edge.experimentId = 1L;
		edge.srcNodeId = 1L;
		edge.srcNodePort = 0;
		edge.dstNodeId = 2L;
		edge.dstNodePort = 0;

		mvc.perform(post("/api/v1/edge/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(edge)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.id").isNumber());

	}

	@Test
	public void deleteEdge() throws Exception {
		EdgeDTO edge = new EdgeDTO();
		edge.experimentId = 1L;
		edge.srcNodeId = 1L;
		edge.srcNodePort = 0;
		edge.dstNodeId = 2L;
		edge.dstNodePort = 0;
		MvcResult mvcResult = mvc.perform(post("/api/v1/edge/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(edge)))
			.andReturn();
		AddEdgeResponse response = gson.fromJson(mvcResult.getResponse().getContentAsString(), AddEdgeResponse.class);
		Long id = response.data.id;
		mvc.perform(get("/api/v1/edge/del")
				.queryParam("experiment_id", "1")
				.queryParam("edge_id", id.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	public void deleteEdgeWithIllegalNodeId() throws Exception {
		mvc.perform(get("/api/v1/edge/del")
				.queryParam("edge_id", "100"))
			.andExpect(jsonPath("$.success").value(false));
	}
}
